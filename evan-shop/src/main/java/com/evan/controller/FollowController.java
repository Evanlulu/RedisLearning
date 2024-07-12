package com.evan.controller;


import com.evan.dto.Result;
import com.evan.service.IFollowService;
import com.sun.org.apache.xpath.internal.operations.Bool;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;

/**
 * <p>
 *  前端控制器
 * </p>
 *
 * @author Evan
 * @since 20240624
 */
@RestController
@RequestMapping("/follow")
public class FollowController {
    @Resource
    private IFollowService followService;
    
    @PutMapping("/{id}/{isFollow}")
    public Result follow(@PathVariable("id")Long followUserId, @PathVariable("isFollow")Boolean isFollow){
        return followService.follow(followUserId, isFollow);
    }

    @GetMapping("/or/not/{id}")
    public Result follow(@PathVariable("id")Long followUserId){
        return followService.isFollow(followUserId);
    }
}
