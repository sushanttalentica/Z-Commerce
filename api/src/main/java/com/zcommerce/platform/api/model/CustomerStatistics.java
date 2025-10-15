package com.zcommerce.platform.api.model;

import java.net.URI;
import java.util.Objects;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonCreator;
import org.openapitools.jackson.nullable.JsonNullable;
import java.time.OffsetDateTime;
import jakarta.validation.Valid;
import jakarta.validation.constraints.*;
import io.swagger.v3.oas.annotations.media.Schema;


import java.util.*;
import jakarta.annotation.Generated;

/**
 * CustomerStatistics
 */

@Generated(value = "org.openapitools.codegen.languages.SpringCodegen", date = "2025-10-16T03:23:32.468932+05:30[Asia/Kolkata]")
public class CustomerStatistics {

  private Long totalCustomers;

  private Long activeCustomers;

  private Long verifiedEmails;

  public CustomerStatistics totalCustomers(Long totalCustomers) {
    this.totalCustomers = totalCustomers;
    return this;
  }

  /**
   * Get totalCustomers
   * @return totalCustomers
  */
  
  @Schema(name = "totalCustomers", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("totalCustomers")
  public Long getTotalCustomers() {
    return totalCustomers;
  }

  public void setTotalCustomers(Long totalCustomers) {
    this.totalCustomers = totalCustomers;
  }

  public CustomerStatistics activeCustomers(Long activeCustomers) {
    this.activeCustomers = activeCustomers;
    return this;
  }

  /**
   * Get activeCustomers
   * @return activeCustomers
  */
  
  @Schema(name = "activeCustomers", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("activeCustomers")
  public Long getActiveCustomers() {
    return activeCustomers;
  }

  public void setActiveCustomers(Long activeCustomers) {
    this.activeCustomers = activeCustomers;
  }

  public CustomerStatistics verifiedEmails(Long verifiedEmails) {
    this.verifiedEmails = verifiedEmails;
    return this;
  }

  /**
   * Get verifiedEmails
   * @return verifiedEmails
  */
  
  @Schema(name = "verifiedEmails", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("verifiedEmails")
  public Long getVerifiedEmails() {
    return verifiedEmails;
  }

  public void setVerifiedEmails(Long verifiedEmails) {
    this.verifiedEmails = verifiedEmails;
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || getClass() != o.getClass()) {
      return false;
    }
    CustomerStatistics customerStatistics = (CustomerStatistics) o;
    return Objects.equals(this.totalCustomers, customerStatistics.totalCustomers) &&
        Objects.equals(this.activeCustomers, customerStatistics.activeCustomers) &&
        Objects.equals(this.verifiedEmails, customerStatistics.verifiedEmails);
  }

  @Override
  public int hashCode() {
    return Objects.hash(totalCustomers, activeCustomers, verifiedEmails);
  }

  @Override
  public String toString() {
    StringBuilder sb = new StringBuilder();
    sb.append("class CustomerStatistics {\n");
    sb.append("    totalCustomers: ").append(toIndentedString(totalCustomers)).append("\n");
    sb.append("    activeCustomers: ").append(toIndentedString(activeCustomers)).append("\n");
    sb.append("    verifiedEmails: ").append(toIndentedString(verifiedEmails)).append("\n");
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

