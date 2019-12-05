package com.fh.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.fh.bean.Order;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

@Repository
@Mapper
public interface IOrderMapper extends BaseMapper<Order> {

    public Integer updateProductStock(@Param("productId") Integer productId, @Param("count")Integer count);
}
