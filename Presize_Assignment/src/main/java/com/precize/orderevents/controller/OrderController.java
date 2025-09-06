package com.precize.orderevents.controller;

import com.precize.orderevents.model.Order;
import com.precize.orderevents.repository.OrderRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Collection;

@RestController
@RequestMapping("/orders")
@RequiredArgsConstructor
public class OrderController {

    private final OrderRepository orderRepository;

    // Fetch all current orders with status and details
    @GetMapping
    public Collection<Order> getAllOrders() {
        return orderRepository.findAll();
    }
}