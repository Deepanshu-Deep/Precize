package com.precize.orderevents.processor;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import com.precize.orderevents.event.*;
import com.precize.orderevents.model.Order;
import com.precize.orderevents.model.OrderStatus;
import com.precize.orderevents.observer.OrderObserver;
import com.precize.orderevents.repository.InMemoryOrderRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.core.io.ClassPathResource;
import org.springframework.stereotype.Service;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;

@Service
@Slf4j
public class EventProcessor {

    private final InMemoryOrderRepository repository;
    private final ObjectMapper mapper;
    private final List<OrderObserver> observers = new ArrayList<>();

    public EventProcessor(InMemoryOrderRepository repository) {
        this.repository = repository;
        this.mapper = new ObjectMapper();
        this.mapper.registerModule(new JavaTimeModule());
    }

    private final ObjectMapper objectMapper = new ObjectMapper();

    public void registerObserver(OrderObserver observer) {
        observers.add(observer);
    }

    public void notifyEventProcessed(Order order, Event event) {
        for (OrderObserver o : observers) o.onEventProcessed(order, event);
    }

    public void notifyStatusChanged(Order order, String prev, String next) {
        for (OrderObserver o : observers) o.onStatusChanged(order, prev, next);
    }

    /**
     * Parse a JSON string into an Event (polymorphic).
     * Exposed publicly so controller can parse each line and then process it.
     */
    public Event parseEvent(String json) throws Exception {
        return mapper.readValue(json, Event.class);
    }

    /**
     * Core processing logic for each Event subclass.
     */
    public void processEvent(Event event) {
        if (event == null) {
            log.warn("Null event passed to processEvent");
            return;
        }

        if (event instanceof OrderCreatedEvent) {
            OrderCreatedEvent e = (OrderCreatedEvent) event;
            Order order = new Order(e.getOrderId(), e.getCustomerId(), e.getItems(), e.getTotalAmount());
            order.setStatus(OrderStatus.PENDING);
            order.addEventToHistory("Created: " + e.getEventId());
            repository.save(order);
            notifyEventProcessed(order, event);
        } else if (event instanceof PaymentReceivedEvent) {
            PaymentReceivedEvent e = (PaymentReceivedEvent) event;
            Order order = repository.findById(e.getOrderId());
            if (order == null) {
                log.warn("Received payment for unknown order {}", e.getOrderId());
                return;
            }

            double prevPaid = order.getAmountPaid();
            double newPaid = prevPaid + e.getAmount();
            order.setAmountPaid(newPaid);
            order.addEventToHistory("Payment: " + e.getEventId() + " amount=" + e.getAmount());

            String prevStatus = order.getStatus().name();
            if (Double.compare(newPaid, order.getTotalAmount()) >= 0) {
                order.setStatus(OrderStatus.PAID);
            } else if (Double.compare(newPaid, 0.0) > 0) {
                order.setStatus(OrderStatus.PARTIALLY_PAID);
            }
            repository.save(order);
            notifyEventProcessed(order, event);
            if (!prevStatus.equals(order.getStatus().name())) {
                notifyStatusChanged(order, prevStatus, order.getStatus().name());
            }
        } else if (event instanceof ShippingScheduledEvent) {
            ShippingScheduledEvent e = (ShippingScheduledEvent) event;
            Order order = repository.findById(e.getOrderId());
            if (order == null) {
                log.warn("Shipping scheduled for unknown order {}", e.getOrderId());
                return;
            }
            String prevStatus = order.getStatus().name();
            order.setStatus(OrderStatus.SHIPPED);
            order.addEventToHistory("Shipped: " + e.getEventId());
            repository.save(order);
            notifyEventProcessed(order, event);
            notifyStatusChanged(order, prevStatus, order.getStatus().name());
        } else if (event instanceof OrderCancelledEvent) {
            OrderCancelledEvent e = (OrderCancelledEvent) event;
            Order order = repository.findById(e.getOrderId());
            if (order == null) {
                log.warn("Cancel requested for unknown order {}", e.getOrderId());
                return;
            }
            String prevStatus = order.getStatus().name();
            order.setStatus(OrderStatus.CANCELLED);
            order.addEventToHistory("Cancelled: " + e.getEventId() + " reason=" + e.getReason());
            repository.save(order);
            notifyEventProcessed(order, event);
            notifyStatusChanged(order, prevStatus, order.getStatus().name());
        } else {
            log.warn("Unknown event type: {}", event.getClass().getName());
        }
    }

    /**
     * Read file where each line is a JSON object and process events.
     * Keeps going even if some lines fail to parse.
     */
    public void processEventsFromFile(String fileName) {
        try {
            ClassPathResource resource = new ClassPathResource(fileName);

            try (BufferedReader reader = new BufferedReader(new InputStreamReader(resource.getInputStream()))) {
                String line;
                while ((line = reader.readLine()) != null) {
                    // Deserialize JSON line into Event
                    Event event = objectMapper.readValue(line, Event.class);
                    log.info("Processing event: {}", event.getEventType());
                    // your order update logic goes here...
                }
            }
        } catch (Exception e) {
            log.error("Failed to read file {} : {}", fileName, e.getMessage());
        }
    }
}

