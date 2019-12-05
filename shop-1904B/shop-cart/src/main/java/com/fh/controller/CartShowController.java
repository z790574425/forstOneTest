package com.fh.controller;

import com.fh.bean.CartBean;
import com.fh.login.LoginAnnotation;
import com.fh.service.ICartService;
import com.fh.utils.response.ResponseServer;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import java.util.Map;

@RestController
@RequestMapping("/cartsShow")
@CrossOrigin(maxAge = 3600, origins = "http://localhost:8080", exposedHeaders = "NOLONGIN")
public class CartShowController {

    @Autowired
    private ICartService cartService;


    @LoginAnnotation
    @GetMapping
    public ResponseServer cartShow(HttpServletRequest request){
      String  phone= (String) request.getAttribute("phone");
       Map<String,Object> result=cartService.getCarts(phone);
        return ResponseServer.success(result);
    }


    @LoginAnnotation
    @PostMapping("/")
    public ResponseServer changChecked(Integer productId,HttpServletRequest request){
        String  phone= (String) request.getAttribute("phone");
        cartService.updateCheckStatus(productId,phone);
        return ResponseServer.success();
    }

    @LoginAnnotation
    @PostMapping("/updataNum")
    public ResponseServer changeNum(Integer productId,HttpServletRequest request){
        String  phone= (String) request.getAttribute("phone");
        cartService.changeNum(productId,phone);
        return ResponseServer.success();
    }

    @LoginAnnotation
    @PostMapping("/cartsCount")
    public ResponseServer cartsCount(HttpServletRequest request){
        Map<String, Object> stringObjectMap = cartService.cartsCount(request);
        return ResponseServer.success(stringObjectMap);
    }


    @LoginAnnotation
    @PostMapping("/isAll")
    public ResponseServer isAll(HttpServletRequest request,String checkeIds){
        cartService.isAll(request,checkeIds);
        return ResponseServer.success();
    }

    @PostMapping("/addCarts")
    @LoginAnnotation
    public ResponseServer addCarts(String rune, Integer productId,Integer countNUmber) {
        cartService.addCarts(rune,productId,countNUmber);
        return ResponseServer.success();
    }

    @PostMapping("/deleteCarts")
    @LoginAnnotation
    public ResponseServer deleteCarts(Integer id) {
        cartService.deleteCarts(id);
        return ResponseServer.success();
    }

}
