package com.zcommerce.platform.api.model;

import java.net.URI;
import java.util.Objects;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonCreator;
import java.math.BigDecimal;
import org.openapitools.jackson.nullable.JsonNullable;
import java.time.OffsetDateTime;
import jakarta.validation.Valid;
import jakarta.validation.constraints.*;
import io.swagger.v3.oas.annotations.media.Schema;


import java.util.*;
import jakarta.annotation.Generated;

/**
 * OrderStatistics
 */

@Generated(value = "org.openapitools.codegen.languages.SpringCodegen", date = "2025-10-16T03:23:32.468932+05:30[Asia/Kolkata]")
public class OrderStatistics {

  private Long totalOrders;

  private BigDecimal totalRevenue;

  private BigDecimal averageOrderValue;

  public OrderStatistics totalOrders(Long totalOrders) {
    this.totalOrders = totalOrders;
    return this;
  }

  /**
   * Get totalOrders
   * @return totalOrders
  */
  
  @Schema(name = "totalOrders", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("totalOrders")
  public Long getTotalOrders() {
    return totalOrders;
  }

  public void setTotalOrders(Long totalOrders) {
    this.totalOrders = totalOrders;
  }

  public OrderStatistics totalRevenue(BigDecimal totalRevenue) {
    this.totalRevenue = totalRevenue;
    return this;
  }

  /**
   * Get totalRevenue
   * @return totalRevenue
  */
  @Valid 
  @Schema(name = "totalRevenue", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("totalRevenue")
  public BigDecimal getTotalRevenue() {
    return totalRevenue;
  }

  public void setTotalRevenue(BigDecimal totalRevenue) {
    this.totalRevenue = totalRevenue;
  }

  public OrderStatistics averageOrderValue(BigDecimal averageOrderValue) {
    this.averageOrderValue = averageOrderValue;
    return this;
  }

  /**
   * Get averageOrderValue
   * @return averageOrderValue
  */
  @Valid 
  @Schema(name = "averageOrderValue", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("averageOrderValue")
  public BigDecimal getAverageOrderValue() {
    return averageOrderValue;
  }

  public void setAverageOrderValue(BigDecimal averageOrderValue) {
    this.averageOrderValue = averageOrderValue;
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || getClass() != o.getClass()) {
      return false;
    }
    OrderStatistics orderStatistics = (OrderStatistics) o;
    return Objects.equals(this.totalOrders, orderStatistics.totalOrders) &&
        Objects.equals(this.totalRevenue, orderStatistics.totalRevenue) &&
        Objects.equals(this.averageOrderValue, orderStatistics.averageOrderValue);
  }

  @Override
  public int hashCode() {
    return Objects.hash(totalOrders, totalRevenue, averageOrderValue);
  }

  @Override
  public String toString() {
    StringBuilder sb = new StringBuilder();
    sb.append("class OrderStatistics {\n");
    sb.append("    totalOrders: ").append(toIndentedString(totalOrders)).append("\n");
    sb.append("    totalRevenue: ").append(toIndentedString(totalRevenue)).append("\n");
    sb.append("    averageOrderValue: ").append(toIndentedString(averageOrderValue)).append("\n");
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

