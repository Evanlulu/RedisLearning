package com.evan.service.impl;

import cn.hutool.core.util.StrUtil;
import cn.hutool.json.JSONUtil;
import com.evan.dto.Result;
import com.evan.entity.Shop;
import com.evan.mapper.ShopMapper;
import com.evan.service.IShopService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;

import java.util.concurrent.TimeUnit;

import static com.evan.utils.RedisConstants.CACHE_SHOP_KEY;
import static com.evan.utils.RedisConstants.CACHE_SHOP_TTL;

/**
 * <p>
 *  服務實現類
 * </p>
 *
 * @author Evan
 * @since 20240622
 */
@Service
public class ShopServiceImpl extends ServiceImpl<ShopMapper, Shop> implements IShopService {

    @Resource
    private StringRedisTemplate stringRedisTemplate;
    @Override
    public Result queryById(Long id) {
        String key = CACHE_SHOP_KEY + id;
        String shopJson = stringRedisTemplate.opsForValue().get(key);

        if (StrUtil.isNotBlank(shopJson)){
            Shop shop = JSONUtil.toBean(shopJson, Shop.class);
            return Result.ok(shop);
        }
        //判斷命中是否為空值
        if (shopJson != null) {
            return Result.fail("商店不存在!");
        }


        Shop shop = getById(id);
        if (shop == null){
            // 防穿透
            stringRedisTemplate.opsForValue().set(key, "",CACHE_SHOP_TTL, TimeUnit.MINUTES);
            return Result.fail("商店不存在");
        }
        stringRedisTemplate.opsForValue().set(key, JSONUtil.toJsonStr(shop), 30L, TimeUnit.MINUTES);

        return Result.ok(shop);
    }

    @Override
    public Result update(Shop shop) {
        Long id = shop.getId();
        if (id == null) {
            return Result.fail("id 不可以為空");
        }
        updateById(shop);

        stringRedisTemplate.delete(CACHE_SHOP_KEY + id);
        return Result.ok();
    }
}
