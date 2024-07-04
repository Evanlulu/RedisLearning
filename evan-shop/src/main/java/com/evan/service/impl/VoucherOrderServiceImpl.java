package com.evan.service.impl;

import com.evan.dto.Result;
import com.evan.entity.VoucherOrder;
import com.evan.mapper.VoucherOrderMapper;
import com.evan.service.ISeckillVoucherService;
import com.evan.service.IVoucherOrderService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.evan.utils.RedisIdWorker;
import com.evan.utils.UserHolder;
import lombok.extern.slf4j.Slf4j;
import org.redisson.api.RLock;
import org.redisson.api.RedissonClient;
import org.springframework.aop.framework.AopContext;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.ClassPathResource;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.data.redis.core.script.DefaultRedisScript;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.PostConstruct;
import javax.annotation.Resource;
import java.util.Collections;
import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

/**
 * <p>
 *  服務實現類
 * </p>
 *
 * @author Evan
 * @since 20240624
 */
@Service
@Slf4j
public class VoucherOrderServiceImpl extends ServiceImpl<VoucherOrderMapper, VoucherOrder> implements IVoucherOrderService {
    
    @Resource
    private ISeckillVoucherService seckillVoucherService;
    
    @Resource
    private RedisIdWorker redisIdWorker;
    @Autowired
    private StringRedisTemplate stringRedisTemplate;
    
    @Resource
    private RedissonClient redissonClient;
    
    private static final DefaultRedisScript<Long> SECKILL_SCRIPT;
    private IVoucherOrderService proxy;


    static {
        SECKILL_SCRIPT = new DefaultRedisScript<>();
        SECKILL_SCRIPT.setLocation(new ClassPathResource("lua/seckill.lua"));
        SECKILL_SCRIPT.setResultType(Long.class);
    }
    private BlockingQueue<VoucherOrder> orderTasks = new ArrayBlockingQueue<>(1024*1024);
    private static final  ExecutorService SECKILL_ORDER_EXECUTOR = Executors.newSingleThreadExecutor();
    
    @PostConstruct //初始化完執行
    private void init(){
        SECKILL_ORDER_EXECUTOR.submit(new VoucherOrderHandler());
    }
    
    private class VoucherOrderHandler implements Runnable {

        @Override
        public void run() {
            while (true) {
                //獲取對列中的訂單信息
                try {
                    VoucherOrder voucherOrder = orderTasks.take();
                    handleVoucherOrder(voucherOrder);
                } catch (InterruptedException e) {
                    log.error("處理訂單異常", e);
                }
            }
        }
    }

    private void handleVoucherOrder(VoucherOrder voucherOrder) {
        Long userId = voucherOrder.getUserId();
        RLock lock = redissonClient.getLock("lock:order:" + userId);
        boolean isLock = lock.tryLock();

        if (!isLock){
            //獲取失敗
            log.error("不允許重複下單");
            return ;
        }
        try {
           proxy.createVoucherOrder(voucherOrder);
        }finally {
            //釋放鎖
            lock.unlock();
        }
    }

    @Override
    public Result seckillVoucher(Long voucherId) {
        Long userId = UserHolder.getUser().getId();
        //Lua
        Long result = stringRedisTemplate.execute(
                SECKILL_SCRIPT,
                Collections.emptyList(),
                voucherId.toString(), userId.toString()
        );
        //是否為0
        int r = result.intValue();
        if (r != 0 )
            return Result.fail( r==1 ? "沒庫存" : "不能重複下單");
        //為0可以購買 把下單放到阻塞對列
        VoucherOrder voucherOrder = new VoucherOrder();
        long orderId = redisIdWorker.nextId("order");
        voucherOrder.setId(orderId);
        voucherOrder.setUserId(userId);
        voucherOrder.setVoucherId(voucherId);
        //阻塞對列
        orderTasks.add(voucherOrder);
         proxy = (IVoucherOrderService) AopContext.currentProxy();//注意子限程會拿不到主線程的代理對象
        //返回訂單
        return  Result.ok(orderId);
    }

//    @Override
//    public Result seckillVoucher(Long voucherId) {
//        SeckillVoucher voucher = seckillVoucherService.getById(voucherId);
//        if (voucher.getBeginTime().isAfter(LocalDateTime.now()))
//            return Result.fail("秒殺尚未開始");
//        if (voucher.getEndTime().isBefore(LocalDateTime.now()))
//            return Result.fail("秒殺結束");
//        if (voucher.getStock() < 1)
//            return Result.fail("庫存不足");
//        Long userId = UserHolder.getUser().getId();
//
////        //鎖要等事務提交再釋放 但如果放在方法明旁邊 是鎖這整個物件
////        synchronized (userId.toString().intern()) { //常量值 不然 toString 會導致每次被鎖的 號碼都不一樣 沒有辦法侷限人
////            //獲取事務代理對象
////            IVoucherOrderService proxy = (IVoucherOrderService) AopContext.currentProxy();
////            return proxy.createVoucherOrder(voucherId);
//
////        SimpleRedisLock lock = new SimpleRedisLock("order:" + userId, stringRedisTemplate);
//        RLock lock = redissonClient.getLock("lock:order:" + userId);
//        boolean isLock = lock.tryLock();
//        
//        if (!isLock){
//            //獲取失敗
//            return Result.fail("一人只能下一單");
//        }
//        try {
//            IVoucherOrderService proxy = (IVoucherOrderService) AopContext.currentProxy();
//            return proxy.createVoucherOrder(voucherId);
//        }finally {
//            //釋放鎖
//            lock.unlock();
//        }
//    }

    @Transactional//這個會有問題 因為上一個 方法調用 是非代理對象 所以事務有可能失效
    public void createVoucherOrder(VoucherOrder voucherOrder) {
        Long userId = voucherOrder.getUserId();
        int count = query().eq("user_id", userId).eq("voucher_id", voucherOrder.getVoucherId()).count();
            if (count > 0){
                log.error("不許重複下單");
                return;
            }

            boolean success = seckillVoucherService.update()
                    .setSql("stock = stock - 1")
                    .eq("voucher_id", voucherOrder.getVoucherId())
                    .gt("stock", 0) //利用庫存統一決高併發線程問題  但因為樂觀鎖增加失敗問題 庫存卻未售罄 解決方式 > 0
                    .update();
            if (!success){
                log.error("庫存不足");
                return;
            }
            
            save(voucherOrder);
            
        
    }
}
