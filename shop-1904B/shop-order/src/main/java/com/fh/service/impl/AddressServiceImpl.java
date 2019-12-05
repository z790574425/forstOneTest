package com.fh.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.fh.bean.AddressBean;
import com.fh.bean.CustomerBean;
import com.fh.mapper.IAddressMapper;
import com.fh.service.IAddressService;
import com.fh.utils.response.ResponseServer;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.List;

@Service("addressService")
public class AddressServiceImpl implements IAddressService {
    @Autowired
    private IAddressMapper addressMapper;
    @Autowired
    private RedisTemplate redisTemplate;

    @Override
    public ResponseServer queryAddressList(String userPhone) {
       CustomerBean customer= (CustomerBean) redisTemplate.opsForValue().get("user_"+userPhone);
        Integer userId=customer.getCustomerId();
        QueryWrapper<AddressBean> queryWrapper=new QueryWrapper<AddressBean>();
        queryWrapper.eq("creatorId",userId);
        queryWrapper.orderByDesc("createTime");
        List<AddressBean> addressList=addressMapper.selectList(queryWrapper);
        return ResponseServer.success(addressList);
    }

    @Override
    public ResponseServer saveAddress(String userPhone, AddressBean address) {
        CustomerBean customer= (CustomerBean) redisTemplate.opsForValue().get("user_"+userPhone);
        Integer userId=customer.getCustomerId();
        address.setCreatorId(userId);
        address.setCreateTime(new Date());
        if(address.getId() == null){
            addressMapper.insert(address);
        }else{
            addressMapper.updateById(address);
        }

        return ResponseServer.success();
    }

    @Override
    public ResponseServer getAddressById(Integer addressId) {
        AddressBean address=addressMapper.selectById(addressId);
        return ResponseServer.success(address);
    }
}
