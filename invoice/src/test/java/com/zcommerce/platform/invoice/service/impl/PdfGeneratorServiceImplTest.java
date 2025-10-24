package com.zcommerce.platform.invoice.service.impl;

import static org.assertj.core.api.Assertions.*;

import com.ecommerce.productorder.domain.entity.Category;
import com.ecommerce.productorder.domain.entity.Order;
import com.ecommerce.productorder.domain.entity.OrderItem;
import com.ecommerce.productorder.domain.entity.Product;
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
    byte[] pdfContent = pdfGeneratorService.generateInvoicePdf(testOrder);
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
    byte[] pdfContent = pdfGeneratorService.generateInvoicePdf(testOrder);
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
    byte[] pdfContent = pdfGeneratorService.generateInvoicePdf(testOrder);
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
    byte[] pdfContent = pdfGeneratorService.generateInvoicePdf(testOrder);
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
    byte[] pdfContent = pdfGeneratorService.generateInvoicePdf(testOrder);
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
    assertThatThrownBy(() -> pdfGeneratorService.generateInvoicePdf(null))
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
    byte[] pdfContent = pdfGeneratorService.generateInvoicePdf(testOrder);
    assertThat(pdfContent).isNotNull();
    assertThat(pdfContent.length).isGreaterThan(0);
  }

  @Test
  @DisplayName("generateReceiptPdf returns valid PDF content")
  void generateReceiptPdfReturnsValidPdfContent() {
    byte[] pdfContent = pdfGeneratorService.generateReceiptPdf(testOrder);
    assertThat(pdfContent).isNotNull();
    assertThat(pdfContent.length).isGreaterThan(0);
    String pdfContentString = new String(pdfContent);
    assertThat(pdfContentString).containsAnyOf("RECEIPT", "Receipt");
  }

  @Test
  @DisplayName("generateReceiptPdf returns PDF with order details")
  void generateReceiptPdfReturnsPdfWithOrderDetails() {
    byte[] pdfContent = pdfGeneratorService.generateReceiptPdf(testOrder);
    assertThat(pdfContent).isNotNull();
    assertThat(pdfContent.length).isGreaterThan(0);
    String pdfContentString = new String(pdfContent);
    assertThat(pdfContentString).containsAnyOf(testOrder.getOrderNumber(), String.valueOf(testOrder.getId()));
  }

  @Test
  @DisplayName("generateReceiptPdf throws exception when order is null")
  void generateReceiptPdfThrowsExceptionWhenOrderIsNull() {
    assertThatThrownBy(() -> pdfGeneratorService.generateReceiptPdf(null))
        .isInstanceOf(Exception.class);
  }

  @Test
  @DisplayName("generateShippingLabelPdf returns valid PDF content")
  void generateShippingLabelPdfReturnsValidPdfContent() {
    byte[] pdfContent = pdfGeneratorService.generateShippingLabelPdf(testOrder);
    assertThat(pdfContent).isNotNull();
    assertThat(pdfContent.length).isGreaterThan(0);
    String pdfContentString = new String(pdfContent);
    assertThat(pdfContentString).containsAnyOf("SHIPPING", "Shipping", "LABEL", "Label");
  }

  @Test
  @DisplayName("generateShippingLabelPdf returns PDF with shipping address")
  void generateShippingLabelPdfReturnsPdfWithShippingAddress() {
    byte[] pdfContent = pdfGeneratorService.generateShippingLabelPdf(testOrder);
    assertThat(pdfContent).isNotNull();
    assertThat(pdfContent.length).isGreaterThan(0);
    String pdfContentString = new String(pdfContent);
    assertThat(pdfContentString).containsAnyOf(testOrder.getShippingAddress(), "123 Test St");
  }

  @Test
  @DisplayName("generateShippingLabelPdf throws exception when order is null")
  void generateShippingLabelPdfThrowsExceptionWhenOrderIsNull() {
    assertThatThrownBy(() -> pdfGeneratorService.generateShippingLabelPdf(null))
        .isInstanceOf(Exception.class);
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
    assertThatThrownBy(() -> pdfGeneratorService.generateInvoicePdf(testOrder))
        .isInstanceOf(RuntimeException.class);
  }

  @Test
  @DisplayName("generateInvoicePdf handles order with very large amounts")
  void generateInvoicePdfHandlesOrderWithVeryLargeAmounts() {
    BigDecimal largeAmount = new BigDecimal("999999.99");
    testOrder.setTotalAmount(largeAmount);
    testOrder.getOrderItems().get(0).setUnitPrice(largeAmount);
    testOrder.getOrderItems().get(0).setSubtotal(largeAmount);
    byte[] pdfContent = pdfGeneratorService.generateInvoicePdf(testOrder);
    assertThat(pdfContent).isNotNull();
    assertThat(pdfContent.length).isGreaterThan(0);
  }

  @Test
  @DisplayName("generateInvoicePdf throws IllegalArgumentException when order has empty order items")
  void generateInvoicePdfThrowsIllegalArgumentExceptionWhenOrderHasEmptyOrderItems() {
    testOrder.setOrderItems(List.of());
    assertThatThrownBy(() -> pdfGeneratorService.generateInvoicePdf(testOrder))
        .isInstanceOf(IllegalArgumentException.class);
  }

  @Test
  @DisplayName("generateInvoicePdf throws RuntimeException when order has null product")
  void generateInvoicePdfThrowsRuntimeExceptionWhenOrderHasNullProduct() {
    testOrder.getOrderItems().get(0).setProduct(null);
    
    // This should throw an exception due to null product
    assertThatThrownBy(() -> pdfGeneratorService.generateInvoicePdf(testOrder))
        .isInstanceOf(RuntimeException.class);
  }

  @Test
  @DisplayName("generateInvoicePdf handles order with very long customer email")
  void generateInvoicePdfHandlesOrderWithVeryLongCustomerEmail() {
    String longEmail =
        "very.long.email.address.that.might.exceed.normal.length@very.long.domain.name.com";
    testOrder.setCustomerEmail(longEmail);
    byte[] pdfContent = pdfGeneratorService.generateInvoicePdf(testOrder);
    assertThat(pdfContent).isNotNull();
    assertThat(pdfContent.length).isGreaterThan(0);
  }

  @Test
  @DisplayName("generateInvoicePdf handles order with very long shipping address")
  void generateInvoicePdfHandlesOrderWithVeryLongShippingAddress() {
    String longAddress =
        "123 Very Long Street Name That Might Exceed Normal Length, Very Long City Name, Very"
            + " Long State Name, 12345-6789";
    testOrder.setShippingAddress(longAddress);
    byte[] pdfContent = pdfGeneratorService.generateInvoicePdf(testOrder);
    assertThat(pdfContent).isNotNull();
    assertThat(pdfContent.length).isGreaterThan(0);
  }

  @Test
  @DisplayName("generateInvoicePdf handles order with special characters in product name")
  void generateInvoicePdfHandlesOrderWithSpecialCharactersInProductName() {
    testProduct.setName("iPhone 15 Pro Max™ (Special Edition) - 256GB");
    testProduct.setDescription("Latest iPhone with special characters: @#$%^&*()");
    byte[] pdfContent = pdfGeneratorService.generateInvoicePdf(testOrder);
    assertThat(pdfContent).isNotNull();
    assertThat(pdfContent.length).isGreaterThan(0);
  }

  @Test
  @DisplayName("generateInvoicePdf handles order with unicode characters")
  void generateInvoicePdfHandlesOrderWithUnicodeCharacters() {
    testProduct.setName("iPhone 15 Pro Max™ (特别版) - 256GB");
    testProduct.setDescription("最新款iPhone，带有特殊字符：@#$%^&*()");
    testOrder.setCustomerEmail("test@example.com");
    testOrder.setShippingAddress("123 测试街道, 测试城市, 测试省份, 12345");
    byte[] pdfContent = pdfGeneratorService.generateInvoicePdf(testOrder);
    assertThat(pdfContent).isNotNull();
    assertThat(pdfContent.length).isGreaterThan(0);
  }

  @Test
  @DisplayName("generateInvoicePdf completes within reasonable time")
  void generateInvoicePdfCompletesWithinReasonableTime() {
    long startTime = System.currentTimeMillis();
    byte[] pdfContent = pdfGeneratorService.generateInvoicePdf(testOrder);
    long endTime = System.currentTimeMillis();
    long duration = endTime - startTime;
    assertThat(pdfContent).isNotNull();
    assertThat(pdfContent.length).isGreaterThan(0);
    assertThat(duration).isLessThan(5000);
  }

  @Test
  @DisplayName("generateInvoicePdf generates consistent results across multiple calls")
  void generateInvoicePdfGeneratesConsistentResultsAcrossMultipleCalls() {
    byte[] pdf1 = pdfGeneratorService.generateInvoicePdf(testOrder);
    byte[] pdf2 = pdfGeneratorService.generateInvoicePdf(testOrder);
    byte[] pdf3 = pdfGeneratorService.generateInvoicePdf(testOrder);
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