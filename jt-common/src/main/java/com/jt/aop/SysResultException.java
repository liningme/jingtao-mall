package com.jt.aop;

import com.fasterxml.jackson.databind.util.JSONPObject;
import com.jt.vo.SysResult;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;

@RestControllerAdvice
public class SysResultException {
    /*
     * 当程序遇到什么样的异常时,程序开始执行
     * 应该返回什么样的数据 SysResult
     *
     * JSONP调用时,应该让数据按照特定的格式进行数据返回  callback(sysResult.fail());
     * 判断依据: callback 参数
     */
    @ExceptionHandler(RuntimeException.class)
    public Object fail(Exception e, HttpServletRequest request)
    {
        String callback = request.getParameter("callback");
        if (StringUtils.hasLength(callback))
        {
            e.printStackTrace();
            //有值，代表的是jsonp的跨域请求
            return new JSONPObject(callback,SysResult.fail());
        }

        e.printStackTrace();
        return SysResult.fail();

    }

}
