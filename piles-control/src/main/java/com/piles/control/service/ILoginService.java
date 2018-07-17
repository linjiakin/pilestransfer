package com.piles.control.service;

import com.piles.control.entity.LoginRequest;

/**
 * 登录接口
 */
public interface ILoginService {
    /**
     * 登录身份验证
     * @param loginRequest 请求体
     * @return 验证成功返回true，失败返回false
     */
    boolean login(LoginRequest loginRequest);
}
