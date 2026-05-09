package com.fyfe.common;

import lombok.Data;

import java.util.List;

@Data
public class Order {
    private String orderId;
    private Double totalAmount;
    private String status;
    private List<OrderItem> items;
}
