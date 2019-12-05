package com.fh;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.data.redis.repository.configuration.EnableRedisRepositories;

@SpringBootApplication
@EnableRedisRepositories
public class ShopCartApplication {

    public static void main(String[] args) {
        SpringApplication.run(ShopCartApplication.class, args);
    }

}
