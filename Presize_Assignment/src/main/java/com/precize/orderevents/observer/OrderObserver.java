package com.precize.orderevents.observer;

import com.precize.orderevents.event.Event;
import com.precize.orderevents.model.Order;

public interface OrderObserver {

    void onEventProcessed(Order order, Event event);
    void onStatusChanged(Order order, String previousStatus, String newStatus);
}

