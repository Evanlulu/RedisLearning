package com.evan.jedis.util;

import redis.clients.jedis.Jedis;
import redis.clients.jedis.JedisPool;
import redis.clients.jedis.JedisPoolConfig;

public class JedisConnectionFactory {

    private  static final JedisPool jedisPool;

    static {
        JedisPoolConfig poolConfig = new JedisPoolConfig();
        poolConfig.setMaxTotal(8);
        poolConfig.setMaxIdle(8);
        poolConfig.setMinIdle(0);
        poolConfig.setMaxWaitMillis(1000);

        jedisPool = new JedisPool(poolConfig,
                "172.25.201.65", 6379, 1000, "123456");
    }
    public static Jedis getJedis(){
        return jedisPool.getResource();
    }

}
