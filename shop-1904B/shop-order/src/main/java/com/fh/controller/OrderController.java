package com.fh.controller;

import com.fh.login.LoginAnnotation;
import com.fh.service.IOrderService;
import com.fh.utils.response.ResponseServer;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;

@RestController
@RequestMapping("orders")
@CrossOrigin(maxAge = 3600,origins = "http://localhost:8080",exposedHeaders = "NOLONGIN")
public class OrderController {
    @Autowired
    private IOrderService orderService;

    /**
     * 生成订单
     * @param request
     * @param addressId
     * @return
     */
    @PutMapping
    @LoginAnnotation
    public ResponseServer createOrder(HttpServletRequest request,Integer addressId){
            String phone= (String) request.getAttribute("phone");
            return orderService.createOrder(addressId,phone);
    }

}
