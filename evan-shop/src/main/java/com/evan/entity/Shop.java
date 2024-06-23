package com.evan.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
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
 * @since 20240623
 */
@Data
@EqualsAndHashCode(callSuper = false)
@Accessors(chain = true)
@TableName("tb_shop")
public class Shop implements Serializable {

    private static final long serialVersionUID = 1L;

    /**
     * 主鍵
     */
    @TableId(value = "id", type = IdType.AUTO)
    private Long id;

    /**
     * 商店名
     */
    private String name;

    /**
     * 類型ID
     */
    private Long typeId;

    /**
     * 圖片
     */
    private String images;

    /**
     * 商圈
     */
    private String area;

    /**
     * 地址
     */
    private String address;

    /**
     * 輕度
     */
    private Double x;

    /**
     * 维度
     */
    private Double y;

    /**
     * 均價
     */
    private Long avgPrice;

    /**
     * 銷量
     */
    private Integer sold;

    /**
     * 評論數
     */
    private Integer comments;

    /**
     * 評分
     */
    private Integer score;

    /**
     * 營業時間
     */
    private String openHours;

    /**
     * 創建時間
     */
    private LocalDateTime createTime;

    /**
     * 更新時間
     */
    private LocalDateTime updateTime;


    @TableField(exist = false)
    private Double distance;
}
