package com.fh.controller;

import com.fh.bean.ProductParamter;
import com.fh.bean.TShopProduct;
import com.fh.service.IProductService;
import com.fh.utils.page.PageBean;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/products")
@CrossOrigin(maxAge = 3600,origins = "http://localhost:8080")
public class ProductController {

    @Autowired
    private IProductService productService;

    @PostMapping
    public PageBean<TShopProduct> queryProductList(PageBean<TShopProduct> page, ProductParamter paramter){
            page=productService.queryProductPageList(page,paramter);
            return page;
    }


}
