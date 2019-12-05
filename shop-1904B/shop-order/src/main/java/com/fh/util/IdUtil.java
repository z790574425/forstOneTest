package com.fh.util;

import com.baomidou.mybatisplus.core.toolkit.IdWorker;

public class IdUtil {

    public static  String getOrderId(){
        return IdWorker.getIdStr();
    }
}
