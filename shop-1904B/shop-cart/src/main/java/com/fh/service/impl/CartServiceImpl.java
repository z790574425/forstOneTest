package com.fh.service.impl;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.fh.bean.CartBean;
import com.fh.httpclient.HttpConnection;
import com.fh.service.ICartService;
import com.fh.utils.UtilsTools;
import com.fh.utils.response.ResponseServer;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;

import javax.servlet.http.HttpServletRequest;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service("cartService")
public class CartServiceImpl implements ICartService {
    @Autowired
    private RedisTemplate redisTemplate;

    @Override
    public ResponseServer addCart(Integer productId, String userPhone) {
        //获取购物车id
        String cartId = (String) redisTemplate.opsForValue().get("cartid_" + userPhone);
        getProductById(productId,cartId);
        Long cartCount = redisTemplate.opsForHash().size(cartId);
        return ResponseServer.success(cartCount);
    }

    @Override
    public Map<String, Object> getCarts(String phone) {
        //获取购物车的ID
        String cartId = (String) redisTemplate.opsForValue().get("cartid_" + phone);
        //取出购物车的数据
        List<CartBean> cartList = redisTemplate.opsForHash().values(cartId);
        BigDecimal bigDecimal = BigDecimal.valueOf(0.00);
        for (CartBean cart : cartList) {
            if(cart.getIsChecked()){
                bigDecimal = bigDecimal.add(cart.getSubtotal());
            }
        }
        Map<String, Object> map = new HashMap<String, Object>();
        map.put("cartList", cartList);
        map.put("total", bigDecimal);
        return map;
    }

    @Override
    public void updateCheckStatus(Integer productId, String phone) {
        //获取购物车的ID
        String cartId = (String) redisTemplate.opsForValue().get("cartid_" + phone);
        CartBean cartBean = (CartBean) redisTemplate.opsForHash().get(cartId, productId);
        cartBean.setIsChecked(!cartBean.getIsChecked());
        redisTemplate.opsForHash().put(cartId, productId,cartBean);
    }

    @Override
    public void changeNum(Integer productId, String phone) {
        //获取购物车的ID
        String cartId = (String) redisTemplate.opsForValue().get("cartid_" + phone);
        CartBean cartBean = (CartBean) redisTemplate.opsForHash().get(cartId, productId);

        cartBean.setCount(cartBean.getCount()+1);
        cartBean.setSubtotal(cartBean.getPrice().multiply(new BigDecimal(cartBean.getCount())));
        redisTemplate.opsForHash().put(cartId, productId,cartBean);
    }

    @Override
    public Map<String, Object> getPayCarts(String phone) {
        //获取购物车的ID
        String cartId = (String) redisTemplate.opsForValue().get("cartid_" + phone);
        //取出购物车的数据
        List<CartBean> cartList = redisTemplate.opsForHash().values(cartId);
        BigDecimal bigDecimal = BigDecimal.valueOf(0.00);
        List<CartBean> resultList=new ArrayList<CartBean>();
        for (CartBean cart : cartList) {
            if(cart.getIsChecked()){
                cart=payProudctById(cart.getProductId(),cartId);
                if(cart.getIsStock()){
                    bigDecimal = bigDecimal.add(cart.getSubtotal());
                }
                resultList.add(cart);
            }
        }
        Map<String, Object> map = new HashMap<String, Object>();
        map.put("cartList", resultList);
        map.put("total", bigDecimal);
        return map;
    }


