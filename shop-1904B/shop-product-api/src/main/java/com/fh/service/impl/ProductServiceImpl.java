package com.fh.service.impl;

import com.fh.bean.ProductParamter;
import com.fh.bean.TShopProduct;
import com.fh.dao.ProductMapper;
import com.fh.service.IProductService;
import com.fh.utils.page.PageBean;
import com.fh.utils.response.ResponseServer;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service("productService")
public class ProductServiceImpl implements IProductService {
    @Autowired
    private ProductMapper productMapper;
    @Override
    public PageBean<TShopProduct> queryProductPageList(PageBean<TShopProduct> page,ProductParamter paramter) {
        //查询总条数
        Long count=productMapper.queryProductCount(paramter);
        page.setRecordsFiltered(count);
        page.setRecordsTotal(count);
        //查询分页数据
        List<TShopProduct> productList=productMapper.queryList(page,paramter);
        page.setData(productList);
        return page;
    }

    @Override
    public ResponseServer getProductById(Integer productId) {
        TShopProduct product=productMapper.getProductById(productId);
        return ResponseServer.success(product);
    }
}
