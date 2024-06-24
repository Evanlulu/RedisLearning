package com.evan.controller;


import cn.hutool.core.util.StrUtil;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.evan.dto.Result;
import com.evan.entity.Shop;
import com.evan.service.IShopService;
import com.evan.utils.SystemConstants;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;

/**
 * <p>
 * 前端控制器
 * </p>
 *
 * @author Evan
 * @since 20240624
 */
@RestController
@RequestMapping("/shop")
public class ShopController {

    @Resource
    public IShopService shopService;

    /**
     * id查商店
     * @param id 商id
     * @return 商店詳細
     */
    @GetMapping("/{id}")
    public Result queryShopById(@PathVariable("id") Long id) {
        return shopService.queryById(id);
    }

    /**
     * 新增商店
     * @param shop 商店
     * @return 商店id
     */
    @PostMapping
    public Result saveShop(@RequestBody Shop shop) {
        //
        shopService.save(shop);
        //
        return Result.ok(shop.getId());
    }

    /**
     * 更新
     * @param shop
     * @return void
     */
    @PutMapping
    public Result updateShop(@RequestBody Shop shop) {

        return shopService.update(shop);
    }

    /**
     *
     * @param typeId 商
     * @param current 頁碼
     * @return 商店列表
     */
    @GetMapping("/of/type")
    public Result queryShopByType(
            @RequestParam("typeId") Integer typeId,
            @RequestParam(value = "current", defaultValue = "1") Integer current
    ) {

        Page<Shop> page = shopService.query()
                .eq("type_id", typeId)
                .page(new Page<>(current, SystemConstants.DEFAULT_PAGE_SIZE));
        return Result.ok(page.getRecords());
    }

    /**
     * @param name 商店名稱
     * @param current 頁碼
     * @return 商店列表
     */
    @GetMapping("/of/name")
    public Result queryShopByName(
            @RequestParam(value = "name", required = false) String name,
            @RequestParam(value = "current", defaultValue = "1") Integer current
    ) {
        Page<Shop> page = shopService.query()
                .like(StrUtil.isNotBlank(name), "name", name)
                .page(new Page<>(current, SystemConstants.MAX_PAGE_SIZE));
        return Result.ok(page.getRecords());
    }
}
