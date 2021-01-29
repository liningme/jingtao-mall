package com.jt.controller;

import com.alibaba.dubbo.config.annotation.Reference;
import com.jt.pojo.User;
import com.jt.service.DubboUserService;
import com.jt.service.UserService;
import com.jt.util.CookieUtil;
import com.jt.vo.SysResult;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import redis.clients.jedis.JedisCluster;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.List;


@Controller
@RequestMapping("/user")
public class UserController {

    @Autowired
    private UserService userService;

    @Reference(check = false)
    private DubboUserService dubboUserService;

    @Autowired
    private JedisCluster jedisCluster;

    /*
     * 用户退出操作
     *  url : http://www.jt.com/user/logout.html
     *  返回值: 重定向到系统首页
     *  实现步骤:
     *        1. 从cookie中获取ticket信息
     *        2. 删除Redis中的数据
     *        3. 删除cookie中的记录
     */
    @RequestMapping("/logout")
    public String logOut(HttpServletResponse response, HttpServletRequest request)
    {
        String ticket = CookieUtil.getCookieValue(request,"JT_TICKET");
        if (StringUtils.hasLength(ticket))
        {
            jedisCluster.del(ticket);//删除redis中的数据
            CookieUtil.addCookie(response,"JT_TICKET","","jt.com",0  );
        }
        return "redirect:/";
    }




    /*
     * url: http://www.jt.com/user/doLogin?r=0.4511522931461409
     * 参数: username/password
     * 返回值: SysResult对象
     *
     * cookie相同知识:
     *  http://xxx/addUser;
     *  http://xxx/aaa/addUser;
     *
     *  1.cookie.setPath("/aaa");
     *  实现用户单点登录业务:
     *  步骤:
     *      1.接收用户名和密码 并且校验是否有效
     *      2.将username和password发送到sso进行数据校验
     *      3.判断ticket数据信息是否有效
     *      4.如果ticket有效则将数据写入cookie
     */

    @RequestMapping("/doLogin")
    @ResponseBody
    public SysResult doLogin(User user, HttpServletResponse response)
    {
        //1 判断数据是否有效
        if(StringUtils.isEmpty(user.getUsername()) || StringUtils.isEmpty(user.getPassword()))
        {
            SysResult.fail();
        }

        //2 完成用户信息的校验
        String ticket = dubboUserService.findUserByUP(user);
        //3 判断返回值是否有效
        if (StringUtils.hasLength(ticket))
        {
            //开始进行单点操作
            Cookie cookie = new Cookie("JT_TICKET", ticket);
            cookie.setPath("/");//访问路径
            cookie.setMaxAge(30 * 24 * 60 * 60 ) ;//30天超时
            cookie.setDomain("jt.com"); //设定数据共享
            response.addCookie(cookie);
            return SysResult.success();
        }
        return SysResult.fail();

    }




    /*
     * 完成用户注册    公司开发时:业务接口文档
     *  url地址:  http://www.jt.com/user/doRegister
     *  参数:     注册表单提交
     *  业务逻辑:  接收用户信息,将数据密码之后保存到数据库中
     *  返回值:   SysResult对象
     */

    @RequestMapping("/doRegister")
    @ResponseBody //转换为json
    public SysResult doRegister(User user)
    {
        dubboUserService.doRegister(user);
        return SysResult.success();

    }







    /*
     * 完成httpClient入门案例测试,返回JSON数据
     */

    @RequestMapping("/findUserList")
    @ResponseBody
    public List<User> findUserList()
    {
        return userService.findUserList();
    }


    /*
     * 实现通用的页面跳转
     * url1: http://www.jt.com/user/login.html     系统登录    页面名称 login.jsp
     * url2: http://www.jt.com/user/register.html 注册操作    页面名称 register.jsp
     */
    @RequestMapping("/{modelName}")
    public String model(@PathVariable String modelName){

        return modelName;
    }



}
