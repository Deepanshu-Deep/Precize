package com.precize.orderevents.repository;

import com.precize.orderevents.model.Order;
import org.springframework.stereotype.Repository;

import java.util.Collection;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

@Repository
public class InMemoryOrderRepository {
    private final Map<String, Order> store = new ConcurrentHashMap<>();

    public Order save(Order order) {
        store.put(order.getOrderId(), order);
        return order;
    }

    public Order findById(String orderId) {
        return store.get(orderId);
    }

    public Collection<Order> findAll() {
        return store.values();
    }
}

