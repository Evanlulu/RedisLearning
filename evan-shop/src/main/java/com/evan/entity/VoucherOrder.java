package com.evan.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

import java.io.Serializable;
import java.time.LocalDateTime;

/**
 * <p>
 * 
 * </p>
 *
 * @author Evan
 * @since 2021-12-22
 */
@Data
@EqualsAndHashCode(callSuper = false)
@Accessors(chain = true)
@TableName("tb_voucher_order")
public class VoucherOrder implements Serializable {

    private static final long serialVersionUID = 1L;

    /**
     * pk
     */
    @TableId(value = "id", type = IdType.INPUT)
    private Long id;

    /**
     * 下单的用户id
     */
    private Long userId;

    /**
     * 购买的代金券id
     */
    private Long voucherId;

    /**
     * 支付方式 1：余额支付；2：支付宝；3：微信
     */
    private Integer payType;

    /**
     * 订单状态，1：未支付；2：已支付；3：已核销；4：已取消；5：退款中；6：已退款
     */
    private Integer status;

    /**
     * 下单時間
     */
    private LocalDateTime createTime;

    /**
     * 支付時間
     */
    private LocalDateTime payTime;

    /**
     * 核销時間
     */
    private LocalDateTime useTime;

    /**
     * 退款時間
     */
    private LocalDateTime refundTime;

    /**
     * 更新時間
     */
    private LocalDateTime updateTime;


}
