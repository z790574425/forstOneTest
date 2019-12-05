package com.fh.service;

import com.fh.utils.response.ResponseServer;

import javax.servlet.http.HttpServletRequest;

public interface IWXPayService {

    ResponseServer paymentManage(String outTradeNo, HttpServletRequest request, String orderId);
}
