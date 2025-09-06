package com.precize.orderevents.event;

import com.precize.orderevents.model.Item;
import lombok.Data;
import java.util.*;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class OrderCreatedEvent extends Event {

    private String orderId;
    private String customerId;
    private List<Item> items;
    private double totalAmount;
}