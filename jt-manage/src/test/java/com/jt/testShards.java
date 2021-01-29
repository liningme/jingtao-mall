package com.jt;

import org.junit.jupiter.api.Test;
import redis.clients.jedis.*;

import java.lang.management.PlatformLoggingMXBean;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class testShards {


    @Test
    public void testCluster()
    {
        Set<HostAndPort> nodes = new HashSet<>();
        nodes.add(new HostAndPort("192.168.126.129",7000));
        nodes.add(new HostAndPort("192.168.126.129",7001));
        nodes.add(new HostAndPort("192.168.126.129",7002));
        nodes.add(new HostAndPort("192.168.126.129",7003));
        nodes.add(new HostAndPort("192.168.126.129",7004));
        nodes.add(new HostAndPort("192.168.126.129",7005));

        JedisCluster jedisCluster = new JedisCluster(nodes);
        jedisCluster.set("cluster","呜呼呼");
        System.out.println(jedisCluster.get("cluster"));

    }


    @Test
    public void testSentinel()
    {
        Set<String> sentinels = new HashSet<>();
        sentinels.add("192.168.126.129:26379");
        JedisSentinelPool pool = new JedisSentinelPool("mymaster",sentinels);
        Jedis jedis = pool.getResource();
        jedis.set("a","aa");
        System.out.println(jedis.get("a"));
    }

    @Test
    public void testShardDemo()
    {
        List<JedisShardInfo> shards = new ArrayList<>();
        shards.add(new JedisShardInfo("192.168.126.129",6379));
        shards.add(new JedisShardInfo("192.168.126.129",6380));
        shards.add(new JedisShardInfo("192.168.126.129",6381));

        ShardedJedis shardedJedis = new ShardedJedis(shards);
        shardedJedis.set("shards", "测试分片机制");
        System.out.println(shardedJedis.get("shards"));
    }


}
