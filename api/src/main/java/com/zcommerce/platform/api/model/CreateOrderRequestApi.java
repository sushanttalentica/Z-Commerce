package com.zcommerce.platform.api.model;

import java.net.URI;
import java.util.Objects;
import com.zcommerce.platform.api.model.OrderItemRequestApi;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonCreator;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import org.openapitools.jackson.nullable.JsonNullable;
import java.time.OffsetDateTime;
import jakarta.validation.Valid;
import jakarta.validation.constraints.*;
import io.swagger.v3.oas.annotations.media.Schema;


import java.util.*;
import jakarta.annotation.Generated;

/**
 * CreateOrderRequestApi
 */

@Generated(value = "org.openapitools.codegen.languages.SpringCodegen", date = "2025-10-16T03:23:32.468932+05:30[Asia/Kolkata]")
public class CreateOrderRequestApi {

  private Long customerId;

  private String customerEmail;

  private String shippingAddress;

  @Valid
  private List<@Valid OrderItemRequestApi> orderItems = new ArrayList<>();

  public CreateOrderRequestApi() {
    super();
  }

  /**
   * Constructor with only required parameters
   */
  public CreateOrderRequestApi(Long customerId, String customerEmail, List<@Valid OrderItemRequestApi> orderItems) {
    this.customerId = customerId;
    this.customerEmail = customerEmail;
    this.orderItems = orderItems;
  }

  public CreateOrderRequestApi customerId(Long customerId) {
    this.customerId = customerId;
    return this;
  }

  /**
   * Get customerId
   * @return customerId
  */
  @NotNull 
  @Schema(name = "customerId", requiredMode = Schema.RequiredMode.REQUIRED)
  @JsonProperty("customerId")
  public Long getCustomerId() {
    return customerId;
  }

  public void setCustomerId(Long customerId) {
    this.customerId = customerId;
  }

  public CreateOrderRequestApi customerEmail(String customerEmail) {
    this.customerEmail = customerEmail;
    return this;
  }

  /**
   * Get customerEmail
   * @return customerEmail
  */
  @NotNull @jakarta.validation.constraints.Email
  @Schema(name = "customerEmail", requiredMode = Schema.RequiredMode.REQUIRED)
  @JsonProperty("customerEmail")
  public String getCustomerEmail() {
    return customerEmail;
  }

  public void setCustomerEmail(String customerEmail) {
    this.customerEmail = customerEmail;
  }

  public CreateOrderRequestApi shippingAddress(String shippingAddress) {
    this.shippingAddress = shippingAddress;
    return this;
  }

  /**
   * Get shippingAddress
   * @return shippingAddress
  */
  
  @Schema(name = "shippingAddress", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("shippingAddress")
  public String getShippingAddress() {
    return shippingAddress;
  }

  public void setShippingAddress(String shippingAddress) {
    this.shippingAddress = shippingAddress;
  }

  public CreateOrderRequestApi orderItems(List<@Valid OrderItemRequestApi> orderItems) {
    this.orderItems = orderItems;
    return this;
  }

  public CreateOrderRequestApi addOrderItemsItem(OrderItemRequestApi orderItemsItem) {
    if (this.orderItems == null) {
      this.orderItems = new ArrayList<>();
    }
    this.orderItems.add(orderItemsItem);
    return this;
  }

  /**
   * Get orderItems
   * @return orderItems
  */
  @NotNull @Valid 
  @Schema(name = "orderItems", requiredMode = Schema.RequiredMode.REQUIRED)
  @JsonProperty("orderItems")
  public List<@Valid OrderItemRequestApi> getOrderItems() {
    return orderItems;
  }

  public void setOrderItems(List<@Valid OrderItemRequestApi> orderItems) {
    this.orderItems = orderItems;
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || getClass() != o.getClass()) {
      return false;
    }
    CreateOrderRequestApi createOrderRequestApi = (CreateOrderRequestApi) o;
    return Objects.equals(this.customerId, createOrderRequestApi.customerId) &&
        Objects.equals(this.customerEmail, createOrderRequestApi.customerEmail) &&
        Objects.equals(this.shippingAddress, createOrderRequestApi.shippingAddress) &&
        Objects.equals(this.orderItems, createOrderRequestApi.orderItems);
  }

  @Override
  public int hashCode() {
    return Objects.hash(customerId, customerEmail, shippingAddress, orderItems);
  }

  @Override
  public String toString() {
    StringBuilder sb = new StringBuilder();
    sb.append("class CreateOrderRequestApi {\n");
    sb.append("    customerId: ").append(toIndentedString(customerId)).append("\n");
    sb.append("    customerEmail: ").append(toIndentedString(customerEmail)).append("\n");
    sb.append("    shippingAddress: ").append(toIndentedString(shippingAddress)).append("\n");
    sb.append("    orderItems: ").append(toIndentedString(orderItems)).append("\n");
    sb.append("}");
    return sb.toString();
  }

  /**
   * Convert the given object to string with each line indented by 4 spaces
   * (except the first line).
   */
  private String toIndentedString(Object o) {
    if (o == null) {
      return "null";
    }
    return o.toString().replace("\n", "\n    ");
  }
}

