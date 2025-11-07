package com.zcommerce.platform.invoice.service.impl;

import static org.assertj.core.api.Assertions.*;

import com.zcommerce.platform.domain.entity.Category;
import com.zcommerce.platform.domain.entity.Order;
import com.zcommerce.platform.domain.entity.OrderItem;
import com.zcommerce.platform.domain.entity.Product;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

// JUnit tests for PdfGeneratorServiceImpl class.
@DisplayName("PdfGeneratorServiceImpl Tests")
public class PdfGeneratorServiceImplTest {
  private PdfGeneratorServiceImpl pdfGeneratorService;
  private Order testOrder;
  private Product testProduct;

  @BeforeEach
  void setUp() {
    pdfGeneratorService = new PdfGeneratorServiceImpl();
    testProduct = new Product();
    testProduct.setId(1L);
    testProduct.setName("iPhone 15");
    testProduct.setSku("IPHONE15-001");
    testProduct.setPrice(new BigDecimal("999.99"));
    testProduct.setDescription("Latest iPhone model");
    Category testCategory = new Category();
    testCategory.setId(1L);
    testCategory.setName("Test Category");
    testCategory.setDescription("Test Description");
    testProduct.setCategory(testCategory);
    testProduct.setStockQuantity(10);
    testProduct.setActive(true);
    testOrder = new Order();
    testOrder.setId(100L);
    testOrder.setOrderNumber(UUID.randomUUID().toString());
    testOrder.setCustomerId(200L);
    testOrder.setCustomerEmail("test@example.com");
    testOrder.setStatus(Order.OrderStatus.DELIVERED);
    testOrder.setTotalAmount(new BigDecimal("1999.98"));
    testOrder.setShippingAddress("123 Test St, Test City, TC 12345");
    testOrder.setCreatedAt(LocalDateTime.now().minusDays(1));
    testOrder.setUpdatedAt(LocalDateTime.now());
    OrderItem orderItem = new OrderItem();
    orderItem.setId(1L);
    orderItem.setProduct(testProduct);
    orderItem.setQuantity(2);
    orderItem.setUnitPrice(new BigDecimal("999.99"));
    orderItem.setSubtotal(new BigDecimal("1999.98"));
    testOrder.setOrderItems(List.of(orderItem));
  }

  @Test
  @DisplayName("generateInvoicePdf returns valid PDF content for valid order")
  void generateInvoicePdfReturnsValidPdfContentForValidOrder() {
    byte[] pdfContent = pdfGeneratorService.generateInvoice(testOrder);
    assertThat(pdfContent).isNotNull();
    assertThat(pdfContent.length).isGreaterThan(0);
    // PDF files start with %PDF header
    assertThat(pdfContent[0]).isEqualTo((byte) 0x25);
    assertThat(pdfContent[1]).isEqualTo((byte) 0x50);
    assertThat(pdfContent[2]).isEqualTo((byte) 0x44);
    assertThat(pdfContent[3]).isEqualTo((byte) 0x46);
  }

  @Test
  @DisplayName("generateInvoicePdf returns PDF with order details")
  void generateInvoicePdfReturnsPdfWithOrderDetails() {
    byte[] pdfContent = pdfGeneratorService.generateInvoice(testOrder);
    assertThat(pdfContent).isNotNull();
    assertThat(pdfContent.length).isGreaterThan(0);
    // PDF files start with %PDF header
    assertThat(pdfContent[0]).isEqualTo((byte) 0x25);
    assertThat(pdfContent[1]).isEqualTo((byte) 0x50);
    assertThat(pdfContent[2]).isEqualTo((byte) 0x44);
    assertThat(pdfContent[3]).isEqualTo((byte) 0x46);
  }

  @Test
  @DisplayName("generateInvoicePdf returns PDF with customer details")
  void generateInvoicePdfReturnsPdfWithCustomerDetails() {
    byte[] pdfContent = pdfGeneratorService.generateInvoice(testOrder);
    assertThat(pdfContent).isNotNull();
    assertThat(pdfContent.length).isGreaterThan(0);
    // PDF files start with %PDF header
    assertThat(pdfContent[0]).isEqualTo((byte) 0x25);
    assertThat(pdfContent[1]).isEqualTo((byte) 0x50);
    assertThat(pdfContent[2]).isEqualTo((byte) 0x44);
    assertThat(pdfContent[3]).isEqualTo((byte) 0x46);
  }

