package com.evan.service.impl;

import cn.hutool.core.bean.BeanUtil;
import cn.hutool.core.util.BooleanUtil;
import cn.hutool.core.util.StrUtil;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.evan.dto.Result;
import com.evan.dto.UserDTO;
import com.evan.entity.Blog;
import com.evan.entity.Follow;
import com.evan.entity.User;
import com.evan.mapper.BlogMapper;
import com.evan.service.IBlogService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.evan.service.IFollowService;
import com.evan.service.IUserService;
import com.evan.utils.SystemConstants;
import com.evan.utils.UserHolder;
import jodd.util.StringUtil;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.Collections;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

import static com.evan.utils.RedisConstants.BLOG_LIKED_KEY;

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
    
    @Resource
    private IFollowService followService;
    
    @Override
    public Result queryHotBlog(Integer id) {
        // 根據用戶查询
        Page<Blog> page = query()
                .orderByDesc("liked")
                .page(new Page<>(id, SystemConstants.MAX_PAGE_SIZE));
        // 獲取當前頁數據
        List<Blog> records = page.getRecords();
        // 查询用戶
        records.forEach(blog -> {
            this.queryBlogUser(blog);
            this.isBlogLiked(blog);
        });
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
        Blog blog = getById(id);
        if (blog == null)
            return Result.fail("筆記不存在");
        queryBlogUser(blog);
        isBlogLiked(blog);
        return Result.ok(blog);
    }

    private void isBlogLiked(Blog blog) {
        UserDTO user = UserHolder.getUser();
        if (user == null)
            return;
        Long userId = user.getId();
        String key = "blog:liked:" + blog.getId();
//        Boolean isMember = stringRedisTemplate.opsForSet().isMember(key, userId.toString());
        Double score = stringRedisTemplate.opsForZSet().score(key, userId.toString());
//        blog.setIsLike(BooleanUtil.isTrue(isMember));
        blog.setIsLike(score != null);
    }

    @Override
    public Result likeBlog(Long id) {
        Long userId = UserHolder.getUser().getId();
        String key = "blog:liked:" + id;
        Double score = stringRedisTemplate.opsForZSet().score(key, userId.toString());
//        Boolean isMember = stringRedisTemplate.opsForSet().isMember(key, userId.toString());
//        if (BooleanUtil.isFalse(isMember)){
        if (score == null){
            // 修改點讚數量
            boolean isSuccess = update().setSql("liked = liked + 1").eq("id", id).update();
            if (isSuccess)
                stringRedisTemplate.opsForZSet().add(key, userId.toString(),System.currentTimeMillis()); //排名
        }else{
            boolean isSuccess = update().setSql("liked = liked - 1").eq("id", id).update();
            if (isSuccess)
                stringRedisTemplate.opsForZSet().remove(key, userId.toString());
        }
            

        return null;
    }

    @Override
    public Result queyBlogLikes(Long id) {
        String key = BLOG_LIKED_KEY + id;
        Set<String> top5 = stringRedisTemplate.opsForZSet().range(key, 0, 4);
        if (top5 == null || top5.isEmpty())
            return Result.ok(Collections.emptyList());
        List<Long> ids = top5.stream().map(Long::valueOf).collect(Collectors.toList());
        String idStr = StrUtil.join(",",ids);
//        List<UserDTO> userDTOS = userService.listByIds(ids) // sql 會下 in 導致排序會有問題
        List<UserDTO> userDTOS = userService.query()
                .in("id", ids)
                .last("ORDER BY FIELD(id," + idStr + ")").list() //調整順序
                .stream()
                .map(user -> BeanUtil.copyProperties(user, UserDTO.class))
                .collect(Collectors.toList());
        return Result.ok(userDTOS);
    }

    @Override
    public Result saveBlog(Blog blog) {
        UserDTO user = UserHolder.getUser();
        blog.setUserId(user.getId());
        boolean isSuccess = save(blog);
        if (!isSuccess){
            return Result.fail("新增筆記失敗");
        }

        List<Follow> follows = followService.query().eq("follow_user_id", user.getId()).list();
        for (Follow follow : follows) {
            Long userId = follow.getUserId();
            String key = "feed:" + userId;
            stringRedisTemplate.opsForZSet().add(key, blog.getId().toString(), System.currentTimeMillis());
        }
        return Result.ok(blog.getId());
    }
}
