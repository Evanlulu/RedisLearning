package com.evan.controller;


import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.evan.dto.Result;
import com.evan.dto.UserDTO;
import com.evan.entity.Blog;
import com.evan.entity.User;
import com.evan.service.IBlogService;
import com.evan.service.IUserService;
import com.evan.utils.SystemConstants;
import com.evan.utils.UserHolder;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import java.util.List;

/**
 * <p>
 * 前端控制器
 * </p>
 *
 * @author Evan
 * @since 20240624
 */
@RestController
@RequestMapping("/blog")
public class BlogController {

    @Resource
    private IBlogService blogService;

    @PostMapping
    public Result saveBlog(@RequestBody Blog blog) {
        // 獲取登錄用戶
        UserDTO user = UserHolder.getUser();
        blog.setUserId(user.getId());
        // 保存探店博文
        blogService.save(blog);
        // 返回id
        return Result.ok(blog.getId());
    }

    @PutMapping("/like/{id}")
    public Result likeBlog(@PathVariable("id") Long id) {
        
        return blogService.likeBlog(id);
    }

    @GetMapping("/of/me")
    public Result queryMyBlog(@RequestParam(value = "current", defaultValue = "1") Integer current) {
        // 獲取登錄用戶
        UserDTO user = UserHolder.getUser();
        // 根據用戶查询
        Page<Blog> page = blogService.query()
                .eq("user_id", user.getId()).page(new Page<>(current, SystemConstants.MAX_PAGE_SIZE));
        // 獲取當前頁數據
        List<Blog> records = page.getRecords();
        return Result.ok(records);
    }

    @GetMapping("/hot")
    public Result queryHotBlog(@RequestParam(value = "current", defaultValue = "1") Integer current) {
        return  blogService.queryHotBlog(current);
    }
    
    @GetMapping("/{id}")
    public Result queryNBlogById(@PathVariable("id")Long id){
        return  blogService.queryBlogById(id);
    }
    
    @GetMapping("/likes/{id}")
    public Result queryBlogLikes(@PathVariable("id")Long id){
        return  blogService.queyBlogLikes(id);
    }
}
