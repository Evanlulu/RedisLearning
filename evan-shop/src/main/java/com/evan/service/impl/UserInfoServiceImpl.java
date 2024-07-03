package com.evan.service.impl;

import com.evan.entity.UserInfo;
import com.evan.mapper.UserInfoMapper;
import com.evan.service.IUserInfoService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.stereotype.Service;

/**
 * <p>
 *  服務實現類
 * </p>
 *
 * @author Evan
 * @since 2021-12-24
 */
@Service
public class UserInfoServiceImpl extends ServiceImpl<UserInfoMapper, UserInfo> implements IUserInfoService {

}
