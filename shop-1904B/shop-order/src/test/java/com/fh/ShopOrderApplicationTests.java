package com.fh;


import com.baomidou.mybatisplus.core.toolkit.IdWorker;
import com.fh.util.BigDecimalUtil;
import org.junit.Test;
import org.springframework.boot.test.context.SpringBootTest;



@SpringBootTest
public class ShopOrderApplicationTests {

    @Test
    public void contextLoads() {

        int price=BigDecimalUtil.mul("12.88","100").intValue();
        System.out.println( price);

    }

}
