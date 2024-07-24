package com.evan.service;

import com.evan.dto.Result;
import com.evan.entity.Blog;
import com.baomidou.mybatisplus.extension.service.IService;

/**
 * <p>
 *  服務類
 * </p>
 *
 * @author Evan
 * @since 20240624
 */
public interface IBlogService extends IService<Blog> {

    Result queryHotBlog(Integer id);

    Result queryBlogById(Long id);

    Result likeBlog(Long id);

    Result queyBlogLikes(Long id);

    Result saveBlog(Blog blog);

    Result queryBlogOfFollow(Long max, Integer offset);
}
