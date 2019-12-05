package com.fh.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.fh.bean.CustomerBean;
import com.fh.dao.ICustomerMapper;
import com.fh.service.ICustomerService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Date;
import java.util.UUID;

@Service("customerService")
public class CustomerServiceImpl implements ICustomerService {
    @Autowired
    private ICustomerMapper customerMapper;

    @Override
    @Transactional
    public CustomerBean isRegister(String phone) {
        //判断手机号是否存在
        QueryWrapper<CustomerBean> queryWrapper = new QueryWrapper<CustomerBean>();
        queryWrapper.eq("phone", phone);
        CustomerBean customer= customerMapper.selectOne(queryWrapper);
        if(customer == null){
            customer =new CustomerBean();
            customer.setModifiedTime(new Date());
            customer.setUserStats(1);
            customer.setPhone(phone);
            customer.setCartId(UUID.randomUUID().toString().replace("-",""));
            customerMapper.insert(customer);
        }else{
            customer.setModifiedTime(new Date());
            customerMapper.updateById(customer);
        }
        return customer;
    }
}
