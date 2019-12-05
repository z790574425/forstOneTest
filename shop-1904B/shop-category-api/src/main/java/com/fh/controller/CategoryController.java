package com.fh.controller;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.TypeReference;
import com.fh.redis.IRedisService;
import com.fh.service.ICategoryService;
import com.fh.utils.response.ResponseServer;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("categorys")
@CrossOrigin(maxAge = 3600,origins = "http://localhost:8080")
public class CategoryController {

    @Autowired
    private ICategoryService categoryService;

    @Autowired
    private IRedisService redisService;
    /**
     * 查询所有的商品类别数据
     * @return
     */
    @GetMapping
    public ResponseServer queryCategoryList(){
        //先查缓存再去查数据库
        Boolean isExistKey=redisService.isExistKey("categoryAll");
       Object categoryList=null;
        if(!isExistKey){
            categoryList=categoryService.queryCategoryList();
            redisService.setStringKeyAndValue("categoryAll",categoryList);
        }else{
            categoryList=redisService.getStringValueByKey("categoryAll");
        }
        return ResponseServer.success(categoryList);
    }
}
