package com.zcommerce.platform.invoice.domain.entity;

import static org.assertj.core.api.Assertions.*;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

// JUnit tests for Invoice entity class.
@DisplayName("Invoice Entity Tests")
public class InvoiceTest {
  private Invoice invoice;
  private final LocalDateTime now = LocalDateTime.now();
  private final BigDecimal totalAmount = new BigDecimal("999.99");

  @BeforeEach
  void setUp() {
    invoice = new Invoice();
    invoice.setId(1L);
    invoice.setOrderId(100L);
    invoice.setCustomerId(200L);
    invoice.setCustomerEmail("test@example.com");
    invoice.setTotalAmount(totalAmount);
    invoice.setObjectKey("invoices/100/test-key.pdf");
    invoice.setObjectUrl("https://example.com/invoices/100/test-key.pdf");
    invoice.setStatus(Invoice.InvoiceStatus.GENERATED);
    invoice.setGeneratedAt(now);
    invoice.setCreatedAt(now);
    invoice.setUpdatedAt(now);
  }

  @Test
  @DisplayName("Invoice constructor creates invoice with all parameters")
  void invoiceConstructorCreatesInvoiceWithAllParameters() {
    Long id = 1L;
    Long orderId = 100L;
    Long customerId = 200L;
    String customerEmail = "test@example.com";
    BigDecimal totalAmount = new BigDecimal("999.99");
    String objectKey = "invoices/100/test-key.pdf";
    String objectUrl = "https://example.com/invoices/100/test-key.pdf";
    Invoice.InvoiceStatus status = Invoice.InvoiceStatus.GENERATED;
    LocalDateTime generatedAt = LocalDateTime.now();

    Invoice invoice =
        new Invoice(
            id, orderId, customerId, customerEmail, totalAmount, objectKey, objectUrl, status,
            generatedAt, now, now);

    assertThat(invoice).isNotNull();
    assertThat(invoice.getId()).isEqualTo(id);
    assertThat(invoice.getOrderId()).isEqualTo(orderId);
    assertThat(invoice.getCustomerId()).isEqualTo(customerId);
    assertThat(invoice.getCustomerEmail()).isEqualTo(customerEmail);
    assertThat(invoice.getTotalAmount()).isEqualTo(totalAmount);
    assertThat(invoice.getObjectKey()).isEqualTo(objectKey);
    assertThat(invoice.getObjectUrl()).isEqualTo(objectUrl);
    assertThat(invoice.getStatus()).isEqualTo(status);
    assertThat(invoice.getGeneratedAt()).isEqualTo(generatedAt);
    assertThat(invoice.getCreatedAt()).isEqualTo(now);
    assertThat(invoice.getUpdatedAt()).isEqualTo(now);
  }

  @Test
  @DisplayName("Should create empty invoice with default constructor")
  void shouldCreateEmptyInvoiceWithDefaultConstructor() {
    Invoice invoice = new Invoice();

    assertThat(invoice);
    assertThat(invoice.getId());
    assertThat(invoice.getOrderId());
    assertThat(invoice.getCustomerId());
    assertThat(invoice.getCustomerEmail());
    assertThat(invoice.getTotalAmount());
    assertThat(invoice.getObjectKey());
    assertThat(invoice.getObjectUrl());
    assertThat(Invoice.InvoiceStatus.GENERATED, invoice.getStatus());
    assertThat(invoice.getGeneratedAt());
    assertThat(invoice.getCreatedAt());
    assertThat(invoice.getUpdatedAt());
  }

  @Test
  @DisplayName("Should allow download when status is GENERATED")
  void shouldAllowDownloadWhenStatusIsGenerated() {
    invoice.setStatus(Invoice.InvoiceStatus.GENERATED);

    boolean canDownload = invoice.canBeDownloaded();

    assertThat(canDownload);
  }

  @Test
  @DisplayName("Should allow download when status is SENT")
  void shouldAllowDownloadWhenStatusIsSent() {
    invoice.setStatus(Invoice.InvoiceStatus.SENT);

    boolean canDownload = invoice.canBeDownloaded();

    assertThat(canDownload);
  }

