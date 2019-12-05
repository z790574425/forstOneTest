package com.fh.controller;

import com.fh.bean.PayLog;
import com.fh.login.LoginAnnotation;
import com.fh.rediskey.RedisKeyUtil;
import com.fh.service.IWXPayService;
import com.fh.utils.response.ResponseServer;
import com.fh.utils.response.ServerEnum;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;

@RestController
@RequestMapping("wxPay")
@CrossOrigin(maxAge = 3600,origins = "http://localhost:8080",exposedHeaders = "NOLONGIN")
public class WXPayController {

    @Autowired
    private IWXPayService iwxPayService;

    @GetMapping("paymentManage")
    @LoginAnnotation
    public ResponseServer paymentManage(String outTradeNo, HttpServletRequest request, String orderId){
        return iwxPayService.paymentManage(outTradeNo, request,orderId);
    }


}
