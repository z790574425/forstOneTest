package com.fh.service;

import com.fh.bean.CartBean;
import com.fh.utils.response.ResponseServer;

import javax.servlet.http.HttpServletRequest;
import java.util.List;
import java.util.Map;

public interface ICartService {

    ResponseServer addCart(Integer productId,String userPhone);

    Map<String,Object> getCarts(String phone);

    void updateCheckStatus(Integer productId, String phone);

    void changeNum(Integer productId, String phone);

    Map<String,Object> getPayCarts(String phone);

    Map<String,Object> cartsCount(HttpServletRequest request);

    void isAll(HttpServletRequest request,String checkeIds);

    void addCarts(String rune, Integer productId,Integer countNUmber);

    void deleteCarts(Integer id);



}
