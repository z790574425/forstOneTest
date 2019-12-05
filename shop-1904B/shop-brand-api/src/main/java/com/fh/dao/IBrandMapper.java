package com.fh.dao;

import com.fh.bean.BrandBean;
import org.apache.ibatis.annotations.Mapper;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
@Mapper
public interface IBrandMapper {
    List<BrandBean> queryBrandByCateId();
}
