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
 * PaymentStatistics
 */

@Generated(value = "org.openapitools.codegen.languages.SpringCodegen", date = "2025-10-16T03:23:32.468932+05:30[Asia/Kolkata]")
public class PaymentStatistics {

  private Long totalPayments;

  private Long successfulPayments;

  private Long failedPayments;

  private BigDecimal totalAmount;

  public PaymentStatistics totalPayments(Long totalPayments) {
    this.totalPayments = totalPayments;
    return this;
  }

  /**
   * Get totalPayments
   * @return totalPayments
  */
  
  @Schema(name = "totalPayments", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("totalPayments")
  public Long getTotalPayments() {
    return totalPayments;
  }

  public void setTotalPayments(Long totalPayments) {
    this.totalPayments = totalPayments;
  }

  public PaymentStatistics successfulPayments(Long successfulPayments) {
    this.successfulPayments = successfulPayments;
    return this;
  }

  /**
   * Get successfulPayments
   * @return successfulPayments
  */
  
  @Schema(name = "successfulPayments", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("successfulPayments")
  public Long getSuccessfulPayments() {
    return successfulPayments;
  }

  public void setSuccessfulPayments(Long successfulPayments) {
    this.successfulPayments = successfulPayments;
  }

  public PaymentStatistics failedPayments(Long failedPayments) {
    this.failedPayments = failedPayments;
    return this;
  }

  /**
   * Get failedPayments
   * @return failedPayments
  */
  
  @Schema(name = "failedPayments", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("failedPayments")
  public Long getFailedPayments() {
    return failedPayments;
  }

  public void setFailedPayments(Long failedPayments) {
    this.failedPayments = failedPayments;
  }

  public PaymentStatistics totalAmount(BigDecimal totalAmount) {
    this.totalAmount = totalAmount;
    return this;
  }

  /**
   * Get totalAmount
   * @return totalAmount
  */
  @Valid 
  @Schema(name = "totalAmount", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("totalAmount")
  public BigDecimal getTotalAmount() {
    return totalAmount;
  }

  public void setTotalAmount(BigDecimal totalAmount) {
    this.totalAmount = totalAmount;
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || getClass() != o.getClass()) {
      return false;
    }
    PaymentStatistics paymentStatistics = (PaymentStatistics) o;
    return Objects.equals(this.totalPayments, paymentStatistics.totalPayments) &&
        Objects.equals(this.successfulPayments, paymentStatistics.successfulPayments) &&
        Objects.equals(this.failedPayments, paymentStatistics.failedPayments) &&
        Objects.equals(this.totalAmount, paymentStatistics.totalAmount);
  }

  @Override
  public int hashCode() {
    return Objects.hash(totalPayments, successfulPayments, failedPayments, totalAmount);
  }

  @Override
  public String toString() {
    StringBuilder sb = new StringBuilder();
    sb.append("class PaymentStatistics {\n");
    sb.append("    totalPayments: ").append(toIndentedString(totalPayments)).append("\n");
    sb.append("    successfulPayments: ").append(toIndentedString(successfulPayments)).append("\n");
    sb.append("    failedPayments: ").append(toIndentedString(failedPayments)).append("\n");
    sb.append("    totalAmount: ").append(toIndentedString(totalAmount)).append("\n");
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

