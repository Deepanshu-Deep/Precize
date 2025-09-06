package com.precize.orderevents.model;

import lombok.Data;
import lombok.NoArgsConstructor;
import java.util.ArrayList;
import java.util.List;

@Data
@NoArgsConstructor
public class Order {

    private String orderId;
    private String customerId;
    private List<Item> items = new ArrayList<>();
    private double totalAmount;
    private double amountPaid = 0.0;
    private OrderStatus status = OrderStatus.PENDING;
    private List<String> eventHistory = new ArrayList<>();

    public Order(String orderId, String customerId, List<Item> items, double totalAmount) {
        this.orderId = orderId;
        this.customerId = customerId;
        this.items = items;
        this.totalAmount = totalAmount;
    }

    public void addEventToHistory(String eventDesc) {
        this.eventHistory.add(eventDesc);
    }
}