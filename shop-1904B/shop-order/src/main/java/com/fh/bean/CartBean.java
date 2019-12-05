package com.fh.bean;

import lombok.Data;

import java.io.Serializable;
import java.math.BigDecimal;

@Data
public class CartBean implements Serializable {

        private Integer productId;

        private String productName;

        private String mainImg;

        private BigDecimal price;

        private Integer count;

        private BigDecimal subtotal;

        private Boolean isChecked;

        private Boolean isStock;
}
