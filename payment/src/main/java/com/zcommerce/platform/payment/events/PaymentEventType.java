package com.zcommerce.platform.payment.events;

// Enum for payment event types to avoid magic strings.
public enum PaymentEventType {
  PAYMENT_PROCESSED("PAYMENT_PROCESSED"),
  PAYMENT_FAILED("PAYMENT_FAILED"),
  PAYMENT_REFUNDED("PAYMENT_REFUNDED"),
  PAYMENT_CANCELLED("PAYMENT_CANCELLED"),
  PAYMENT_RETRY("PAYMENT_RETRY");

  private final String eventType;

  PaymentEventType(String eventType) {
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

