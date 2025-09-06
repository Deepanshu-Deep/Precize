package com.precize.orderevents.event;

import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class OrderCancelledEvent extends Event {

    private String orderId;
    private String reason;
}