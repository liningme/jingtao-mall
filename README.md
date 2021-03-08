# 京淘项目

#### 介绍

####  **介绍** 
实现的功能：
1.  使用Dubbo框架搭建微服务系统。
1.  使用异步树加载、EasyUITree、Ajax实现后台对商品的增删改查。
1.  使用Redis集群和AOP对商品分类菜单进行缓存。
1.  使用Nginx实现商品后台集群的负载均衡和高可用。
1.  使用Mycat实现读写分离负载均衡、完成MySQL数据库的双机热备以实现其高可用。
1.  使用Zookeeper集群、Redis集群、cookie实现用户的单点登录、购物车的CRUD、订单操作（确认、入库和订单的查询），通过拦截器interceptor实现权限的控制。

#### 软件架构

Spring Boot+ MyBatisPlus + Dubbo + Redis+ Nginx+ Zookeeper+ Mycat+ CORS+ MySQL+ EasyUI + Ajax




#### 业务实现
#####  1 使用Dubbo框架搭建微服务系统
1.  介绍：Apache Dubbo 是一款高性能、轻量级的开源 Java 服务框架。提供了六大核心能力：高性能RPC调用，智能容错和负载均衡，服务自动注册和发现，高度可扩展能力，运行期流量调度，可视化的服务治理与运维。
1.  Dubbo属于客户端负载均衡，由客户端自己进行负载均衡，客户端根据负载均衡的结果直接访问服务器。
1.  Dubbo负载均衡策略：一致性hash算法、最少访问、随机算法（Random）、轮询机制

#####  2 使用异步树加载、EasyUITree、Ajax实现后台对商品的增删改查。

1.  异步树加载：选择类目的展现。树控件读取URL，子节点的加载依赖于父节点的状态，当展开一个封闭的节点，如果节点没有加载子节点，它将把节点id的值作为http请求参数并命名为“id”，通过URL发送到服务器上面检索子节点。
2.  商品列表的展现：ItemController　-> ItemService（MP方式实现分页、IPage、编辑配置类）
3.  商品分类展现：EasyUITree(POJO) -> 异步树加载 ->　ItemCatController　->　ItemCatService
~~~
EasyUITree：id（编号）、text（节点名称）、state（节点状态）
~~~
4.  商品新增：封装系统返回值　-> ItemController　-> ItemService　
~~~
  `id` bigint(20) NOT NULL AUTO_INCREMENT COMMENT '商品id，同时也是商品编号',
  `title` varchar(100) NOT NULL COMMENT '商品标题',
  `sell_point` varchar(500) DEFAULT NULL COMMENT '商品卖点',
  `price` bigint(20) NOT NULL COMMENT '商品价格，单位为：分',
  `num` int(10) NOT NULL COMMENT '库存数量',
  `barcode` varchar(30) DEFAULT NULL COMMENT '商品条形码',
  `image` varchar(500) DEFAULT NULL COMMENT '商品图片',
  `cid` bigint(10) NOT NULL COMMENT '所属类目，叶子类目',
  `status` tinyint(4) NOT NULL DEFAULT '1' COMMENT '商品状态，1-正常，2-下架，3-删除',
  `created` datetime NOT NULL COMMENT '创建时间',
  `updated` datetime NOT NULL COMMENT '更新时间',
~~~
5.  实现商品修改操作：ItemController ->　ItemService（MP的updateById）
6.  实现商品删除：ItemController -> ItemService -> ItemMapper
~~~
//ItemMapper
<delete id="deleteItems">
    delete from tb_items 
    where id in
    <foreach collection="array" open="(" sperator="," item="id">
    #{id}
    </foreach>
</delete>
~~~

##### 3 完成购物车功能 CRUD操作
1.  实现购物车列表页面：
通过userId，查询cart数据库，selectList。
1.  实现购物车数量更新：
通过userId、itemId，查询cart数据库，使用mp的update。
1.  实现购物车入库操作
~~~
说明: 当用户点击加入购物车时，可以将数据添加到购物车列表中。
如果购物车中已经存在数据，则更新数量；如果购物车中没有数据，则新增数据，通过userId、itemId，查询cart数据库，使用mp的update。
~~~

##### 3 订单业务逻辑 表3张 为对象的引用赋值 多张表业务操作 注意事务控制
	1）订单确认页面跳转
	ThreadLocal方式动态取id值，根据id值，通过购物车service完成订单确认页面跳转
	2）订单入库操作
	订单入库、订单物流入库、订单商品入库，mp的Insert操作
	3）实现订单查询
	当用户通过id查询订单信息时 查询订单的三张表数据（order、orderShipping 、OrderItem） 之后返回给页面即可

![表设计](https://images.gitee.com/uploads/images/2021/0308/172257_0fffbcaf_5549520.png "屏幕截图.png")


##### 4 使用Redis集群和AOP对商品分类菜单进行缓存
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



##### 5 单点登录
1.  用户通过用户名和密码访问web服务器
2.  WEB服务器通过SSO校验用户名和密码是否正确
2. 如果用户名和密码正确,则将数据保存到redis中. 将TICKET密钥返回给用户
3.  为了客户端动态获取数据，将ticket保存在cookie中

##### 6 用户回显功能
如果用户登录成功之后，会在cookie中记录TICKET登录凭证，利用跨域机制(JSONP|CORS) 通过TICKET数据信息查询用户数据，之后返回。
##### 7 用户退出操作：删除TICKET/REDIS中的数据

##### 8 完成权限控制--基于拦截器
通过拦截器实现权限的控制，如果用户不登录，则不允许访问涉密数据，使用的接口HandlerInterceptor 其中有3个方法用来控制程序运行的轨迹. 在拦截器中如果需要实现数据的传递，可以使用ThreadLocal或者Request对象的方式获取。
但是需要注意:数据存储之后记得清空，防止内存泄露。
