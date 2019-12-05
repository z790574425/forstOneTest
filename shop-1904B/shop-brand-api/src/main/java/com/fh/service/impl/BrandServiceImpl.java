package com.fh.service.impl;

import com.fh.bean.BrandBean;
import com.fh.dao.IBrandMapper;
import com.fh.service.IBrandService;
import com.fh.utils.response.ResponseServer;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Service("brandService")
public class BrandServiceImpl implements IBrandService {
    @Autowired
    private RedisTemplate redisTemplate;
    @Autowired
    private IBrandMapper brandMapper;
    @Override
    public ResponseServer queryBrandsByCateId(Integer pid) {
        Boolean isExistBrand=redisTemplate.hasKey("brands");
        List<BrandBean> brandBeans=new ArrayList<>();
        if(isExistBrand){
            brandBeans= (List<BrandBean>) redisTemplate.opsForValue().get("brands");
        }else{
            brandBeans=brandMapper.queryBrandByCateId();
            redisTemplate.opsForValue().set("brands",brandBeans);
        }
        //就是筛选出符合条件的品牌集合
        List<BrandBean> returnList= brandBeans.stream()
                .filter(brand->brand.getCategoryId().equals(pid))
                .collect(Collectors.toList());
        return ResponseServer.success(returnList);
    }
}
