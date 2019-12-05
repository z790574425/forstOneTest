package com.fh.bean;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;

@Data
@TableName("t_paylog")
public class PayLog implements Serializable {

    @TableId(value = "outTradeNo",type=IdType.INPUT)
    private String outTradeNo;
    @TableField("orderId")
    private String orderId;
    @TableField("userId")
    private Long userId;
    @TableField("transactionId")
    private String transactionId;
    @TableField("createTime")
    private Date createTime;
    @TableField("payTime")
    private Date payTime;
    @TableField("payMoney")
    private BigDecimal payMoney;
    @TableField("payType")
    private Integer payType;
    @TableField("payStatus")
    private Integer payStatus;

}
