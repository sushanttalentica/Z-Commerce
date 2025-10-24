package com.zcommerce.platform.invoice.service;

import static org.assertj.core.api.Assertions.*;
import static org.mockito.Mockito.*;

import com.ecommerce.productorder.domain.entity.Order;
import com.ecommerce.productorder.domain.entity.OrderItem;
import com.ecommerce.productorder.domain.entity.Product;
import com.ecommerce.productorder.invoice.service.impl.PdfGeneratorServiceImpl;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.junit.jupiter.MockitoExtension;

// JUnit tests for InvoiceGeneratorService interface.
@ExtendWith(MockitoExtension.class)
@org.mockito.junit.jupiter.MockitoSettings(strictness = org.mockito.quality.Strictness.LENIENT)
@DisplayName("InvoiceGeneratorService Tests")
public class InvoiceGeneratorServiceTest {

  @InjectMocks private PdfGeneratorServiceImpl pdfGeneratorService;

  private Order testOrder;
  private OrderItem testOrderItem;
  private Product testProduct;

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
  }

  @Test
  @DisplayName("Should generate invoice content")
  void generateInvoiceReturnsValidPdfContentForValidOrder() {
    byte[] result = pdfGeneratorService.generateInvoice(testOrder);

    assertThat(result);
    assertThat(result.length > 0);
  }

  @Test
  @DisplayName("Should return correct content type")
  void getContentTypeReturnsCorrectContentType() {
    String contentType = pdfGeneratorService.getContentType();

    assertThat("application/pdf", contentType);
  }

  @Test
  @DisplayName("Should throw exception for null order")
  void generateInvoiceThrowsExceptionForNullOrder() {
    assertThatThrownBy(IllegalArgumentException.class, () -> pdfGeneratorService.generateInvoice(null));
  }

  @Test
  @DisplayName("Should throw exception for order with zero total amount")
  void generateInvoiceThrowsExceptionForOrderWithZeroTotalAmount() {
    testOrder.setTotalAmount(BigDecimal.ZERO);

    assertThatThrownBy(IllegalArgumentException.class, () -> pdfGeneratorService.generateInvoice(testOrder));
  }

  @Test
  @DisplayName("Should throw exception for order with null total amount")
  void generateInvoiceThrowsExceptionForOrderWithNullTotalAmount() {
    testOrder.setTotalAmount(null);

    assertThatThrownBy(IllegalArgumentException.class, () -> pdfGeneratorService.generateInvoice(testOrder));
  }

  @Test
  @DisplayName("Should throw exception for order with null order items")
  void generateInvoiceThrowsExceptionForOrderWithNullOrderItems() {
    testOrder.setOrderItems(null);

    assertThatThrownBy(IllegalArgumentException.class, () -> pdfGeneratorService.generateInvoice(testOrder));
  }

  @Test
  @DisplayName("Should throw exception for order with empty order items")
  void generateInvoiceThrowsExceptionForOrderWithEmptyOrderItems() {
    testOrder.setOrderItems(new ArrayList<>());

    assertThatThrownBy(IllegalArgumentException.class, () -> pdfGeneratorService.generateInvoice(testOrder));
  }
}
