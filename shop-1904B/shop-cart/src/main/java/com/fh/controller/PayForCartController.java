package com.fh.controller;

import com.fh.login.LoginAnnotation;
import com.fh.service.ICartService;
import com.fh.utils.response.ResponseServer;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;
import java.util.Map;

@RestController
@RequestMapping("/payCart")
@CrossOrigin(maxAge = 3600, origins = "http://localhost:8080", exposedHeaders = "NOLONGIN")
public class PayForCartController {
    @Autowired
    private ICartService cartService;
    @GetMapping
    @LoginAnnotation
    public ResponseServer queryPayList(HttpServletRequest request){
        String  phone= (String) request.getAttribute("phone");
        Map<String,Object> result=cartService.getPayCarts(phone);
        return ResponseServer.success(result);
    }
}
