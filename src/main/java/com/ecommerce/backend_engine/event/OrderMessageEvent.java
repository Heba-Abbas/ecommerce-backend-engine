package com.ecommerce.backend_engine.event;

import lombok.Getter;

@Getter
public class OrderMessageEvent {

    private final Long orderId;

    public OrderMessageEvent(Long orderId) {
        this.orderId = orderId;
    }

    public Long getOrderId() {
        return orderId;
    }

}