    public CartBean payProudctById(Integer productId,String cartId){
        //根据商品id查询商品信息
        String url = "http://localhost:9092/productSearch/" + productId;
        String result = HttpConnection.doGet(url);
        JSONObject jsonObject = JSON.parseObject(result);
        System.out.println(jsonObject.get("data"));
        JSONObject productData = JSON.parseObject(jsonObject.get("data").toString());

        //讲数据加入购物车实体bean中
        CartBean cartBean = new CartBean();
        cartBean.setProductId(productData.getInteger("id"));
        cartBean.setProductName(productData.getString("name"));
        cartBean.setMainImg(productData.getString("mainImg"));
        cartBean.setPrice(productData.getBigDecimal("price"));
        //判断商品是否存在购物车
        int stock=productData.getIntValue("stock");
        CartBean cart = (CartBean) redisTemplate.opsForHash().get(cartId, productId);
        cartBean.setCount(cart.getCount());
        if(stock<cart.getCount()){
            cartBean.setIsStock(false);
        }else{
            cartBean.setIsStock(true);
        }
        //计算小计金额
        BigDecimal bigDecimal = BigDecimal.valueOf(0.00);
        BigDecimal count = new BigDecimal(cart.getCount());
        BigDecimal subtotal = bigDecimal.add(cartBean.getPrice()).multiply(count);
        cartBean.setSubtotal(subtotal);
        cartBean.setIsChecked(true);
        //加入到redis
        redisTemplate.opsForHash().put(cartId, productId, cartBean);
        return cartBean;
    }
    public CartBean getProductById(Integer productId,String cartId){
        //根据商品id查询商品信息
        String url = "http://localhost:9092/productSearch/" + productId;
        String result = HttpConnection.doGet(url);
        JSONObject jsonObject = JSON.parseObject(result);
        System.out.println(jsonObject.get("data"));
        JSONObject productData = JSON.parseObject(jsonObject.get("data").toString());

        //讲数据加入购物车实体bean中
        CartBean cartBean = new CartBean();
        cartBean.setProductId(productData.getInteger("id"));
        cartBean.setProductName(productData.getString("name"));
        cartBean.setMainImg(productData.getString("mainImg"));
        cartBean.setPrice(productData.getBigDecimal("price"));
        //判断商品是否存在购物车
        if (redisTemplate.opsForHash().hasKey(cartId, productId)) {
            CartBean cart = (CartBean) redisTemplate.opsForHash().get(cartId, productId);
            cartBean.setCount(cart.getCount() + 1);
        } else {
            cartBean.setCount(1);
        }
        int stock=productData.getIntValue("stock");
        if(stock<cartBean.getCount()){
            cartBean.setIsStock(false);
        }else{
            cartBean.setIsStock(true);
        }
        //计算小计金额
        BigDecimal bigDecimal = BigDecimal.valueOf(0.00);
        BigDecimal count = new BigDecimal(cartBean.getCount());
        BigDecimal subtotal = bigDecimal.add(cartBean.getPrice()).multiply(count);
        cartBean.setSubtotal(subtotal);
        cartBean.setIsChecked(true);
        //加入到redis
        redisTemplate.opsForHash().put(cartId, productId, cartBean);
        return cartBean;
    }


    @Override
    public Map<String, Object> cartsCount(HttpServletRequest request) {
        Map<String, Object> map=new HashMap<>();
        String userPhone= (String) request.getAttribute("phone");
        String cartId = (String) redisTemplate.opsForValue().get("cartid_" + userPhone);
        Long size = redisTemplate.opsForHash().size(cartId);
        map.put("size",size);
        return map;
    }

    @Override
    public void isAll(HttpServletRequest request, String checkeIds) {
        String userPhone= (String) request.getAttribute("phone");
        String cartId = (String) redisTemplate.opsForValue().get("cartid_" + userPhone);
        List<CartBean> carList = redisTemplate.opsForHash().values(cartId);
        for (int i = 0;i<carList.size();i++){
            CartBean carBean = carList.get(i);
            if(checkeIds.equals("true")){
                carBean.setIsChecked(true);
            }else {
                carBean.setIsChecked(false);
            }
        }
    }



    @Override
    public void addCarts(String rune, Integer productId,Integer countNUmber) {
        HttpServletRequest request = UtilsTools.getRequest();
        String photo= (String) request.getAttribute("phone");
        String carstId = (String) redisTemplate.opsForValue().get("cartid_" + photo);
        CartBean cartBean = (CartBean) redisTemplate.opsForHash().get(carstId,productId);
        if(rune.equals("+")){
            cartBean.setCount(cartBean.getCount()+1);
        }else if(rune.equals("-")){
            cartBean.setCount(cartBean.getCount()-1);
        }else if(rune.equals("==")){
            cartBean.setCount(countNUmber);
        }
        BigDecimal bigDecimal=BigDecimal.valueOf(0.00);
        BigDecimal count=new BigDecimal(cartBean.getCount());
        BigDecimal subTotal=bigDecimal.add(cartBean.getPrice()).multiply(count);
        cartBean.setSubtotal(subTotal);
        redisTemplate.opsForHash().put(carstId,productId,cartBean);
    }

    @Override
    public void deleteCarts(Integer id) {
        HttpServletRequest request = UtilsTools.getRequest();
        String photo= (String) request.getAttribute("phone");
        String carstId = (String) redisTemplate.opsForValue().get("cartid_" + photo);
        redisTemplate.opsForHash().delete(carstId, id);
    }

}
