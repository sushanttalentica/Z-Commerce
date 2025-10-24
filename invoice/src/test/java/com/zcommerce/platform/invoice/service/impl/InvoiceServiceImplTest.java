package com.zcommerce.platform.invoice.service.impl;

import static org.assertj.core.api.Assertions.*;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;

import com.ecommerce.productorder.domain.entity.Order;
import com.ecommerce.productorder.invoice.domain.entity.Invoice;
import com.ecommerce.productorder.invoice.domain.repository.InvoiceRepository;
import com.ecommerce.productorder.invoice.service.InvoiceGeneratorService;
import com.ecommerce.productorder.invoice.service.ObjectStoreService;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.Optional;
import java.util.UUID;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

// JUnit tests for InvoiceServiceImpl class.
@ExtendWith(MockitoExtension.class)
@DisplayName("InvoiceServiceImpl Tests")
public class InvoiceServiceImplTest {
  @Mock private InvoiceRepository invoiceRepository;
  @Mock private InvoiceGeneratorService invoiceGeneratorService;
  @Mock private ObjectStoreService objectStoreService;
  @InjectMocks private InvoiceServiceImpl invoiceService;
  private Order testOrder;
  private Invoice testInvoice;
  private final String testObjectKey = "invoices/100/test-key.pdf";
  private final String testObjectUrl = "https://example.com/invoices/100/test-key.pdf";
  private final byte[] testContent = "test invoice content".getBytes();

  @BeforeEach
  void setUp() {
    testOrder = new Order();
    testOrder.setId(100L);
    testOrder.setOrderNumber(UUID.randomUUID().toString());
    testOrder.setCustomerId(200L);
    testOrder.setCustomerEmail("test@example.com");
    testOrder.setStatus(Order.OrderStatus.DELIVERED);
    testOrder.setTotalAmount(new BigDecimal("999.99"));
    testOrder.setShippingAddress("123 Test St, Test City, TC 12345");
    testOrder.setCreatedAt(LocalDateTime.now());
    testOrder.setUpdatedAt(LocalDateTime.now());

    testInvoice = new Invoice();
    testInvoice.setId(1L);
    testInvoice.setOrderId(100L);
    testInvoice.setCustomerId(200L);
    testInvoice.setCustomerEmail("test@example.com");
    testInvoice.setTotalAmount(new BigDecimal("999.99"));
    testInvoice.setObjectKey(testObjectKey);
    testInvoice.setObjectUrl(testObjectUrl);
    testInvoice.setStatus(Invoice.InvoiceStatus.GENERATED);
    testInvoice.setGeneratedAt(LocalDateTime.now());
    testInvoice.setCreatedAt(LocalDateTime.now());
    testInvoice.setUpdatedAt(LocalDateTime.now());

    lenient().when(invoiceGeneratorService.generateInvoice(any(Order.class))).thenReturn(testContent);
    lenient().when(invoiceGeneratorService.getContentType()).thenReturn("application/pdf");
    lenient().when(objectStoreService.uploadFile(anyString(), any(byte[].class), anyString())).thenReturn(testObjectUrl);
    lenient().when(invoiceRepository.save(any(Invoice.class))).thenReturn(testInvoice);
    lenient().when(invoiceRepository.findByOrderId(anyLong())).thenReturn(Optional.empty());
  }

  @Test
  @DisplayName("generateInvoice returns expected URL for new order")
  void generateInvoiceReturnsExpectedUrlForNewOrder() {
    when(invoiceRepository.findByOrderId(testOrder.getId())).thenReturn(Optional.empty());
    when(objectStoreService.uploadFile(anyString(), any(byte[].class), anyString())).thenReturn(testObjectUrl);
    when(invoiceRepository.save(any(Invoice.class))).thenReturn(testInvoice);

    Optional<String> result = invoiceService.generateInvoice(testOrder);

    assertThat(result).isPresent();
    assertThat(result.get()).isEqualTo(testObjectUrl);
    verify(invoiceGeneratorService, times(1)).generateInvoice(testOrder);
    verify(objectStoreService, times(1)).uploadFile(anyString(), any(byte[].class), anyString());
    verify(invoiceRepository, times(1)).save(any(Invoice.class));
  }

