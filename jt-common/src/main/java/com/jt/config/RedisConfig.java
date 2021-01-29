package com.jt.config;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.PropertySource;
import org.springframework.data.domain.AfterDomainEventPublication;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.annotation.RestController;
import redis.clients.jedis.*;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

@Configuration //标识为一个配置类 一般与@bean注解连用
@PropertySource("classpath:/properties/redis.properties")
public class RedisConfig {

    @Value("${redis.nodes}")
    private String nodes;

    @Bean
    public JedisCluster jedisCluster()
    {
        Set<HostAndPort> hostAndPorts = new HashSet<>();
        String[] nodeArray = nodes.split(",");
        for (String node : nodeArray)
        {
            String hosts = node.split(":")[0];
            Integer ports = Integer.valueOf(node.split(":")[1]);
            HostAndPort str = new HostAndPort(hosts,ports) ;
            hostAndPorts.add(str);
        }
        return new JedisCluster(hostAndPorts);
    }
/*
    //配置哨兵机制
    @Value("${redis.sentinel}")
    public String sentinel;

    @Bean
    public JedisSentinelPool jedisSentinelPool()
    {
        //设定连接池的大小
        JedisPoolConfig poolConfig = new JedisPoolConfig();
        poolConfig.setMinIdle(5);//最小空闲数量
        poolConfig.setMaxIdle(10);//最大空闲数量
        poolConfig.setMaxTotal(100);//最大连接数

        //连接哨兵的集合
        Set<String> sentinels = new HashSet<>();
        sentinels.add(sentinel);
        return new JedisSentinelPool("mymaster",sentinels,poolConfig);

    }
*/





   /* @Value("${redis.nodes}")
    private String nodes;

    @Bean
    public ShardedJedis shardedJedis()
    {
        List<JedisShardInfo> shardInfoList = new ArrayList<>();
        String[] nodeArray = nodes.split(",");
        for (String node : nodeArray)
        {
            String host = node.split(":")[0];
            String port = node.split(":")[1];
            JedisShardInfo shardInfo = new JedisShardInfo(host,port);
            shardInfoList.add(shardInfo);

        }
        return new ShardedJedis(shardInfoList);


    }
*/



//    @Value("${redis.host}")
//    private String host;
//    @Value("${redis.port}")
//    private Integer port;
//
//    @Bean
//    public Jedis jedis()
//    {
//        return new Jedis(host, port);
//    }
}
