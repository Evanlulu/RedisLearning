package com.evan;

import com.evan.service.impl.ShopServiceImpl;
import com.evan.utils.CacheClient;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

import javax.annotation.Resource;

@SpringBootTest
class EvanApplicationTests {

    @Resource
    private ShopServiceImpl shopService;
    @Resource
    private CacheClient cacheClient;

    @Test
    void testSaveShop(){
//        shopService.saveShop2Redis(1L, 10L);
    }

}
