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
  @DisplayName("Invoice default constructor creates empty invoice")
  void invoiceDefaultConstructorCreatesEmptyInvoice() {
    Invoice invoice = new Invoice();

    assertThat(invoice).isNotNull();
    assertThat(invoice.getId()).isNull();
    assertThat(invoice.getOrderId()).isNull();
    assertThat(invoice.getCustomerId()).isNull();
    assertThat(invoice.getCustomerEmail()).isNull();
    assertThat(invoice.getTotalAmount()).isNull();
    assertThat(invoice.getObjectKey()).isNull();
    assertThat(invoice.getObjectUrl()).isNull();
    assertThat(invoice.getStatus()).isEqualTo(Invoice.InvoiceStatus.GENERATED);
    assertThat(invoice.getGeneratedAt()).isNull();
    assertThat(invoice.getCreatedAt()).isNull();
    assertThat(invoice.getUpdatedAt()).isNull();
  }

  @Test
  @DisplayName("canBeDownloaded returns true when status is GENERATED")
  void canBeDownloadedReturnsTrueWhenStatusIsGenerated() {
    invoice.setStatus(Invoice.InvoiceStatus.GENERATED);

    boolean canDownload = invoice.canBeDownloaded();

    assertThat(canDownload).isTrue();
  }

  @Test
  @DisplayName("canBeDownloaded returns true when status is SENT")
  void canBeDownloadedReturnsTrueWhenStatusIsSent() {
    invoice.setStatus(Invoice.InvoiceStatus.SENT);

    boolean canDownload = invoice.canBeDownloaded();

    assertThat(canDownload).isTrue();
  }

  @Test
  @DisplayName("canBeDownloaded returns false when status is FAILED")
  void canBeDownloadedReturnsFalseWhenStatusIsFailed() {
    invoice.setStatus(Invoice.InvoiceStatus.FAILED);

    boolean canDownload = invoice.canBeDownloaded();

    assertThat(canDownload).isFalse();
  }

  @Test
  @DisplayName("canBeDownloaded returns false when status is DELETED")
  void canBeDownloadedReturnsFalseWhenStatusIsDeleted() {
    invoice.setStatus(Invoice.InvoiceStatus.DELETED);

    boolean canDownload = invoice.canBeDownloaded();

    assertThat(canDownload).isFalse();
  }

  @Test
  @DisplayName("canBeRegenerated returns true when status is GENERATED")
  void canBeRegeneratedReturnsTrueWhenStatusIsGenerated() {
    invoice.setStatus(Invoice.InvoiceStatus.GENERATED);

    boolean canRegenerate = invoice.canBeRegenerated();

    assertThat(canRegenerate).isTrue();
  }

  @Test
  @DisplayName("canBeRegenerated returns true when status is SENT")
  void canBeRegeneratedReturnsTrueWhenStatusIsSent() {
    invoice.setStatus(Invoice.InvoiceStatus.SENT);

    boolean canRegenerate = invoice.canBeRegenerated();

    assertThat(canRegenerate).isTrue();
  }

  @Test
  @DisplayName("canBeRegenerated returns false when status is FAILED")
  void canBeRegeneratedReturnsFalseWhenStatusIsFailed() {
    invoice.setStatus(Invoice.InvoiceStatus.FAILED);

    boolean canRegenerate = invoice.canBeRegenerated();

    assertThat(canRegenerate).isFalse();
  }

  @Test
  @DisplayName("markAsSent updates status to SENT when status is GENERATED")
  void markAsSentUpdatesStatusToSentWhenStatusIsGenerated() {
    invoice.setStatus(Invoice.InvoiceStatus.GENERATED);

    invoice.markAsSent();

    assertThat(invoice.getStatus()).isEqualTo(Invoice.InvoiceStatus.SENT);
  }

  @Test
  @DisplayName("markAsSent throws IllegalStateException when status is not GENERATED")
  void markAsSentThrowsIllegalStateExceptionWhenStatusIsNotGenerated() {
    invoice.setStatus(Invoice.InvoiceStatus.SENT);

    assertThatThrownBy(() -> invoice.markAsSent())
        .isInstanceOf(IllegalStateException.class)
        .hasMessage("Invoice cannot be marked as sent in current state: SENT");
  }

  @Test
  @DisplayName("markAsSent throws IllegalStateException when status is FAILED")
  void markAsSentThrowsIllegalStateExceptionWhenStatusIsFailed() {
    invoice.setStatus(Invoice.InvoiceStatus.FAILED);

    assertThatThrownBy(() -> invoice.markAsSent())
        .isInstanceOf(IllegalStateException.class)
        .hasMessage("Invoice cannot be marked as sent in current state: FAILED");
  }

  @Test
  @DisplayName("markAsFailed updates status to FAILED and sets failure reason")
  void markAsFailedUpdatesStatusToFailedAndSetsFailureReason() {
    String failureReason = "Generation failed";

    invoice.markAsFailed(failureReason);

    assertThat(invoice.getStatus()).isEqualTo(Invoice.InvoiceStatus.FAILED);
    assertThat(invoice.getFailureReason()).isEqualTo(failureReason);
  }

  @Test
  @DisplayName("InvoiceStatus enum has all expected values")
  void invoiceStatusEnumHasAllExpectedValues() {
    Invoice.InvoiceStatus[] statuses = Invoice.InvoiceStatus.values();

    assertThat(statuses.length).isEqualTo(4);
    assertThat(java.util.Arrays.asList(statuses)).contains(Invoice.InvoiceStatus.GENERATED);
    assertThat(java.util.Arrays.asList(statuses)).contains(Invoice.InvoiceStatus.SENT);
    assertThat(java.util.Arrays.asList(statuses)).contains(Invoice.InvoiceStatus.FAILED);
    assertThat(java.util.Arrays.asList(statuses)).contains(Invoice.InvoiceStatus.DELETED);
  }

  @Test
  @DisplayName("InvoiceStatus enum has correct string representation")
  void invoiceStatusEnumHasCorrectStringRepresentation() {
    assertThat(Invoice.InvoiceStatus.GENERATED.name()).isEqualTo("GENERATED");
    assertThat(Invoice.InvoiceStatus.SENT.name()).isEqualTo("SENT");
    assertThat(Invoice.InvoiceStatus.FAILED.name()).isEqualTo("FAILED");
    assertThat(Invoice.InvoiceStatus.DELETED.name()).isEqualTo("DELETED");
  }

  @Test
  @DisplayName("Invoice setters and getters work correctly for all properties")
  void invoiceSettersAndGettersWorkCorrectlyForAllProperties() {
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

    assertThat(testInvoice.getId()).isEqualTo(id);
    assertThat(testInvoice.getOrderId()).isEqualTo(orderId);
    assertThat(testInvoice.getCustomerId()).isEqualTo(customerId);
    assertThat(testInvoice.getCustomerEmail()).isEqualTo(customerEmail);
    assertThat(testInvoice.getTotalAmount()).isEqualTo(totalAmount);
    assertThat(testInvoice.getObjectKey()).isEqualTo(objectKey);
    assertThat(testInvoice.getObjectUrl()).isEqualTo(objectUrl);
    assertThat(testInvoice.getStatus()).isEqualTo(status);
    assertThat(testInvoice.getGeneratedAt()).isEqualTo(generatedAt);
    assertThat(testInvoice.getCreatedAt()).isEqualTo(createdAt);
    assertThat(testInvoice.getUpdatedAt()).isEqualTo(updatedAt);
  }

  @Test
  @DisplayName("Invoice handles null values gracefully")
  void invoiceHandlesNullValuesGracefully() {
    Invoice testInvoice = new Invoice();

    assertThatCode(
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
        }).doesNotThrowAnyException();
  }

  @Test
  @DisplayName("Invoice handles zero amount correctly")
  void invoiceHandlesZeroAmountCorrectly() {
    BigDecimal zeroAmount = BigDecimal.ZERO;

    invoice.setTotalAmount(zeroAmount);

    assertThat(invoice.getTotalAmount()).isEqualTo(zeroAmount);
  }

  @Test
  @DisplayName("Invoice handles large amounts correctly")
  void invoiceHandlesLargeAmountsCorrectly() {
    BigDecimal largeAmount = new BigDecimal("999999.99");

    invoice.setTotalAmount(largeAmount);

    assertThat(invoice.getTotalAmount()).isEqualTo(largeAmount);
  }

  @Test
  @DisplayName("Invoice handles long email addresses correctly")
  void invoiceHandlesLongEmailAddressesCorrectly() {
    String longEmail =
        "very.long.email.address.that.might.exceed.normal.length@very.long.domain.name.com";

    invoice.setCustomerEmail(longEmail);

    assertThat(invoice.getCustomerEmail()).isEqualTo(longEmail);
  }
}
