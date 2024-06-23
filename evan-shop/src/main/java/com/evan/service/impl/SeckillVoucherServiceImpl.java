package com.evan.service.impl;

import com.evan.entity.SeckillVoucher;
import com.evan.mapper.SeckillVoucherMapper;
import com.evan.service.ISeckillVoucherService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.stereotype.Service;

/**
 * <p>
 * 秒殺優惠券表，与優惠券是一对一关系 服务实现類
 * </p>
 *
 * @author Evan
 * @since 2022-01-04
 */
@Service
public class SeckillVoucherServiceImpl extends ServiceImpl<SeckillVoucherMapper, SeckillVoucher> implements ISeckillVoucherService {

}
