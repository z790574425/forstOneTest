package com.fh.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.fh.bean.OrderItem;
import com.fh.bean.PayLog;
import org.apache.ibatis.annotations.Mapper;
import org.springframework.stereotype.Repository;

@Repository
@Mapper
public interface IPayLogMapper extends BaseMapper<PayLog> {



}
