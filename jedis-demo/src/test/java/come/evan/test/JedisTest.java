package come.evan.test;

import com.evan.jedis.util.JedisConnectionFactory;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import redis.clients.jedis.Jedis;

import java.util.Map;

public class JedisTest {
    private Jedis jedis;

    @BeforeEach
    void setUp() {
        //建立連結
//        jedis = new Jedis("172.25.201.65", 6379);
        jedis = JedisConnectionFactory.getJedis();
        jedis.auth("123456");
        jedis.select(0);
    }

    @Test
    void testString() {
        String result = jedis.set("name", "Evan");
        System.out.println("result = " + result);

        String name = jedis.get("name");
        System.out.println("name = " + name);
    }

    @Test
    void testHash() {
        jedis.hset("user:1", "name", "Jack");
        jedis.hset("user:1", "age", "21");

        Map<String, String> map = jedis.hgetAll("user:1");
        System.out.println(map);

    }


    @AfterEach
    void tearDown() {
        if (jedis != null) {
            jedis.close();
        }
    }
}
