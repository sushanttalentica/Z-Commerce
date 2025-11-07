package com.zcommerce.platform.invoice.service;

import static org.assertj.core.api.Assertions.*;
import static org.mockito.Mockito.*;

import com.zcommerce.platform.domain.entity.Order;
import com.zcommerce.platform.domain.entity.OrderItem;
import com.zcommerce.platform.domain.entity.Product;
import com.zcommerce.platform.invoice.domain.entity.Invoice;
import com.zcommerce.platform.invoice.domain.repository.InvoiceRepository;
import com.zcommerce.platform.invoice.service.impl.InvoiceServiceImpl;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

// JUnit tests for InvoiceService interface.
@ExtendWith(MockitoExtension.class)
@DisplayName("InvoiceService Tests")
public class InvoiceServiceTest {

  @Mock private InvoiceRepository invoiceRepository;
  @Mock private InvoiceGeneratorService invoiceGeneratorService;
  @Mock private ObjectStoreService objectStoreService;

  @InjectMocks private InvoiceServiceImpl invoiceService;

  private Order testOrder;
  private OrderItem testOrderItem;
  private Product testProduct;
  private Invoice testInvoice;

  @BeforeEach
  void setUp() {
    testProduct = new Product();
    testProduct.setId(1L);
    testProduct.setName("Test Product");
    testProduct.setPrice(new BigDecimal("99.99"));

    testOrderItem = new OrderItem();
    testOrderItem.setId(1L);
    testOrderItem.setProduct(testProduct);
    testOrderItem.setQuantity(2);
    testOrderItem.setUnitPrice(new BigDecimal("99.99"));

    testOrder = new Order();
    testOrder.setId(100L);
    testOrder.setCustomerId(200L);
    testOrder.setCustomerEmail("test@example.com");
    testOrder.setTotalAmount(new BigDecimal("199.98"));
    testOrder.setStatus(Order.OrderStatus.DELIVERED);
    testOrder.setCreatedAt(LocalDateTime.now());

    List<OrderItem> orderItems = new ArrayList<>();
    orderItems.add(testOrderItem);
    testOrder.setOrderItems(orderItems);

    testInvoice = new Invoice();
    testInvoice.setId(1L);
    testInvoice.setOrderId(100L);
    testInvoice.setCustomerId(200L);
    testInvoice.setCustomerEmail("test@example.com");
    testInvoice.setTotalAmount(new BigDecimal("199.98"));
    testInvoice.setObjectKey("invoices/100/test-invoice.pdf");
    testInvoice.setObjectUrl("https://example.com/invoices/100/test-invoice.pdf");
    testInvoice.setStatus(Invoice.InvoiceStatus.GENERATED);
    testInvoice.setGeneratedAt(LocalDateTime.now());
    testInvoice.setCreatedAt(LocalDateTime.now());
    testInvoice.setUpdatedAt(LocalDateTime.now());
  }

  @Test
  @DisplayName("generateInvoice returns invoice URL when successful")
  void generateInvoiceReturnsInvoiceUrlWhenSuccessful() {
    when(invoiceRepository.findByOrderId(100L)).thenReturn(Optional.empty());
    when(invoiceGeneratorService.generateInvoice(testOrder)).thenReturn(new byte[]{1, 2, 3});
    when(invoiceGeneratorService.getContentType()).thenReturn("application/pdf");
    when(objectStoreService.uploadFile(anyString(), any(byte[].class), anyString()))
        .thenReturn("https://example.com/invoices/100/test-invoice.pdf");
    when(invoiceRepository.save(any(Invoice.class))).thenReturn(testInvoice);

    Optional<String> result = invoiceService.generateInvoice(testOrder);

    assertThat(result.isPresent()).isTrue();
    assertThat(result.get()).isEqualTo("https://example.com/invoices/100/test-invoice.pdf");
    verify(invoiceRepository).save(any(Invoice.class));
  }

