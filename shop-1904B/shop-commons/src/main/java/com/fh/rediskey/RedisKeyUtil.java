package com.fh.rediskey;

public class RedisKeyUtil {

    public static  String  getCartIdKey(String phone){
        return "cartid_"+phone;
    }

    public static String getWaitPayKey(String phone){
        return "pay_wait_"+phone;
    }
}
