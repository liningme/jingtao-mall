package com.jt.util;

import com.jt.pojo.User;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.HandlerInterceptor;

public class UserThreadLocal implements HandlerInterceptor {
    private static ThreadLocal<User> threadLocal = new ThreadLocal<>();

    //存数据
    public static void set(User user)
    {
        threadLocal.set(user);
    }

    //取数据
    public static User get()
    {
        return threadLocal.get();
    }

    //数据删除，防止内存泄漏
    public static void remove()
    {
        threadLocal.remove();
    }
}
