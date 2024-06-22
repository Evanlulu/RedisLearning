package com.evan.redisdemospringdataredis;

import com.evan.redis.config.RedisConfig;
import com.evan.redis.pojo.User;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.annotation.Import;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.StringRedisTemplate;

import java.util.Map;


@SpringBootTest
@Import(RedisConfig.class)
class RedisStringTests {

    @Autowired
    private StringRedisTemplate redisTemplate;

    @Test
    void contextLoads() {
        redisTemplate.opsForValue().set("name","noraBig");

        Object name = redisTemplate.opsForValue().get("name");
        System.out.println("name : " + name);
    }

    private static final ObjectMapper mapper = new ObjectMapper();

    @Test
    void testSaveUser() throws JsonProcessingException {
        User user = new User("帆哥",21);

        String s = mapper.writeValueAsString(user);


        redisTemplate.opsForValue().set("user:100",s);

        String s1 = redisTemplate.opsForValue().get("user:100");

        User user1 = mapper.readValue(s1, User.class);
        System.out.println("user : "+ user1);
    }

    @Test
    void testHash(){
        redisTemplate.opsForHash().put("user:4000", "name", "虎辜");
        redisTemplate.opsForHash().put("user:4000", "age", "21");

        Map<Object, Object> entries = redisTemplate.opsForHash().entries("user:4000");
        System.out.println("entries : " + entries);

    }

}
