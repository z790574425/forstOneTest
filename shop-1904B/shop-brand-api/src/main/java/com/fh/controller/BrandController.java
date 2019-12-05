package com.fh.controller;

import com.fh.service.IBrandService;
import com.fh.utils.response.ResponseServer;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/brands")
@CrossOrigin(maxAge = 3600,origins = "http://localhost:8080")
public class BrandController {

    @Autowired
    private IBrandService brandService;
    /**
     * 根据商品类别ID查询品牌信息
     * @param pid
     * @return
     */
    @GetMapping("/{pid}")
    public ResponseServer queryBrandsByCateId(@PathVariable("pid") Integer pid){
        return brandService.queryBrandsByCateId(pid);
    }
}
