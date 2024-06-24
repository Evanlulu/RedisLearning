package com.evan.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.evan.dto.LoginFormDTO;
import com.evan.dto.Result;
import com.evan.entity.User;

import javax.servlet.http.HttpSession;

/**
 * <p>
 *  服務類
 * </p>
 *
 * @author Evan
 * @since 20240624
 */
public interface IUserService extends IService<User> {

    Result sendCode(String phone, HttpSession session);

    Result login(LoginFormDTO loginForm, HttpSession session);
}
