package com.fh.controller;

import com.fh.bean.AddressBean;
import com.fh.login.LoginAnnotation;
import com.fh.service.IAddressService;
import com.fh.utils.response.ResponseServer;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;

@RestController
@RequestMapping("address")
@CrossOrigin(maxAge = 3600,origins = "http://localhost:8080",exposedHeaders = "NOLONGIN")
public class AddressController {
    @Autowired
    private IAddressService addressService;

    /**
     * 查询配送地址列表
     * @param request
     * @return
     */
    @GetMapping
    @LoginAnnotation
    public ResponseServer queryAddressList(HttpServletRequest request){
        //获取手机号
        String userPhone = (String) request.getAttribute("phone");
        return addressService.queryAddressList(userPhone);
    }

    @PutMapping
    @LoginAnnotation
    public ResponseServer saveAddress(HttpServletRequest request, AddressBean address){
        //获取手机号
        String userPhone = (String) request.getAttribute("phone");
        return addressService.saveAddress(userPhone,address);
    }

    @GetMapping("/{addressId}")
    @LoginAnnotation
    public ResponseServer getAddressById(@PathVariable Integer addressId){
        return addressService.getAddressById(addressId);
    }

}
