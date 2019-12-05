package com.fh.bean;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

import java.io.Serializable;
import java.util.Date;

@Data
@TableName("customer_login")
public class CustomerBean implements Serializable {

    @TableId(value = "customer_id",type = IdType.AUTO)
    private Integer customerId;
    private String loginName;
    private String  phone ;
    private Integer userStats;
    private Date  modifiedTime;
    private String password;
    private String cartId;

}
