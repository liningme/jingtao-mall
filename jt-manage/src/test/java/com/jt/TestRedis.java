package com.jt;
import org.junit.jupiter.api.Test;
import redis.clients.jedis.Jedis;
import redis.clients.jedis.params.SetParams;

public class TestRedis {

    //如果数据存在 不做任何操作,如果数据不存在则赋值
    @Test
    public void test02(){
        Jedis jedis = new Jedis("192.168.126.129", 6379);
        jedis.flushAll();
        //jedis.set("redis", "AAA");
        jedis.setnx("redis","BBB");
        System.out.println(jedis.get("redis"));
    }

    //添加一个数据,并且设定超时时间 10秒
    //设定超时时间应该注意
    // 原子性: 要么同时成功/要么同时失败.
    @Test
    public void test03() throws InterruptedException {
        Jedis jedis = new Jedis("192.168.126.129", 6379);
        //jedis.set("a","设定超时时间");
        //jedis.expire("a", 10);
        jedis.setex("a", 10, "设定超时时间");
        Thread.sleep(2000);
        System.out.println("剩余时间" + jedis.ttl("a"));
    }

    /**
     * 1.如果数据存在,则不允许修改  反之允许修改
     * 2.同时为数据设定超时时间. 10秒
     * 3.上述操作需要满足原子性要求
     *  XX: 存在才修改
     *  NX: 不存在才修改
     *  PX: 毫秒
     *  EX: 秒
     */
    @Test
    public void test04(){
        Jedis jedis = new Jedis("192.168.126.129", 6379);
        SetParams setParams = new SetParams();
        setParams.nx().ex(10);
        jedis.set("b", "xxxx",setParams);
        System.out.println(jedis.get("b"));
    }

    /**
     * 需求: 判断数据是否存在
     *      如果存在 则删除数据
     *      如果不存在则赋值操作
     */
    @Test
    public void testString()
    {
        Jedis jedis = new Jedis("192.168.126.129",6379);
        if (jedis.exists("redis"))
        {
            jedis.set("redis","aa");
        }
        else
        {
            jedis.set("redis","hello");
            System.out.println(jedis.get("redis"));
        }
    }
}
