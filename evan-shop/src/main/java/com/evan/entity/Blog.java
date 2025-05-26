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
 * @since 20240624
 */
@Data
@EqualsAndHashCode(callSuper = false)
@Accessors(chain = true)
@TableName("tb_blog")
public class Blog implements Serializable {

    private static final long serialVersionUID = 1L;

    /**
     * pk
     */
    @TableId(value = "id", type = IdType.AUTO)
    private Long id;
    /**
     * 商戶id
     */
    private Long shopId;
    /**
     * 用戶id
     */
    private Long userId;
    /**
     * 用戶圖標
     */
    @TableField(exist = false)
    private String icon;
    /**
     * 用戶姓名
     */
    @TableField(exist = false)
    private String name;
    /**
     * 是否點讚過了
     */
    @TableField(exist = false)
    private Boolean isLike;

    /**
     * 標題
     */
    private String title;

    /**
     * 探店的照片，最多9張，多張以","隔开
     */
    private String images;

    /**
     * 探店的文字描述
     */
    private String content;

    /**
     * 點讚數量
     */
    private Integer liked;

    /**
     * 評論數量
     */
    private Integer comments;

    /**
     * 創建時間
     */
    private LocalDateTime createTime;

    /**
     * 更新時間
     */
    private LocalDateTime updateTime;


}
