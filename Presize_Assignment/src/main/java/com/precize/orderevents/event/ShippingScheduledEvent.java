package com.precize.orderevents.event;

import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.Instant;

@Data
@NoArgsConstructor
public class ShippingScheduledEvent extends Event {

    private String orderId;
    private Instant shippingDate;
}
