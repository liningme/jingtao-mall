package com.jt.util;

import org.apache.http.HttpResponse;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

public class CookieUtil {
    /*
     * 1.保存cookie
     * path: 一般都是/
     */
    public static void addCookie(HttpServletResponse response,
                                 String cookieName,
                                 String cookieValue,
                                 String domain,
                                 Integer seconds)
    {
        Cookie cookie = new Cookie(cookieName,cookieValue);
        cookie.setDomain(domain);
        cookie.setMaxAge(seconds);
        cookie.setPath("/");
        response.addCookie(cookie);

    }
    //2 获取cookie
    public static Cookie getCookie(HttpServletRequest request,String cookieName)
    {
        Cookie cookieTem = null;
        Cookie[] cookies = request.getCookies();
        if (cookies != null && cookies.length > 0)
        {
            for (Cookie cookie : cookies)
            {
                if (cookieName.equals(cookie.getName()))
                {
                    cookieTem = cookie;
                    break;
                }

            }
        }
        return cookieTem;

    }

    //3 获取cookie的值
    public static String getCookieValue(HttpServletRequest request,String cookieName)
    {
        Cookie cookie = getCookie(request,cookieName);
        return cookie == null ? null : cookie.getValue();
    }
}
