package com.precize.orderevents.observer;

import com.precize.orderevents.event.Event;
import com.precize.orderevents.model.Order;
import lombok.extern.slf4j.Slf4j;

@Slf4j
public class LoggerObserver implements OrderObserver {

    @Override
    public void onEventProcessed(Order order, Event event) {
        log.info("[LoggerObserver] Processed event {} for order {}", event != null ? event.getEventId() : "<null>", order != null ? order.getOrderId() : "<unknown>");
    }

    @Override
    public void onStatusChanged(Order order, String previousStatus, String newStatus) {
        log.info("[LoggerObserver] Order {} status changed from {} to {}", order.getOrderId(), previousStatus, newStatus);
    }
}