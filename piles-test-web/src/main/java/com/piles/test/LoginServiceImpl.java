package com.piles.test;

import com.piles.control.entity.LoginRequest;
import com.piles.control.service.ILoginService;
import org.springframework.stereotype.Service;

@Service
public class LoginServiceImpl implements ILoginService{
    @Override
    public boolean login(LoginRequest loginRequest) {
        return true;
    }
}
