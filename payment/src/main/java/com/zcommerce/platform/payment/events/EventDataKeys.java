package com.zcommerce.platform.payment.events;

// Enum for event data keys to avoid magic strings.
public enum EventDataKeys {
  
  EVENT_TYPE("eventType"),
  EVENT_ID("eventId"),
  TIMESTAMP("timestamp"),
  PAYMENT_ID("paymentId"),
  ORDER_ID("orderId"),
  CUSTOMER_ID("customerId"),
  AMOUNT("amount"),
  STATUS("status"),
  PAYMENT_METHOD("paymentMethod"),
  TRANSACTION_ID("transactionId"),
  GATEWAY_RESPONSE("gatewayResponse"),
  FAILURE_REASON("failureReason"),
  PROCESSED_AT("processedAt"),
  CREATED_AT("createdAt"),
  UPDATED_AT("updatedAt"),
  SERVICE_NAME("serviceName"),
  SERVICE_VERSION("serviceVersion");

  private final String key;

  EventDataKeys(String key) {
    this.key = key;
  }

  public String getKey() {
    return key;
  }

  @Override
  public String toString() {
    return key;
  }
}