  @Test
  @DisplayName("Should not allow download when status is FAILED")
  void shouldNotAllowDownloadWhenStatusIsFailed() {
    invoice.setStatus(Invoice.InvoiceStatus.FAILED);

    boolean canDownload = invoice.canBeDownloaded();

    assertThat(canDownload);
  }

  @Test
  @DisplayName("Should not allow download when status is DELETED")
  void shouldNotAllowDownloadWhenStatusIsDeleted() {
    invoice.setStatus(Invoice.InvoiceStatus.DELETED);

    boolean canDownload = invoice.canBeDownloaded();

    assertThat(canDownload);
  }

  @Test
  @DisplayName("Should allow regeneration when status is GENERATED")
  void shouldAllowRegenerationWhenStatusIsGenerated() {
    invoice.setStatus(Invoice.InvoiceStatus.GENERATED);

    boolean canRegenerate = invoice.canBeRegenerated();

    assertThat(canRegenerate);
  }

  @Test
  @DisplayName("Should allow regeneration when status is SENT")
  void shouldAllowRegenerationWhenStatusIsSent() {
    invoice.setStatus(Invoice.InvoiceStatus.SENT);

    boolean canRegenerate = invoice.canBeRegenerated();

    assertThat(canRegenerate);
  }

  @Test
  @DisplayName("Should not allow regeneration when status is FAILED")
  void shouldNotAllowRegenerationWhenStatusIsFailed() {
    invoice.setStatus(Invoice.InvoiceStatus.FAILED);

    boolean canRegenerate = invoice.canBeRegenerated();

    assertThat(canRegenerate);
  }

  @Test
  @DisplayName("Should mark as sent when status is GENERATED")
  void shouldMarkAsSentWhenStatusIsGenerated() {
    invoice.setStatus(Invoice.InvoiceStatus.GENERATED);

    invoice.markAsSent();

    assertThat(Invoice.InvoiceStatus.SENT, invoice.getStatus());
  }

  @Test
  @DisplayName("Should throw exception when marking as sent from non-GENERATED status")
  void shouldThrowExceptionWhenMarkingAsSentFromNonGeneratedStatus() {
    invoice.setStatus(Invoice.InvoiceStatus.SENT);

    IllegalStateException exception =
        assertThatThrownBy(IllegalStateException.class, () -> invoice.markAsSent());
    assertThat("Invoice cannot be marked as sent in current state: SENT", exception.getMessage());
  }

  @Test
  @DisplayName("Should throw exception when marking as sent from FAILED status")
  void shouldThrowExceptionWhenMarkingAsSentFromFailedStatus() {
    invoice.setStatus(Invoice.InvoiceStatus.FAILED);

    IllegalStateException exception =
        assertThatThrownBy(IllegalStateException.class, () -> invoice.markAsSent());
    assertThat("Invoice cannot be marked as sent in current state: FAILED", exception.getMessage());
  }

  @Test
  @DisplayName("Should mark as failed")
  void shouldMarkAsFailed() {
    String failureReason = "Generation failed";

    invoice.markAsFailed(failureReason);

    assertThat(Invoice.InvoiceStatus.FAILED, invoice.getStatus());
    assertThat(failureReason, invoice.getFailureReason());
  }

  @Test
  @DisplayName("Should have all expected status values")
  void shouldHaveAllExpectedStatusValues() {
    Invoice.InvoiceStatus[] statuses = Invoice.InvoiceStatus.values();

    assertThat(4, statuses.length);
    assertThat(java.util.Arrays.asList(statuses).contains(Invoice.InvoiceStatus.GENERATED));
    assertThat(java.util.Arrays.asList(statuses).contains(Invoice.InvoiceStatus.SENT));
    assertThat(java.util.Arrays.asList(statuses).contains(Invoice.InvoiceStatus.FAILED));
    assertThat(java.util.Arrays.asList(statuses).contains(Invoice.InvoiceStatus.DELETED));
  }

