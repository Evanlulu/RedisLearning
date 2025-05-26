package com.evan.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.evan.dto.Result;
import com.evan.entity.Voucher;
import com.evan.mapper.VoucherMapper;
import com.evan.entity.SeckillVoucher;
import com.evan.service.ISeckillVoucherService;
import com.evan.service.IVoucherService;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.util.List;

import static com.evan.utils.RedisConstants.SECKILL_STOCK_KEY;

/**
 * <p>
 *  服務實現類
 * </p>
 *
 * @author Evan
 * @since 20240624
 */
@Service
public class VoucherServiceImpl extends ServiceImpl<VoucherMapper, Voucher> implements IVoucherService {

    @Resource
    private ISeckillVoucherService seckillVoucherService;
    @Resource(name = "stringRedisTemplate")
    private StringRedisTemplate redisTemplate;

    @Override
    public Result queryVoucherOfShop(Long shopId) {
        // 查询優惠券信息
        List<Voucher> vouchers = getBaseMapper().queryVoucherOfShop(shopId);
        // 返回结果
        return Result.ok(vouchers);
    }

    @Override
    @Transactional
    public void addSeckillVoucher(Voucher voucher) {
        // 保存優惠券
        save(voucher);
        // 保存秒殺信息
        SeckillVoucher seckillVoucher = new SeckillVoucher();
        seckillVoucher.setVoucherId(voucher.getId());
        seckillVoucher.setStock(voucher.getStock());
        seckillVoucher.setBeginTime(voucher.getBeginTime());
        seckillVoucher.setEndTime(voucher.getEndTime());
        seckillVoucherService.save(seckillVoucher);
        //保存 到 redis中
        redisTemplate.opsForValue().set(SECKILL_STOCK_KEY + voucher.getId(),voucher.getStock().toString());
    }
}