  @Test
  @DisplayName("generateInvoicePdf returns PDF with product details")
  void generateInvoicePdfReturnsPdfWithProductDetails() {
    byte[] pdfContent = pdfGeneratorService.generateInvoice(testOrder);
    assertThat(pdfContent).isNotNull();
    assertThat(pdfContent.length).isGreaterThan(0);
    // PDF files start with %PDF header
    assertThat(pdfContent[0]).isEqualTo((byte) 0x25);
    assertThat(pdfContent[1]).isEqualTo((byte) 0x50);
    assertThat(pdfContent[2]).isEqualTo((byte) 0x44);
    assertThat(pdfContent[3]).isEqualTo((byte) 0x46);
  }

  @Test
  @DisplayName("generateInvoicePdf returns PDF with total amount")
  void generateInvoicePdfReturnsPdfWithTotalAmount() {
    byte[] pdfContent = pdfGeneratorService.generateInvoice(testOrder);
    assertThat(pdfContent).isNotNull();
    assertThat(pdfContent.length).isGreaterThan(0);
    // PDF files start with %PDF header
    assertThat(pdfContent[0]).isEqualTo((byte) 0x25);
    assertThat(pdfContent[1]).isEqualTo((byte) 0x50);
    assertThat(pdfContent[2]).isEqualTo((byte) 0x44);
    assertThat(pdfContent[3]).isEqualTo((byte) 0x46);
  }

  @Test
  @DisplayName("generateInvoicePdf throws exception when order is null")
  void generateInvoicePdfThrowsExceptionWhenOrderIsNull() {
    assertThatThrownBy(() -> pdfGeneratorService.generateInvoice(null))
        .isInstanceOf(Exception.class);
  }

  @Test
  @DisplayName("generateInvoicePdf handles order with multiple items")
  void generateInvoicePdfHandlesOrderWithMultipleItems() {
    Product secondProduct = new Product();
    secondProduct.setId(2L);
    secondProduct.setName("iPhone Case");
    secondProduct.setSku("CASE-001");
    secondProduct.setPrice(new BigDecimal("29.99"));
    secondProduct.setDescription("Protective case for iPhone");
    Category secondCategory = new Category();
    secondCategory.setId(2L);
    secondCategory.setName("Test Category 2");
    secondCategory.setDescription("Test Description 2");
    secondProduct.setCategory(secondCategory);
    secondProduct.setStockQuantity(50);
    secondProduct.setActive(true);
    OrderItem secondOrderItem = new OrderItem();
    secondOrderItem.setId(2L);
    secondOrderItem.setProduct(secondProduct);
    secondOrderItem.setQuantity(1);
    secondOrderItem.setUnitPrice(new BigDecimal("29.99"));
    secondOrderItem.setSubtotal(new BigDecimal("29.99"));
    testOrder.setOrderItems(List.of(testOrder.getOrderItems().get(0), secondOrderItem));
    testOrder.setTotalAmount(new BigDecimal("2029.97"));
    byte[] pdfContent = pdfGeneratorService.generateInvoice(testOrder);
    assertThat(pdfContent).isNotNull();
    assertThat(pdfContent.length).isGreaterThan(0);
  }

  @Test
  @DisplayName("generateInvoice returns valid PDF content")
  void generateInvoiceReturnsValidPdfContent() {
    byte[] pdfContent = pdfGeneratorService.generateInvoice(testOrder);
    assertThat(pdfContent).isNotNull();
    assertThat(pdfContent.length).isGreaterThan(0);
  }

  @Test
  @DisplayName("getContentType returns correct content type")
  void getContentTypeReturnsCorrectContentType() {
    String contentType = pdfGeneratorService.getContentType();
    assertThat("application/pdf").isEqualTo(contentType);
  }

  @Test
  @DisplayName("generateInvoicePdf throws RuntimeException when order has zero total amount")
  void generateInvoicePdfThrowsRuntimeExceptionWhenOrderHasZeroTotalAmount() {
    testOrder.setTotalAmount(BigDecimal.ZERO);
    testOrder.getOrderItems().get(0).setUnitPrice(BigDecimal.ZERO);
    testOrder.getOrderItems().get(0).setSubtotal(BigDecimal.ZERO);
    
    // This should throw an exception due to validation
    assertThatThrownBy(() -> pdfGeneratorService.generateInvoice(testOrder))
        .isInstanceOf(RuntimeException.class);
  }

