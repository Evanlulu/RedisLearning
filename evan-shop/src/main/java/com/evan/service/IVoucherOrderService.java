package com.evan.service;

import com.evan.dto.Result;
import com.evan.entity.VoucherOrder;
import com.baomidou.mybatisplus.extension.service.IService;

import javax.annotation.Resource;

/**
 * <p>
 *  服務類
 * </p>
 *
 * @author Evan
 * @since 20240624
 */
public interface IVoucherOrderService extends IService<VoucherOrder> {
    
    Result seckillVoucher(Long voucherId);

    Result createVoucherOrder(Long voucherId);
}
