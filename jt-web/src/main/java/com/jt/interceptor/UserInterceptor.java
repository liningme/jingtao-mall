package com.jt.interceptor;

import com.jt.pojo.User;
import com.jt.util.CookieUtil;
import com.jt.util.ObjectMapperUtil;
import com.jt.util.UserThreadLocal;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.repository.config.RepositoryNameSpaceHandler;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;
import org.springframework.web.servlet.HandlerInterceptor;
import org.springframework.web.servlet.ModelAndView;
import redis.clients.jedis.JedisCluster;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;


@Component
public class UserInterceptor implements HandlerInterceptor {
    @Autowired
    private JedisCluster jedisCluster;

    //处理器执行之前  用户不登录拦截     登录放行
    //判断依据:     cookie  redis
    //返回值说明:   false  表示拦截     true 放行
    //实现用户数据动态获取


    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {

        String ticket = CookieUtil.getCookieValue(request,"JT_TICKET");
        //1.从redis中把数据进行获取

        if (StringUtils.hasLength(ticket) && jedisCluster.exists(ticket))
        {
            String json = jedisCluster.get(ticket);
            User user = ObjectMapperUtil.toObj(json, User.class);
            //2.利用request对象实现数据的传递  使用最多的方式
            request.setAttribute("JT_USER",user);
            //3.利用ThreadLocal方式进行数据存储
            UserThreadLocal.set(user);
            return true;//程序放行
        }



        //请求如果被拦截,一般都要配合重定向的方式使用
        response.sendRedirect("/user/login.html");
        return false;
    }
    //处理器执行之后
    @Override
    public void postHandle(HttpServletRequest request, HttpServletResponse response, Object handler, ModelAndView modelAndView) throws Exception {

    }

    @Override
    public void afterCompletion(HttpServletRequest request, HttpServletResponse response, Object handler, Exception ex) throws Exception {

        //1.清空request对象
        request.removeAttribute("JT_USER");
        //2 清空threadLocal数据
        UserThreadLocal.remove();


    }

}
