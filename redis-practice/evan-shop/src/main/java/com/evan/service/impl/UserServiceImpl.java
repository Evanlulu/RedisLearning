package com.evan.service.impl;

import cn.hutool.core.util.RandomUtil;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.evan.dto.LoginFormDTO;
import com.evan.dto.Result;
import com.evan.entity.User;
import com.evan.mapper.UserMapper;
import com.evan.service.IUserService;
import com.evan.utils.RegexUtils;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import javax.servlet.http.HttpSession;

import static com.evan.utils.SystemConstants.USER_NICK_NAME_PREFIX;

/**
 * <p>
 * 服務實現
 * </p>
 *
 * @author Evan
 * @since 2024-06-19
 */
@Service
@Slf4j
public class UserServiceImpl extends ServiceImpl<UserMapper, User> implements IUserService {

    @Override
    public Result sendCode(String phone, HttpSession session) {
        
        if(RegexUtils.isPhoneInvalid(phone)){
            return  Result.fail("手機號碼格式錯誤");
        }

        String code = RandomUtil.randomNumbers(6);

        session.setAttribute("code", code);
        
        log.debug("發送驗證碼成功");
        
        return Result.ok();
    }

    @Override
    public Result login(LoginFormDTO loginForm, HttpSession session) {

        String phone = loginForm.getPhone();
        if(RegexUtils.isPhoneInvalid(phone)){
            return  Result.fail("手機號碼格式錯誤");
        }

        Object cacheCode = session.getAttribute("code");
        String code = loginForm.getCode();
        if (cacheCode == null || !cacheCode.toString().equals(code)){
            return  Result.fail("驗證碼錯誤");
        }

        User user = query().eq("phone", phone).one();
        
        if (user == null ){
            user = createUserWithPhone(phone);
        }
        
        session.setAttribute("user", user);

        return Result.ok();
    }

    private User createUserWithPhone(String phone) {
        User user = new User();
        user.setPhone(phone);
        user.setNickName(USER_NICK_NAME_PREFIX + RandomUtil.randomString(10));
        save(user);
        return  user;
    }
}