  @Test
  @DisplayName("generateInvoice returns existing URL when invoice already exists")
  void generateInvoiceReturnsExistingUrlWhenInvoiceAlreadyExists() {
    when(invoiceRepository.findByOrderId(testOrder.getId())).thenReturn(Optional.of(testInvoice));

    Optional<String> result = invoiceService.generateInvoice(testOrder);

    assertThat(result).isPresent();
    assertThat(result.get()).isEqualTo(testObjectUrl);
    verify(invoiceGeneratorService, never()).generateInvoice(any(Order.class));
    verify(objectStoreService, never()).uploadFile(anyString(), any(byte[].class), anyString());
    verify(invoiceRepository, never()).save(any(Invoice.class));
  }

  @Test
  @DisplayName("generateInvoice throws IllegalArgumentException when order is null")
  void generateInvoiceThrowsIllegalArgumentExceptionWhenOrderIsNull() {
    assertThatThrownBy(() -> invoiceService.generateInvoice(null))
        .isInstanceOf(IllegalArgumentException.class);
  }


  @Test
  @DisplayName("generateInvoice throws IllegalArgumentException when total amount is null")
  void generateInvoiceThrowsIllegalArgumentExceptionWhenTotalAmountIsNull() {
    testOrder.setTotalAmount(null);
    assertThatThrownBy(() -> invoiceService.generateInvoice(testOrder))
        .isInstanceOf(IllegalArgumentException.class);
  }

  @Test
  @DisplayName("generateInvoice throws IllegalArgumentException when total amount is zero or negative")
  void generateInvoiceThrowsIllegalArgumentExceptionWhenTotalAmountIsZeroOrNegative() {
    testOrder.setTotalAmount(BigDecimal.ZERO);
    assertThatThrownBy(() -> invoiceService.generateInvoice(testOrder))
        .isInstanceOf(IllegalArgumentException.class);
  }



  @Test
  @DisplayName("getInvoiceUrl returns expected URL for valid order ID")
  void getInvoiceUrlReturnsExpectedUrlForValidOrderId() {
    when(invoiceRepository.findByOrderId(testOrder.getId())).thenReturn(Optional.of(testInvoice));

    Optional<String> result = invoiceService.getInvoiceUrl(testOrder.getId());

    assertThat(result).isPresent();
    assertThat(result.get()).isEqualTo(testObjectUrl);
    verify(invoiceRepository, times(1)).findByOrderId(testOrder.getId());
  }

  @Test
  @DisplayName("getInvoiceUrl returns empty when invoice not found")
  void getInvoiceUrlReturnsEmptyWhenInvoiceNotFound() {
    when(invoiceRepository.findByOrderId(testOrder.getId())).thenReturn(Optional.empty());

    Optional<String> result = invoiceService.getInvoiceUrl(testOrder.getId());

    assertThat(result).isNotPresent();
    verify(invoiceRepository, times(1)).findByOrderId(testOrder.getId());
  }

  @Test
  @DisplayName("getInvoiceUrl throws IllegalArgumentException when order ID is null")
  void getInvoiceUrlThrowsIllegalArgumentExceptionWhenOrderIdIsNull() {
    assertThatThrownBy(() -> invoiceService.getInvoiceUrl(null))
        .isInstanceOf(IllegalArgumentException.class);
  }

  @Test
  @DisplayName("deleteInvoice returns true when invoice is successfully deleted")
  void deleteInvoiceReturnsTrueWhenInvoiceIsSuccessfullyDeleted() {
    when(invoiceRepository.findByOrderId(testOrder.getId())).thenReturn(Optional.of(testInvoice));
    when(objectStoreService.deleteFile(testObjectKey)).thenReturn(true);
    doNothing().when(invoiceRepository).delete(testInvoice);

    boolean result = invoiceService.deleteInvoice(testOrder.getId());

    assertThat(result).isTrue();
    verify(invoiceRepository, times(1)).findByOrderId(testOrder.getId());
    verify(objectStoreService, times(1)).deleteFile(testObjectKey);
    verify(invoiceRepository, times(1)).delete(testInvoice);
  }

  @Test
  @DisplayName("deleteInvoice returns false when invoice not found")
  void deleteInvoiceReturnsFalseWhenInvoiceNotFound() {
    when(invoiceRepository.findByOrderId(testOrder.getId())).thenReturn(Optional.empty());

    boolean result = invoiceService.deleteInvoice(testOrder.getId());

    assertThat(result).isFalse();
    verify(invoiceRepository, times(1)).findByOrderId(testOrder.getId());
    verify(objectStoreService, never()).deleteFile(anyString());
    verify(invoiceRepository, never()).delete(any(Invoice.class));
  }


