package com.evan.service.impl;

import com.evan.entity.BlogComments;
import com.evan.mapper.BlogCommentsMapper;
import com.evan.service.IBlogCommentsService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.stereotype.Service;

/**
 * <p>
 *  服務实现類
 * </p>
 *
 * @author Evan
 * @since 20240624
 */
@Service
public class BlogCommentsServiceImpl extends ServiceImpl<BlogCommentsMapper, BlogComments> implements IBlogCommentsService {

}
