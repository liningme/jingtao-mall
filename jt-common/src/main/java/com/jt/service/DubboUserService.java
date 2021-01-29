package com.jt.service;

import com.alibaba.dubbo.config.annotation.Service;
import com.jt.pojo.User;


public interface DubboUserService {
    void doRegister(User user);

    String findUserByUP(User user);
}
