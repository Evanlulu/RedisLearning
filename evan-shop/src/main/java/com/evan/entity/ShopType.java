package com.evan.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.fasterxml.jackson.annotation.JsonIgnore;
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
@TableName("tb_shop_type")
public class ShopType implements Serializable {

    private static final long serialVersionUID = 1L;

    /**
     * pk
     */
    @TableId(value = "id", type = IdType.AUTO)
    private Long id;

    /**
     * 類型名称
     */
    private String name;

    /**
     * 图标
     */
    private String icon;

    /**
     * 顺序
     */
    private Integer sort;

    /**
     * 創建時間
     */
    @JsonIgnore
    private LocalDateTime createTime;

    /**
     * 更新時間
     */
    @JsonIgnore
    private LocalDateTime updateTime;


}
