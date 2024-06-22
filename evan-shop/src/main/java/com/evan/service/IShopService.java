package com.evan.service;

import com.evan.dto.Result;
import com.evan.entity.Shop;
import com.baomidou.mybatisplus.extension.service.IService;

/**
 * <p>
 *  服務
 * </p>
 *
 * @author Evan
 * @since 20240622
 */
public interface IShopService extends IService<Shop> {

    Result queryById(Long id);

    Result update(Shop shop);
}
