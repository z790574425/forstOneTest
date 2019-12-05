package com.fh.service.impl;

import com.fh.bean.PayLog;
import com.fh.common.CommonUtil;
import com.fh.mapper.IPayLogMapper;
import com.fh.mapper.IWXPay;
import com.fh.rediskey.RedisKeyUtil;
import com.fh.service.IWXPayService;
import com.fh.util.MyWxConfig;
import com.fh.utils.response.ResponseServer;
import com.fh.utils.response.ServerEnum;
import com.github.wxpay.sdk.WXPay;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;

import javax.servlet.http.HttpServletRequest;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

@Service("wxPayService")
public class IWXPayServiceImpl implements IWXPayService {

    @Autowired
    private  IWXPay iwxPay;

    @Autowired
    private RedisTemplate redisTemplate;

    @Autowired
    private IPayLogMapper payLogMapper;


    @Override
    public ResponseServer paymentManage(String outTradeNo, HttpServletRequest request, String orderId) {

        String phone= (String) request.getAttribute("phone");
        //查询对用要支付的订单
        PayLog payLog= (PayLog) redisTemplate.opsForHash().get(RedisKeyUtil.getWaitPayKey(phone),outTradeNo);
        if(payLog == null){
            return ResponseServer.error(ServerEnum.NO_ORDER_TO_PAY);
        }
        MyWxConfig myWxConfig=new MyWxConfig();
        int count=1;
        while (true){
            try {
                WXPay wxPay=new WXPay(myWxConfig);
                //存放参数的map集合
                Map<String,String>  data=new HashMap<String,String>();
                data.put("out_trade_no",outTradeNo);

                //存放验证
                Map<String,String> result=wxPay.orderQuery(data);
                String returnCode = result.get("return_code");
                String returnMsg = result.get("return_msg");
                if(!"SUCCESS".equalsIgnoreCase(returnCode)){
                    return ResponseServer.error(99999,returnMsg);
                }
                //验证业务是否成功
                String resultCode=result.get("result_code");
                String  errorCodeDes=result.get("err_code_des");
                if(!"SUCCESS".equalsIgnoreCase(resultCode)){
                    return ResponseServer.error(99999,errorCodeDes);
                }
                String tradeState=result.get("trade_state");
                if("SUCCESS".equalsIgnoreCase(tradeState)){
                    payLog.setPayStatus(CommonUtil.ORDER_STATUS_PAY_SUCCESS);
                    String transactionId=result.get("transaction_id");
                    payLog.setTransactionId(transactionId);
                    payLog.setPayTime(new Date());
                    payLog.setOutTradeNo(outTradeNo);
                    payLogMapper.updateById(payLog);
                    //删除订单信息
                    redisTemplate.opsForHash().delete(RedisKeyUtil.getWaitPayKey(phone),outTradeNo);
                    return ResponseServer.success(payLog.getPayMoney());
                }

                 count++;
                //暂停时间,防止死循环导致电脑崩溃
                Thread.sleep(1200L);
                //循环次数，1.2*50秒后就可以失效
                if(count>50){
                    //自定义返回值
                    return ResponseServer.error(ServerEnum.CRATER_PAY_EFFFFICACY);
                }
            }catch (Exception e){
                return ResponseServer.error(ServerEnum.NO_ORDER_TO_PAY);
            }
        }
    }

}
