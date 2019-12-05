package com.fh.bean;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

import java.util.Date;

@Data
@TableName("t_shop_address")
public class AddressBean {
    @TableId(value = "id",type = IdType.AUTO)
    private Integer id;
    @TableField("consignee")
    private String consignee;
    @TableField("address")
    private String address;
    @TableField("phone")
    private String phone;
    @TableField("email")
    private String email;
    @TableField("alias")
    private String alias;
    @TableField("creatorId")
    private Integer creatorId;
    @TableField("createTime")
    private Date createTime;
}
