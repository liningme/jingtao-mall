package com.jt.service;

import com.alibaba.dubbo.config.annotation.Service;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.jt.mapper.UserMapper;
import com.jt.pojo.User;
import com.jt.util.ObjectMapperUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.DigestUtils;
import org.springframework.util.StringUtils;
import redis.clients.jedis.JedisCluster;

import java.util.UUID;


@Service(timeout = 3000)
public class DubboUserServiceImpl implements DubboUserService {
    @Autowired
    private UserMapper userMapper;
    @Autowired
    private JedisCluster jedisCluster;
    /*
     * 根据用户名和密码实现数据查询
     * @param user
     *
     * @return
     */
    @Override
    public String findUserByUP(User user) {
        //1 对密码进行加密
        String md5Pass = DigestUtils.md5DigestAsHex(user.getPassword().getBytes());
        user.setPassword(md5Pass);
        //2 根据用户名和密码查询数据库，根据对象不为null的属性当做where条件
        QueryWrapper<User> queryWrapper = new QueryWrapper<>(user);
        User userDb = userMapper.selectOne(queryWrapper);
        //3 判断信息是否有效
        if (userDb == null)
        {
            return null;
        }
        //4 如果用户名和密码正确开始进行单点登录操作
        String ticket = UUID.randomUUID().toString().replace("-","");
        //5 将用户数据转换为json，需要对数据进行脱敏处理
        userDb.setPassword("123qwer!");
        String json = ObjectMapperUtil.toJSON(userDb);
        //6 将数据保存在redis中，有效期30天
        jedisCluster.setex(ticket,30 * 24 * 60 * 60,json);

        return ticket;
    }


    /*暂时使用电话号码代替email
    * 需要对密码加密 md5hash*/
    @Override
    public void doRegister(User user) {
        user.setEmail(user.getPhone());
        String md5Password = DigestUtils.md5DigestAsHex(user.getPassword().getBytes());
        user.setPassword(md5Password);
        userMapper.insert(user);

    }


}
