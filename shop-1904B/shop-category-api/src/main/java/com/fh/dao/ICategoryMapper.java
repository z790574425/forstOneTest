package com.fh.dao;

import com.fh.bean.CategoryBean;
import org.apache.ibatis.annotations.Mapper;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
@Mapper
public interface ICategoryMapper {
    List<CategoryBean> queryCategoryList();
}
