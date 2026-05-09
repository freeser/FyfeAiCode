package com.fyfe.common;

import lombok.Data;

@Data
public class OrderItem {
    private String productName;
    private Integer count;
    private Double subTotal;
}
