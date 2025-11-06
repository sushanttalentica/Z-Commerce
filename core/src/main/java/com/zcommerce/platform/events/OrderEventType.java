package com.zcommerce.platform.events;

// Enum for order event types to avoid magic strings.
public enum OrderEventType {
  ORDER_CREATED("ORDER_CREATED"),
  ORDER_STATUS_UPDATED("ORDER_STATUS_UPDATED"),
  ORDER_CANCELLED("ORDER_CANCELLED"),
  ORDER_COMPLETED("ORDER_COMPLETED");

  private final String eventType;

  OrderEventType(String eventType) {
    this.eventType = eventType;
  }

  public String getEventType() {
    return eventType;
  }

  @Override
  public String toString() {
    return eventType;
  }
}

