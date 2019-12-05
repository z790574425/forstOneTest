package com.fh.bean;

import lombok.Data;

import java.io.Serializable;

@Data
public class BrandBean implements Serializable {

    private Integer brandId;

    private String brandName;

    private String telephone;

    private String brandWeb;

    private String brandLogo;

    private Integer categoryId;

}