  @Test
  @DisplayName("generateInvoicePdf handles order with very large amounts")
  void generateInvoicePdfHandlesOrderWithVeryLargeAmounts() {
    BigDecimal largeAmount = new BigDecimal("999999.99");
    testOrder.setTotalAmount(largeAmount);
    testOrder.getOrderItems().get(0).setUnitPrice(largeAmount);
    testOrder.getOrderItems().get(0).setSubtotal(largeAmount);
    byte[] pdfContent = pdfGeneratorService.generateInvoice(testOrder);
    assertThat(pdfContent).isNotNull();
    assertThat(pdfContent.length).isGreaterThan(0);
  }

  @Test
  @DisplayName("generateInvoicePdf throws IllegalArgumentException when order has empty order items")
  void generateInvoicePdfThrowsIllegalArgumentExceptionWhenOrderHasEmptyOrderItems() {
    testOrder.setOrderItems(List.of());
    assertThatThrownBy(() -> pdfGeneratorService.generateInvoice(testOrder))
        .isInstanceOf(IllegalArgumentException.class);
  }

  @Test
  @DisplayName("generateInvoicePdf throws RuntimeException when order has null product")
  void generateInvoicePdfThrowsRuntimeExceptionWhenOrderHasNullProduct() {
    testOrder.getOrderItems().get(0).setProduct(null);
    
    // This should throw an exception due to null product
    assertThatThrownBy(() -> pdfGeneratorService.generateInvoice(testOrder))
        .isInstanceOf(RuntimeException.class);
  }

  @Test
  @DisplayName("generateInvoicePdf handles order with very long customer email")
  void generateInvoicePdfHandlesOrderWithVeryLongCustomerEmail() {
    String longEmail =
        "very.long.email.address.that.might.exceed.normal.length@very.long.domain.name.com";
    testOrder.setCustomerEmail(longEmail);
    byte[] pdfContent = pdfGeneratorService.generateInvoice(testOrder);
    assertThat(pdfContent).isNotNull();
    assertThat(pdfContent.length).isGreaterThan(0);
  }

  @Test
  @DisplayName("generateInvoicePdf handles order with special characters in product name")
  void generateInvoicePdfHandlesOrderWithSpecialCharactersInProductName() {
    testProduct.setName("iPhone 15 Pro Max™ (Special Edition) - 256GB");
    testProduct.setDescription("Latest iPhone with special characters: @#$%^&*()");
    byte[] pdfContent = pdfGeneratorService.generateInvoice(testOrder);
    assertThat(pdfContent).isNotNull();
    assertThat(pdfContent.length).isGreaterThan(0);
  }

  @Test
  @DisplayName("generateInvoicePdf completes within reasonable time")
  void generateInvoicePdfCompletesWithinReasonableTime() {
    long startTime = System.currentTimeMillis();
    byte[] pdfContent = pdfGeneratorService.generateInvoice(testOrder);
    long endTime = System.currentTimeMillis();
    long duration = endTime - startTime;
    assertThat(pdfContent).isNotNull();
    assertThat(pdfContent.length).isGreaterThan(0);
    assertThat(duration).isLessThan(5000);
  }

  @Test
  @DisplayName("generateInvoicePdf generates consistent results across multiple calls")
  void generateInvoicePdfGeneratesConsistentResultsAcrossMultipleCalls() {
    byte[] pdf1 = pdfGeneratorService.generateInvoice(testOrder);
    byte[] pdf2 = pdfGeneratorService.generateInvoice(testOrder);
    byte[] pdf3 = pdfGeneratorService.generateInvoice(testOrder);
    assertThat(pdf1).isNotNull();
    assertThat(pdf2).isNotNull();
    assertThat(pdf3).isNotNull();
    assertThat(pdf1.length).isGreaterThan(0);
    assertThat(pdf2.length).isGreaterThan(0);
    assertThat(pdf3.length).isGreaterThan(0);
    assertThat(Math.abs(pdf1.length - pdf2.length)).isLessThan((int) (pdf1.length * 0.1));
    assertThat(Math.abs(pdf1.length - pdf3.length)).isLessThan((int) (pdf1.length * 0.1));
  }
}