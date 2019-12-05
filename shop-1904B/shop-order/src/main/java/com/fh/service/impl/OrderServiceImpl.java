package com.fh.service.impl;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.fh.bean.*;
import com.fh.common.CommonUtil;
import com.fh.httpclient.HttpConnection;
import com.fh.mapper.IOrderItemMapper;
import com.fh.mapper.IOrderMapper;
import com.fh.mapper.IPayLogMapper;
import com.fh.rediskey.RedisKeyUtil;
import com.fh.service.IOrderService;
import com.fh.util.IdUtil;
import com.fh.utils.response.ResponseServer;
import com.fh.utils.response.ServerEnum;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.*;
import java.util.stream.Collectors;

@Service("orderService")
public class OrderServiceImpl implements IOrderService {
    @Autowired
    private RedisTemplate redisTemplate;
    @Autowired
    private IOrderMapper orderMapper;
    @Autowired
    private IOrderItemMapper orderItemMapper;

    @Autowired
    private IPayLogMapper payLogMapper;
    @Override
    @Transactional
    public ResponseServer createOrder(Integer addressId, String phone) {
        String cartId= (String) redisTemplate.opsForValue().get(RedisKeyUtil.getCartIdKey(phone));
        List<CartBean> cartList=redisTemplate.opsForHash().values(cartId);
        //从购物车中获取被选中的商品
        List<CartBean> payCartList = cartList.stream().filter(cart -> cart.getIsChecked()).collect(Collectors.toList());
       //库存不足存放的list
        List<CartBean> understockList=new ArrayList<CartBean>();
        List<Integer>  havePayList=new ArrayList<Integer>();
        CustomerBean customer= (CustomerBean) redisTemplate.opsForValue().get("user_"+phone);
        //生成唯一的订单编号
        String orderId=IdUtil.getOrderId();
        BigDecimal totalPrice=new BigDecimal(0.00);
        BigDecimal totalCount=new BigDecimal(0);
        for(CartBean cart:payCartList){
            Integer productId=cart.getProductId();
            //调用商品的接口查询商品信息
            String url = "http://localhost:9092/productSearch/" + productId;
            String result = HttpConnection.doGet(url);
            JSONObject jsonObject = JSON.parseObject(result);
            JSONObject productData = JSON.parseObject(jsonObject.get("data").toString());
            int stock=productData.getIntValue("stock");
            if(cart.getCount()>stock){
                understockList.add(cart);
            }else{
               Integer successCount= orderMapper.updateProductStock(productId,cart.getCount());
               if(successCount == 0){
                   understockList.add(cart);
               }else{
                   totalPrice=totalPrice.add(cart.getSubtotal());
                   totalCount=totalCount.add(new BigDecimal(cart.getCount()));
                   OrderItem orderItem=new OrderItem();
                   orderItem.setCount(Long.valueOf(String.valueOf(cart.getCount())));
                   orderItem.setImage(cart.getMainImg());
                   orderItem.setProductName(cart.getProductName());
                   orderItem.setPrice(cart.getPrice());
                   orderItem.setSubTotalPrice(cart.getSubtotal());
                   orderItem.setProductId(Long.valueOf(String.valueOf(cart.getProductId())));
                   orderItem.setUserId(Long.valueOf(String.valueOf(customer.getCustomerId())));
                   orderItem.setOrderId(orderId);
                   orderItemMapper.insert(orderItem);
                   havePayList.add(cart.getProductId());

               }
            }
        }
        //有可能你买的商品库存都不足
        if(totalCount.longValue() == 0){
            return ResponseServer.error(ServerEnum.ALL_STOCK_NULL);
        }
        //生成订单
        Order order=new Order();
        order.setAddressId(Long.valueOf(String.valueOf(addressId)));
        order.setId(orderId);
        order.setCreateTime(new Date());
        order.setPayType(1);
        order.setStatus(CommonUtil.ORDER_STATUS_WAIT_PAY);
        order.setUserId(Long.valueOf(String.valueOf(customer.getCustomerId())));
        order.setTotalCount(totalCount.longValue());
        order.setTotalPrice(totalPrice);
        orderMapper.insert(order);
        //生成待支付记录
        PayLog payLog=new PayLog();
        payLog.setCreateTime(new Date());
        payLog.setOrderId(orderId);
        payLog.setOutTradeNo(IdUtil.getOrderId());
        payLog.setPayMoney(totalPrice);
        payLog.setPayStatus(CommonUtil.ORDER_STATUS_WAIT_PAY);
        payLog.setPayType(1);
        payLog.setUserId(Long.valueOf(String.valueOf(customer.getCustomerId())));
        payLogMapper.insert(payLog);
        //存放到redis中
        redisTemplate.opsForHash().put(RedisKeyUtil.getWaitPayKey(phone),payLog.getOutTradeNo(),payLog);
        //从购物车中删除已经生成订单的数据
        for(Integer productId:havePayList){
            redisTemplate.opsForHash().delete(cartId,productId);
        }

        Map<String,Object> map=new HashMap<String,Object>();
        map.put("outTradeNo",payLog.getOutTradeNo());
        map.put("orderId",orderId);
        map.put("understockList",understockList);


        return ResponseServer.success(map);
    }
}