  @Test
  @DisplayName("generateInvoice returns existing invoice URL when invoice already exists")
  void generateInvoiceReturnsExistingInvoiceUrlWhenInvoiceAlreadyExists() {
    when(invoiceRepository.findByOrderId(100L)).thenReturn(Optional.of(testInvoice));

    Optional<String> result = invoiceService.generateInvoice(testOrder);

    assertThat(result.isPresent()).isTrue();
    assertThat(result.get()).isEqualTo("https://example.com/invoices/100/test-invoice.pdf");
    verify(invoiceRepository, never()).save(any(Invoice.class));
  }

  @Test
  @DisplayName("getInvoiceUrl returns URL when successful")
  void getInvoiceUrlReturnsUrlWhenSuccessful() {
    when(invoiceRepository.findByOrderId(100L)).thenReturn(Optional.of(testInvoice));

    Optional<String> result = invoiceService.getInvoiceUrl(100L);

    assertThat(result.isPresent()).isTrue();
    assertThat(result.get()).isEqualTo("https://example.com/invoices/100/test-invoice.pdf");
  }

  @Test
  @DisplayName("getInvoiceUrl returns empty when invoice not found")
  void getInvoiceUrlReturnsEmptyWhenInvoiceNotFound() {
    when(invoiceRepository.findByOrderId(100L)).thenReturn(Optional.empty());

    Optional<String> result = invoiceService.getInvoiceUrl(100L);

    assertThat(result.isPresent()).isFalse();
  }

  @Test
  @DisplayName("deleteInvoice returns true when successful")
  void deleteInvoiceReturnsTrueWhenSuccessful() {
    when(invoiceRepository.findByOrderId(100L)).thenReturn(Optional.of(testInvoice));
    when(objectStoreService.deleteFile("invoices/100/test-invoice.pdf")).thenReturn(true);
    doNothing().when(invoiceRepository).delete(testInvoice);

    boolean result = invoiceService.deleteInvoice(100L);

    assertThat(result).isTrue();
    verify(invoiceRepository).delete(testInvoice);
    verify(objectStoreService).deleteFile("invoices/100/test-invoice.pdf");
  }

  @Test
  @DisplayName("deleteInvoice returns false when invoice not found")
  void deleteInvoiceReturnsFalseWhenInvoiceNotFound() {
    when(invoiceRepository.findByOrderId(100L)).thenReturn(Optional.empty());

    boolean result = invoiceService.deleteInvoice(100L);

    assertThat(result).isFalse();
    verify(invoiceRepository, never()).delete(any(Invoice.class));
  }

  @Test
  @DisplayName("invoiceExists returns true when invoice exists")
  void invoiceExistsReturnsTrueWhenInvoiceExists() {
    when(invoiceRepository.findByOrderId(100L)).thenReturn(Optional.of(testInvoice));

    boolean result = invoiceService.invoiceExists(100L);

    assertThat(result).isTrue();
  }

  @Test
  @DisplayName("invoiceExists returns false when invoice does not exist")
  void invoiceExistsReturnsFalseWhenInvoiceDoesNotExist() {
    when(invoiceRepository.findByOrderId(100L)).thenReturn(Optional.empty());

    boolean result = invoiceService.invoiceExists(100L);

    assertThat(result).isFalse();
  }

  @Test
  @DisplayName("getInvoiceUrl throws exception for null order ID")
  void getInvoiceUrlThrowsExceptionForNullOrderId() {
    assertThatThrownBy(() -> invoiceService.getInvoiceUrl(null))
        .isInstanceOf(IllegalArgumentException.class);
  }

  @Test
  @DisplayName("deleteInvoice throws exception for null order ID")
  void deleteInvoiceThrowsExceptionForNullOrderId() {
    assertThatThrownBy(() -> invoiceService.deleteInvoice(null))
        .isInstanceOf(IllegalArgumentException.class);
  }

  @Test
  @DisplayName("invoiceExists throws exception for null order ID")
  void invoiceExistsThrowsExceptionForNullOrderId() {
    assertThatThrownBy(() -> invoiceService.invoiceExists(null))
        .isInstanceOf(IllegalArgumentException.class);
  }
}
