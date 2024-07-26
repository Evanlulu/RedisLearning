package com.evan.service.impl;


import cn.hutool.core.util.StrUtil;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.evan.dto.Result;
import com.evan.entity.Shop;
import com.evan.mapper.ShopMapper;
import com.evan.service.IShopService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.evan.utils.CacheClient;

import com.evan.utils.SystemConstants;
import org.springframework.data.geo.Distance;
import org.springframework.data.geo.GeoResult;
import org.springframework.data.geo.GeoResults;
import org.springframework.data.redis.connection.RedisGeoCommands;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.data.redis.domain.geo.GeoReference;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.concurrent.TimeUnit;

import static com.evan.utils.RedisConstants.*;

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
    private CacheClient cacheClient;
    @Resource
    private StringRedisTemplate stringRedisTemplate;
    @Override
    public Result queryById(Long id) {
       //緩存穿透
//        Shop shop = cacheClient
//                .queryWithPassThrough(CACHE_SHOP_KEY, id, Shop.class, this::getById, CACHE_SHOP_TTL, TimeUnit.MINUTES);

//        互斥鎖
//        Shop shop = queryWithMutex(id);

//      邏輯過期
        Shop shop = cacheClient
                .queryWithLogicalExpire(CACHE_SHOP_KEY, id, Shop.class, this::getById, CACHE_SHOP_TTL, TimeUnit.MINUTES);
        if (shop == null) {
            return Result.fail("商店不存在");
        }

        return Result.ok(shop);
    }

//    //互斥鎖避免擊穿
//    public Shop queryWithMutex(Long id) {
//        String key = CACHE_SHOP_KEY + id;
//        String shopJson = stringRedisTemplate.opsForValue().get(key);
//
//        if (StrUtil.isNotBlank(shopJson)){
//            return JSONUtil.toBean(shopJson, Shop.class);
//        }
//        //判斷命中是否為空值
//        if (shopJson != null) {
//            return null;
//        }
//
//        String lockKey = LOCK_SHOP_KEY + id;
//        boolean isLock = tryLock(lockKey);
//
//        Shop shop = null;
//        try {
//            if (!isLock){
//                Thread.sleep(50);
//                queryWithMutex(id);
//            }
//
//            shop = getById(id);
//            if (shop == null){
//                // 防穿透
//                stringRedisTemplate.opsForValue().set(key, "",CACHE_SHOP_TTL, TimeUnit.MINUTES);
//                return null;
//            }
//            stringRedisTemplate.opsForValue().set(key, JSONUtil.toJsonStr(shop), 30L, TimeUnit.MINUTES);
//        } catch (InterruptedException e) {
//            throw new RuntimeException(e);
//        }
//        finally {
//            unlock(key);
//        }
//        return shop;
//    }




//    public Shop queryWithPassThrough(Long id) {
//        String key = CACHE_SHOP_KEY + id;
//        String shopJson = stringRedisTemplate.opsForValue().get(key);
//
//        if (StrUtil.isNotBlank(shopJson)){
//            return JSONUtil.toBean(shopJson, Shop.class);
//        }
//        //判斷命中是否為空值
//        if (shopJson != null) {
//            return null;
//        }
//
//
//        Shop shop = getById(id);
//        if (shop == null){
//            // 防穿透
//            stringRedisTemplate.opsForValue().set(key, "",CACHE_SHOP_TTL, TimeUnit.MINUTES);
//            return null;
//        }
//        stringRedisTemplate.opsForValue().set(key, JSONUtil.toJsonStr(shop), 30L, TimeUnit.MINUTES);
//
//        return shop;
//    }



//    public void saveShop2Redis(Long id, Long expireSeconds){
//        Shop shop = getById(id);
//
//        RedisData redisData = new RedisData();
//        redisData.setData(shop);
//        redisData.setExpireTime(LocalDateTime.now().plusSeconds(expireSeconds));
//
//        stringRedisTemplate.opsForValue().set(CACHE_SHOP_KEY + id, JSONUtil.toJsonStr(redisData));
//    }

    @Override
    @Transactional
    public Result update(Shop shop) {
        Long id = shop.getId();
        if (id == null) {
            return Result.fail("id 不可以為空");
        }
        updateById(shop);

        stringRedisTemplate.delete(CACHE_SHOP_KEY + id);
        return Result.ok();
    }

    @Override
    public Result queryShopByType(Integer typeId, Integer current, Double x, Double y) {
        //是否需要根據 座標
        if (x == null || y == null){
            Page<Shop> page = query()
                    .eq("type_id", typeId)
                    .page(new Page<>(current, SystemConstants.DEFAULT_PAGE_SIZE));
            return Result.ok(page.getRecords());
        }
        //計算分業參數
        int from = (current - 1) * SystemConstants.DEFAULT_PAGE_SIZE;
        int end = current * SystemConstants.DEFAULT_PAGE_SIZE;
        // redis 排序
        String key = SHOP_GEO_KEY + typeId;
        GeoResults<RedisGeoCommands.GeoLocation<String>> results = stringRedisTemplate.opsForGeo()
                .search(
                        key,
                        GeoReference.fromCoordinate(x, y),
                        new Distance(5000),
                        RedisGeoCommands.GeoSearchCommandArgs.newGeoSearchArgs().includeDistance().limit(end)
                );
        if (results == null)
            return  Result.ok(Collections.emptyList());

        List<GeoResult<RedisGeoCommands.GeoLocation<String>>> list = results.getContent();
        if (list.size() <= from)
            return  Result.ok(Collections.emptyList());
        
        ArrayList<Long> ids = new ArrayList<>(list.size());
        HashMap<String, Distance> distanceMap = new HashMap<>(list.size());
        list.stream().skip(from).forEach(result ->{
            String shopIdStr = result.getContent().getName();
            ids.add(Long.valueOf(shopIdStr));
            Distance distance = result.getDistance();
            distanceMap.put(shopIdStr, distance);
        });
        String idStr = StrUtil.join(",", ids);
        List<Shop> shops = query().in("id", ids).last("ORDER BY FIELD(id," + idStr + ")").list();

        for (Shop shop : shops) {
            shop.setDistance(distanceMap.get(shop.getId().toString()).getValue());
            
        }
        //解析出id查詢
        return Result.ok(shops);
    }
}
