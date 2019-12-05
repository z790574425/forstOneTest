package com.fh.controller;

import com.fh.login.LoginAnnotation;
import com.fh.service.ICartService;
import com.fh.utils.response.ResponseServer;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;

@RestController
@RequestMapping("/carts")
@CrossOrigin(maxAge = 3600, origins = "http://localhost:8080", exposedHeaders = "NOLONGIN")
public class CartController {

    @Autowired
    private ICartService cartService;

    @PostMapping
    @LoginAnnotation
    public ResponseServer addCart(Integer productId, HttpServletRequest request) {
        String userPhone = (String) request.getAttribute("phone");

        return cartService.addCart(productId, userPhone);
    }

    @LoginAnnotation
    @GetMapping
    public ResponseServer toCarts() {
        return ResponseServer.success();
    }
}
