package com.fh.bean;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

import java.io.Serializable;
import java.util.Date;

@Data
public class CustomerBean implements Serializable {

    private Integer customerId;
    private String loginName;
    private String  phone ;
    private Integer userStats;
    private Date  modifiedTime;
    private String password;
    private String cartId;

}