  @Test
  @DisplayName("deleteInvoice throws IllegalArgumentException when order ID is null")
  void deleteInvoiceThrowsIllegalArgumentExceptionWhenOrderIdIsNull() {
    assertThatThrownBy(() -> invoiceService.deleteInvoice(null))
        .isInstanceOf(IllegalArgumentException.class);
  }

  @Test
  @DisplayName("invoiceExists returns true when invoice exists")
  void invoiceExistsReturnsTrueWhenInvoiceExists() {
    when(invoiceRepository.findByOrderId(testOrder.getId())).thenReturn(Optional.of(testInvoice));

    boolean result = invoiceService.invoiceExists(testOrder.getId());

    assertThat(result).isTrue();
    verify(invoiceRepository, times(1)).findByOrderId(testOrder.getId());
  }

  @Test
  @DisplayName("invoiceExists returns false when invoice does not exist")
  void invoiceExistsReturnsFalseWhenInvoiceDoesNotExist() {
    when(invoiceRepository.findByOrderId(testOrder.getId())).thenReturn(Optional.empty());

    boolean result = invoiceService.invoiceExists(testOrder.getId());

    assertThat(result).isFalse();
    verify(invoiceRepository, times(1)).findByOrderId(testOrder.getId());
  }

  @Test
  @DisplayName("invoiceExists throws IllegalArgumentException when order ID is null")
  void invoiceExistsThrowsIllegalArgumentExceptionWhenOrderIdIsNull() {
    assertThatThrownBy(() -> invoiceService.invoiceExists(null))
        .isInstanceOf(IllegalArgumentException.class);
  }

  @Test
  @DisplayName("complete invoice lifecycle operations work correctly")
  void completeInvoiceLifecycleOperationsWorkCorrectly() {
    // Generate invoice
    when(invoiceRepository.findByOrderId(testOrder.getId())).thenReturn(Optional.empty());
    when(objectStoreService.uploadFile(anyString(), any(byte[].class), anyString())).thenReturn(testObjectUrl);
    when(invoiceRepository.save(any(Invoice.class))).thenReturn(testInvoice);

    Optional<String> generateResult = invoiceService.generateInvoice(testOrder);
    assertThat(generateResult).isPresent();
    assertThat(generateResult.get()).isEqualTo(testObjectUrl);

    // Check if invoice exists
    when(invoiceRepository.findByOrderId(testOrder.getId())).thenReturn(Optional.of(testInvoice));
    boolean existsResult = invoiceService.invoiceExists(testOrder.getId());
    assertThat(existsResult).isTrue();

    // Get invoice URL
    Optional<String> urlResult = invoiceService.getInvoiceUrl(testOrder.getId());
    assertThat(urlResult).isPresent();
    assertThat(urlResult.get()).isEqualTo(testObjectUrl);

    // Delete invoice
    when(objectStoreService.deleteFile(testObjectKey)).thenReturn(true);
    doNothing().when(invoiceRepository).delete(testInvoice);
    boolean deleteResult = invoiceService.deleteInvoice(testOrder.getId());
    assertThat(deleteResult).isTrue();
  }

  @Test
  @DisplayName("generateInvoice handles order with very large total amount")
  void generateInvoiceHandlesOrderWithVeryLargeTotalAmount() {
    testOrder.setTotalAmount(new BigDecimal("999999.99"));

    when(invoiceRepository.findByOrderId(testOrder.getId())).thenReturn(Optional.empty());
    when(objectStoreService.uploadFile(anyString(), any(byte[].class), anyString())).thenReturn(testObjectUrl);
    when(invoiceRepository.save(any(Invoice.class))).thenReturn(testInvoice);

    Optional<String> result = invoiceService.generateInvoice(testOrder);

    assertThat(result).isPresent();
    assertThat(result.get()).isEqualTo(testObjectUrl);
  }

  @Test
  @DisplayName("generateInvoice handles order with special characters in email")
  void generateInvoiceHandlesOrderWithSpecialCharactersInEmail() {
    testOrder.setCustomerEmail("test+special@example-domain.co.uk");

    when(invoiceRepository.findByOrderId(testOrder.getId())).thenReturn(Optional.empty());
    when(objectStoreService.uploadFile(anyString(), any(byte[].class), anyString())).thenReturn(testObjectUrl);
    when(invoiceRepository.save(any(Invoice.class))).thenReturn(testInvoice);

    Optional<String> result = invoiceService.generateInvoice(testOrder);

    assertThat(result).isPresent();
    assertThat(result.get()).isEqualTo(testObjectUrl);
  }
}