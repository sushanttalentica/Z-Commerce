package com.zcommerce.platform.payment.events;

// Enum for service constants to avoid magic strings.
public enum ServiceConstants {
  
  SERVICE_NAME("zcommerce-platform"),
  SERVICE_VERSION("1.0.0");

  private final String value;

  ServiceConstants(String value) {
    this.value = value;
  }

  public String getValue() {
    return value;
  }

  @Override
  public String toString() {
    return value;
  }
}
