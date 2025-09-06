package com.precize.orderevents.observer;

import com.precize.orderevents.event.Event;
import com.precize.orderevents.model.Order;

public class AlertObserver implements OrderObserver {

    @Override
    public void onEventProcessed(Order order, Event event) {
        // no-op
    }

    @Override
    public void onStatusChanged(Order order, String previousStatus, String newStatus) {
        if ("CANCELLED".equals(newStatus) || "SHIPPED".equals(newStatus)) {
            System.out.println("[AlertObserver] Sending alert for Order " + order.getOrderId() + ": Status changed to " + newStatus);
        }
    }

}