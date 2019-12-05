package com.fh.service;

import com.fh.bean.AddressBean;
import com.fh.utils.response.ResponseServer;

public interface IAddressService {
    ResponseServer queryAddressList(String userPhone);

    ResponseServer saveAddress(String userPhone, AddressBean address);

    ResponseServer getAddressById(Integer addressId);
}
