package com.evan.redisdemospringdataredis;

import com.evan.redis.config.RedisConfig;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.annotation.Import;
import org.springframework.data.redis.core.RedisTemplate;


@SpringBootTest
@Import(RedisConfig.class)
class RedisDemoSpringDataRedisApplicationTests {

    @Autowired
    private RedisTemplate<String, Object> redisTemplate;

    @Test
    void contextLoads() {
        redisTemplate.opsForValue().set("name","noraBig");

        Object name = redisTemplate.opsForValue().get("name");
        System.out.println("name : " + name);
    }

}
