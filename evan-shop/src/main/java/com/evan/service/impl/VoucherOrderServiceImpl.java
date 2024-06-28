package com.evan.service.impl;

import com.evan.dto.Result;
import com.evan.entity.SeckillVoucher;
import com.evan.entity.VoucherOrder;
import com.evan.mapper.VoucherOrderMapper;
import com.evan.service.ISeckillVoucherService;
import com.evan.service.IVoucherOrderService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.evan.utils.RedisIdWorker;
import com.evan.utils.SimpleRedisLock;
import com.evan.utils.UserHolder;
import org.redisson.api.RLock;
import org.redisson.api.RedissonClient;
import org.springframework.aop.framework.AopContext;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.StringRedisTemplate;
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
    @Autowired
    private StringRedisTemplate stringRedisTemplate;
    
    @Resource
    private RedissonClient redissonClient;

    @Override
    public Result seckillVoucher(Long voucherId) {
        SeckillVoucher voucher = seckillVoucherService.getById(voucherId);
        if (voucher.getBeginTime().isAfter(LocalDateTime.now()))
            return Result.fail("秒殺尚未開始");
        if (voucher.getEndTime().isBefore(LocalDateTime.now()))
            return Result.fail("秒殺結束");
        if (voucher.getStock() < 1)
            return Result.fail("庫存不足");
        Long userId = UserHolder.getUser().getId();

//        //鎖要等事務提交再釋放 但如果放在方法明旁邊 是鎖這整個物件
//        synchronized (userId.toString().intern()) { //常量值 不然 toString 會導致每次被鎖的 號碼都不一樣 沒有辦法侷限人
//            //獲取事務代理對象
//            IVoucherOrderService proxy = (IVoucherOrderService) AopContext.currentProxy();
//            return proxy.createVoucherOrder(voucherId);

//        SimpleRedisLock lock = new SimpleRedisLock("order:" + userId, stringRedisTemplate);
        RLock lock = redissonClient.getLock("lock:order:" + userId);
        boolean isLock = lock.tryLock();
        
        if (!isLock){
            //獲取失敗
            return Result.fail("一人只能下一單");
        }
        try {
            IVoucherOrderService proxy = (IVoucherOrderService) AopContext.currentProxy();
            return proxy.createVoucherOrder(voucherId);
        }finally {
            //釋放鎖
            lock.unlock();
        }
    }

    @Transactional//這個會有問題 因為上一個 方法調用 是非代理對象 所以事務有可能失效
    public  Result createVoucherOrder(Long voucherId) {
        Long userId = UserHolder.getUser().getId();
        int count = query().eq("user_id", userId).eq("voucher_id", voucherId).count();
            if (count > 0)
                return Result.fail("一個用戶一個優惠券");

            boolean success = seckillVoucherService.update()
                    .setSql("stock = stock - 1")
                    .eq("voucher_id", voucherId)
                    .gt("stock", 0) //利用庫存統一決高併發線程問題  但因為樂觀鎖增加失敗問題 庫存卻未售罄 解決方式 > 0
                    .update();
            if (!success)
                return Result.fail("庫存不足");

            VoucherOrder voucherOrder = new VoucherOrder();
            long orderId = redisIdWorker.nextId("order");
            voucherOrder.setId(orderId);
            voucherOrder.setUserId(userId);
            voucherOrder.setVoucherId(voucherId);
            save(voucherOrder);
       
        return Result.ok(orderId);
        
    }
}
