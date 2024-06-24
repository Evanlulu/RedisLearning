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
 * @since 20240624
 */
@Data
@EqualsAndHashCode(callSuper = false)
@Accessors(chain = true)
@TableName("tb_user")
public class User implements Serializable {

    private static final long serialVersionUID = 1L;

  
    @TableId(value = "id", type = IdType.AUTO)
    private Long id;
    
    private String phone;
    
    private String password;
    
    private String nickName;
    
    private String icon = "";
    
    private LocalDateTime createTime;

    private LocalDateTime updateTime;


}
