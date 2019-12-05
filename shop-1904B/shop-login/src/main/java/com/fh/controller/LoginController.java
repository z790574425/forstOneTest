package com.fh.controller;

import com.alibaba.fastjson.JSONObject;
import com.fh.bean.CustomerBean;
import com.fh.jwt.JwtUtils;
import com.fh.message.SendMessage;
import com.fh.service.ICustomerService;
import com.fh.utils.response.ResponseServer;
import com.fh.utils.response.ServerEnum;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.TimeoutUtils;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.TimeUnit;

@RestController
@RequestMapping("/logins")
@CrossOrigin(maxAge = 3600, origins = "http://localhost:8080")
public class LoginController {

    @Autowired
    private RedisTemplate redisTemplate;

    @Autowired
    private ICustomerService customerService;

    @GetMapping("/{phone}")
    public ResponseServer sendCode(@PathVariable String phone) throws IOException {
        //先判断一下
        if (StringUtils.isBlank(phone)) {
            return ResponseServer.error(ServerEnum.PHONE_ISNULL);
        }
        //先发送验证码
        //JSONObject jsonObject = SendMessage.sendMessage(phone);
    /*    String code = jsonObject.getString("code");
        if (code.equals("200")) {
            String checkCode = jsonObject.getString("obj");
            redisTemplate.opsForValue().set("code_" + phone, checkCode, 300, TimeUnit.SECONDS);
        }*/
        redisTemplate.opsForValue().set("code_" + phone, "123456", 300, TimeUnit.SECONDS);
        return ResponseServer.success();
    }

    /**
     * 用户登录
     * @param phone
     * @param checkCode
     * @return
     */
    @PostMapping
    public ResponseServer login(String phone,String checkCode){
        //判断手机号或者验证码不能为空
        if(StringUtils.isBlank(phone) || StringUtils.isBlank(checkCode)){
                return ResponseServer.error(ServerEnum.LOGIN_PHONEORCODE_INNULL);
        }
        //先验证验证码是否正确
        String code= (String) redisTemplate.opsForValue().get("code_" + phone);
        if(!checkCode.equals(code)){
            return ResponseServer.error(ServerEnum.LOGIN_CODE_ERROR);
        }
        //删除该手机的验证码
        redisTemplate.delete("code_" + phone);

        //判断手机号是否存在，不存在就注册
        CustomerBean customer = customerService.isRegister(phone);
        redisTemplate.opsForValue().set("user_"+phone,customer);
        redisTemplate.opsForValue().set("cartid_"+phone,customer.getCartId());
        //生成Token
        Map<String,Object> map=new HashMap<String,Object>();
        map.put("phone",customer.getPhone());
        String token=JwtUtils.createToken(map);

        return ResponseServer.success(token);

    }
}
