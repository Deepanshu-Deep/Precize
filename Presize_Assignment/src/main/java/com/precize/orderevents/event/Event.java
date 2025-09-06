package com.precize.orderevents.event;

import com.fasterxml.jackson.annotation.JsonSubTypes;
import com.fasterxml.jackson.annotation.JsonTypeInfo;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.precize.orderevents.config.InstantDeserializer;
import lombok.Data;
import java.time.Instant;


@Data
@JsonTypeInfo(use = JsonTypeInfo.Id.NAME, property = "eventType", visible = true)
@JsonSubTypes({
        @JsonSubTypes.Type(value = OrderCreatedEvent.class, name = "OrderCreated"),
        @JsonSubTypes.Type(value = PaymentReceivedEvent.class, name = "PaymentReceived"),
        @JsonSubTypes.Type(value = ShippingScheduledEvent.class, name = "ShippingScheduled"),
        @JsonSubTypes.Type(value = OrderCancelledEvent.class, name = "OrderCancelled")
})
public abstract class Event {

    private String eventId;

    @JsonDeserialize(using = InstantDeserializer.class)
    private Instant timestamp;
    private EventType eventType;
}