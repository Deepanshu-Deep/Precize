package com.precize.orderevents.event;

import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class PaymentReceivedEvent extends Event {

    private String orderId;
    private double amountPaid;
}