package com.evan.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.evan.dto.Result;
import com.evan.entity.Follow;
import com.evan.mapper.FollowMapper;
import com.evan.service.IFollowService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.evan.utils.UserHolder;
import org.springframework.stereotype.Service;

/**
 * <p>
 *  服務實現類
 * </p>
 *
 * @author Evan
 * @since 20240624
 */
@Service
public class FollowServiceImpl extends ServiceImpl<FollowMapper, Follow> implements IFollowService {

    @Override
    public Result follow(Long followUserId, Boolean isFollow) {
        Long userId = UserHolder.getUser().getId();
        if (isFollow){
            Follow follow = new Follow();
            follow.setUserId(userId);
            follow.setFollowUserId(followUserId);
            save(follow);
        }else {
            remove(new QueryWrapper<Follow>()
                    .eq("user_id",userId).eq("follow_user_id", followUserId));
        }
        
        return Result.ok();
    }

    @Override
    public Result isFollow(Long followUserId) {
        Long userId = UserHolder.getUser().getId();
        Integer count = query().eq("user_id", userId).eq("follow_user_id", followUserId).count();
        
        return Result.ok(count() > 0);
    }
}
