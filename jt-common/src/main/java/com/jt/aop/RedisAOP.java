package com.jt.aop;

import annoation.CacheFind;
import com.jt.util.ObjectMapperUtil;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.Signature;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.reflect.MethodSignature;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import redis.clients.jedis.Jedis;
import redis.clients.jedis.JedisCluster;
import redis.clients.jedis.JedisSentinelPool;
import redis.clients.jedis.ShardedJedis;

import java.lang.reflect.Array;
import java.util.Arrays;

@Aspect
@Component
public class RedisAOP {
    @Autowired
    private JedisCluster jedis;
//    private JedisSentinelPool sentinelPool;
//    private ShardedJedis jedis;
    //1. 定义切入点表达式     2.定义通知方法


    /*
     * 实现AOP缓存:
     *      1.准备key = 获取key的前缀 + "动态拼接参数"
     *      2.从redis中获取数据
     *              结果1:  没有数据,查询数据库,之后将数据保存到缓存中
     *              结果2:  有数据,  直接将缓存数据返回
     * @param joinPoint
     * @return
     * @throws Throwable
     */
    @Around("@annotation(cacheFind)")
    public Object around(ProceedingJoinPoint joinPoint, CacheFind cacheFind) throws Throwable {

        //哨兵：从池中, 动态获取数据
//        Jedis jedis = sentinelPool.getResource();


        String preKey = cacheFind.key();
        String args = Arrays.toString(joinPoint.getArgs());
        String key = preKey + args;
        Object result = null;

        //2.判断redis中是否有数据
        if(jedis.exists(key))
        {
            String json = jedis.get(key);
            //获取返回值类型
            MethodSignature methodSignature = (MethodSignature) joinPoint.getSignature();
            Class returnType = methodSignature.getReturnType();

            result = ObjectMapperUtil.toObj(json,returnType);
            System.out.println("查询Redis缓存!!!");

        }
        else
        {
            //表示缓存中没有数据,应该查询数据库动态获取
            result = joinPoint.proceed(); //调用下一个通知/目标方法
            //应该将数据保存到缓存中
            String json = ObjectMapperUtil.toJSON(result);
            if (cacheFind.seconds() > 0)
            {
                jedis.setex(key,cacheFind.seconds(),json);
            }
            else
            {
                jedis.set(key,json);
            }
            System.out.println("AOP查询数据库!!!");

        }
        return result;

    }

}
