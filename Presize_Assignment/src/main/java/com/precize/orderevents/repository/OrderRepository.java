package com.precize.orderevents.repository;

import com.precize.orderevents.model.Order;
import org.springframework.stereotype.Repository;

import java.util.Collection;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

@Repository
public class OrderRepository {

    // In-memory storage for orders
    private final Map<String, Order> orders = new ConcurrentHashMap<>();

    public Order save(Order order) {
        orders.put(order.getOrderId(), order);
        return order;
    }

    public Order findById(String orderId) {
        return orders.get(orderId);
    }

    public Collection<Order> findAll() {
        return orders.values();
    }

    public void delete(String orderId) {
        orders.remove(orderId);
    }

    public void clear() {
        orders.clear();
    }
}
