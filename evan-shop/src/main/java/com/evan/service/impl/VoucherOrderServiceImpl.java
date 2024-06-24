package com.evan.service.impl;

import com.evan.dto.Result;
import com.evan.entity.SeckillVoucher;
import com.evan.entity.VoucherOrder;
import com.evan.mapper.VoucherOrderMapper;
import com.evan.service.ISeckillVoucherService;
import com.evan.service.IVoucherOrderService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.evan.utils.RedisIdWorker;
import com.evan.utils.UserHolder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.time.LocalDateTime;

/**
 * <p>
 *  服務实现類
 * </p>
 *
 * @author Evan
 * @since 20240624
 */
@Service
public class VoucherOrderServiceImpl extends ServiceImpl<VoucherOrderMapper, VoucherOrder> implements IVoucherOrderService {
    
    @Resource
    private ISeckillVoucherService seckillVoucherService;
    
    @Resource
    private RedisIdWorker redisIdWorker;

    @Override
    @Transactional
    public Result seckillVoucher(Long voucherId) {
        SeckillVoucher voucher = seckillVoucherService.getById(voucherId);
        if (voucher.getBeginTime().isAfter(LocalDateTime.now()))
            return Result.fail("秒殺尚未開始");
        if (voucher.getEndTime().isBefore(LocalDateTime.now()))
            return Result.fail("秒殺結束");
        if (voucher.getStock() < 1)
            return Result.fail("庫存不足");

        boolean success = seckillVoucherService.update()
                .setSql("stock = stock - 1")
                .eq("voucher_id", voucher).update();
        if (!success)
            return Result.fail("庫存不足");

        VoucherOrder voucherOrder = new VoucherOrder();
        long orderId = redisIdWorker.nextId("order");
        voucherOrder.setId(orderId);

        Long userId = UserHolder.getUser().getId();
        voucherOrder.setUserId(userId);
        
        voucherOrder.setVoucherId(voucherId);
        save(voucherOrder);

        return Result.ok(orderId);
    }
}