  @Test
  @DisplayName("Should have correct string representation")
  void shouldHaveCorrectStringRepresentation() {
    assertThat("GENERATED", Invoice.InvoiceStatus.GENERATED.name());
    assertThat("SENT", Invoice.InvoiceStatus.SENT.name());
    assertThat("FAILED", Invoice.InvoiceStatus.FAILED.name());
    assertThat("DELETED", Invoice.InvoiceStatus.DELETED.name());
  }

  @Test
  @DisplayName("Should set and get all properties correctly")
  void shouldSetAndGetAllPropertiesCorrectly() {
    Invoice testInvoice = new Invoice();
    Long id = 1L;
    Long orderId = 100L;
    Long customerId = 200L;
    String customerEmail = "test@example.com";
    BigDecimal totalAmount = new BigDecimal("999.99");
    String objectKey = "invoices/100/test-key.pdf";
    String objectUrl = "https://example.com/invoices/100/test-key.pdf";
    Invoice.InvoiceStatus status = Invoice.InvoiceStatus.GENERATED;
    LocalDateTime generatedAt = LocalDateTime.now();
    LocalDateTime createdAt = LocalDateTime.now().minusDays(1);
    LocalDateTime updatedAt = LocalDateTime.now();

    testInvoice.setId(id);
    testInvoice.setOrderId(orderId);
    testInvoice.setCustomerId(customerId);
    testInvoice.setCustomerEmail(customerEmail);
    testInvoice.setTotalAmount(totalAmount);
    testInvoice.setObjectKey(objectKey);
    testInvoice.setObjectUrl(objectUrl);
    testInvoice.setStatus(status);
    testInvoice.setGeneratedAt(generatedAt);
    testInvoice.setCreatedAt(createdAt);
    testInvoice.setUpdatedAt(updatedAt);

    assertThat(id, testInvoice.getId());
    assertThat(orderId, testInvoice.getOrderId());
    assertThat(customerId, testInvoice.getCustomerId());
    assertThat(customerEmail, testInvoice.getCustomerEmail());
    assertThat(totalAmount, testInvoice.getTotalAmount());
    assertThat(objectKey, testInvoice.getObjectKey());
    assertThat(objectUrl, testInvoice.getObjectUrl());
    assertThat(status, testInvoice.getStatus());
    assertThat(generatedAt, testInvoice.getGeneratedAt());
    assertThat(createdAt, testInvoice.getCreatedAt());
    assertThat(updatedAt, testInvoice.getUpdatedAt());
  }

  @Test
  @DisplayName("Should handle null values gracefully")
  void shouldHandleNullValuesGracefully() {
    Invoice testInvoice = new Invoice();

    assertDoesNotThrow(
        () -> {
          testInvoice.setId(null);
          testInvoice.setOrderId(null);
          testInvoice.setCustomerId(null);
          testInvoice.setCustomerEmail(null);
          testInvoice.setTotalAmount(null);
          testInvoice.setObjectKey(null);
          testInvoice.setObjectUrl(null);
          testInvoice.setStatus(null);
          testInvoice.setGeneratedAt(null);
          testInvoice.setCreatedAt(null);
          testInvoice.setUpdatedAt(null);
        });
  }

  @Test
  @DisplayName("Should handle zero amount")
  void shouldHandleZeroAmount() {
    BigDecimal zeroAmount = BigDecimal.ZERO;

    invoice.setTotalAmount(zeroAmount);

    assertThat(zeroAmount, invoice.getTotalAmount());
  }

  @Test
  @DisplayName("Should handle large amounts")
  void shouldHandleLargeAmounts() {
    BigDecimal largeAmount = new BigDecimal("999999.99");

    invoice.setTotalAmount(largeAmount);

    assertThat(largeAmount, invoice.getTotalAmount());
  }

  @Test
  @DisplayName("Should handle long email addresses")
  void shouldHandleLongEmailAddresses() {
    String longEmail =
        "very.long.email.address.that.might.exceed.normal.length@very.long.domain.name.com";

    invoice.setCustomerEmail(longEmail);

    assertThat(longEmail, invoice.getCustomerEmail());
  }
}
