# 京淘项目

#### 介绍

实现的功能：

1、使用Dubbo框架搭建微服务系统。

2、使用异步树加载、EasyUITree、Ajax实现后台对商品的增删改查。

3、使用Redis集群和AOP对商品分类菜单进行缓存。

4、使用Nginx实现商品后台集群的负载均衡和高可用。

5、使用Mycat实现读写分离负载均衡、完成MySQL数据库的双机热备以实现其高可用。

6、完成Zookeeper集群搭建，使用Redis、拦截器interceptor、和cookie实现单点登录和权限控制。


#### 软件架构

Spring Boot+ MyBatisPlus + Dubbo + Redis+ Nginx+ Zookeeper+ Mycat+ CORS+ MySQL+ EasyUI + Ajax


#### 业务实现

##### 业务实现--单点登录
1.  用户通过用户名和密码访问web服务器
2.  WEB服务器通过SSO校验用户名和密码是否正确
2. 如果用户名和密码正确,则将数据保存到redis中. 将TICKET密钥返回给用户
3.  为了客户端动态获取数据，将ticket保存在cookie中

##### 业务实现--用户回显功能
如果用户登录成功之后，会在cookie中记录TICKET登录凭证，利用跨域机制(JSONP|CORS) 通过TICKET数据信息查询用户数据，之后返回。
##### 业务实现--用户退出操作：删除TICKET/REDIS中的数据

##### 业务实现--完成购物车功能 CRUD操作
1.  实现购物车列表页面：
通过userId，查询cart数据库，selectList。
1.  实现购物车数量更新：
通过userId、itemId，查询cart数据库，使用mp的update。
1.  实现购物车入库操作
~~~
说明: 当用户点击加入购物车时，可以将数据添加到购物车列表中。
如果购物车中已经存在数据，则更新数量；如果购物车中没有数据，则新增数据，通过userId、itemId，查询cart数据库，使用mp的update。
~~~
##### 业务实现--使用Redis集群和AOP对商品分类菜单进行缓存
1.  定义自定义注解cacheFind，应该指定key的名称,设定超时时间。
2.  添加自定义注解@cacheFind(key = "XX")
2.  定义aop切面
3.  定义环绕通知方法
~~~
@Component      //交给Spring容器管理
@Aspect         //标识AOP切面类
@Around("@annotation(cacheFind)")
拦截被自定义注解标识的方法 之后利用aop进行缓存的控制
根据key查询redis，查询redis缓存
    有: 直接获取缓存数据返回给用户
    没有: 直接查询数据库，之后将返回值结果保存到redis中，方便下次使用
~~~


##### 完成权限控制--基于拦截器
通过拦截器实现权限的控制，如果用户不登录，则不允许访问涉密数据，使用的接口HandlerInterceptor 其中有3个方法用来控制程序运行的轨迹. 在拦截器中如果需要实现数据的传递，可以使用ThreadLocal或者Request对象的方式获取。
但是需要注意:数据存储之后记得清空，防止内存泄露。





#### 参与贡献

1.  Fork 本仓库
2.  新建 Feat_xxx 分支
3.  提交代码
4.  新建 Pull Request


