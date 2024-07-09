package com.evan.service.impl;

import cn.hutool.core.util.BooleanUtil;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.evan.dto.Result;
import com.evan.entity.Blog;
import com.evan.entity.User;
import com.evan.mapper.BlogMapper;
import com.evan.service.IBlogService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.evan.service.IUserService;
import com.evan.utils.SystemConstants;
import com.evan.utils.UserHolder;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.List;

/**
 * <p>
 *  服務實現類
 * </p>
 *
 * @author Evan
 * @since 20240624
 */
@Service
public class BlogServiceImpl extends ServiceImpl<BlogMapper, Blog> implements IBlogService {

    @Resource
    private IUserService userService;
    
    @Resource
    private StringRedisTemplate stringRedisTemplate;


    @Override
    public Result queryHotBlog(Integer id) {
        // 根據用戶查询
        Page<Blog> page = query()
                .orderByDesc("liked")
                .page(new Page<>(id, SystemConstants.MAX_PAGE_SIZE));
        // 獲取當前頁數據
        List<Blog> records = page.getRecords();
        // 查询用戶
        records.forEach(this::queryBlogUser);
        return Result.ok(records);
    }

    private void queryBlogUser(Blog blog) {
        Long userId = blog.getUserId();
        User user = userService.getById(userId);
        blog.setName(user.getNickName());
        blog.setIcon(user.getIcon());
    }

    @Override
    public Result queryBlogById(Long id) {
        Blog byId = getById(id);
        if (byId == null)
            return Result.fail("筆記不存在");
        queryBlogUser(byId);
        return Result.ok(byId);
    }

    @Override
    public Result likeBlog(Long id) {
        Long userId = UserHolder.getUser().getId();
        String key = "blog:liked:" + id;
        Boolean isMember = stringRedisTemplate.opsForSet().isMember(key, userId.toString());
        if (BooleanUtil.isFalse(isMember)){
            // 修改點讚數量
            boolean isSuccess = update().setSql("liked = liked + 1").eq("id", id).update();
            if (isSuccess)
                stringRedisTemplate.opsForSet().add(key, userId.toString());
        }else{
            boolean isSuccess = update().setSql("liked = liked - 1").eq("id", id).update();
            if (isSuccess)
                stringRedisTemplate.opsForSet().add(key, userId.toString());
        }
            

        return null;
    }
}
