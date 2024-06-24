package com.evan.service;

import com.evan.dto.Result;
import com.evan.entity.Voucher;
import com.baomidou.mybatisplus.extension.service.IService;

/**
 * <p>
 *  服務類
 * </p>
 *
 * @author Evan
 * @since 20240624
 */
public interface IVoucherService extends IService<Voucher> {

    Result queryVoucherOfShop(Long shopId);

    void addSeckillVoucher(Voucher voucher);
}
