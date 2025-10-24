package com.zcommerce.platform.invoice.service.impl;

import com.ecommerce.productorder.domain.entity.Order;
import com.ecommerce.productorder.domain.entity.OrderItem;
import com.zcommerce.platform.invoice.service.InvoiceGeneratorService;
import com.itextpdf.io.font.constants.StandardFonts;
import com.itextpdf.kernel.font.PdfFontFactory;
import com.itextpdf.kernel.pdf.PdfDocument;
import com.itextpdf.kernel.pdf.PdfWriter;
import com.itextpdf.layout.Document;
import com.itextpdf.layout.element.Paragraph;
import com.itextpdf.layout.element.Table;
import com.itextpdf.layout.properties.TextAlignment;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Service
@Slf4j
public class PdfGeneratorServiceImpl implements InvoiceGeneratorService {

  public PdfGeneratorServiceImpl() {}

  @Override
  public byte[] generateInvoice(Order order) {
    return generateInvoicePdf(order);
  }

  @Override
  public String getContentType() {
    return PdfConstants.CONTENT_TYPE;
  }

  private byte[] generateInvoicePdf(Order order) {
    // Validate order first
    validateOrderForPdf(order);
    
    log.info(PdfConstants.GENERATING_INVOICE_LOG, order.getId());

    try {

      // Create PDF using iText
      ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
      PdfWriter writer = new PdfWriter(outputStream);
      PdfDocument pdfDoc = new PdfDocument(writer);
      Document document = new Document(pdfDoc);

      // Add invoice header
      Paragraph header =
          new Paragraph(PdfConstants.INVOICE_HEADER)
              .setFontSize(PdfConstants.HEADER_FONT_SIZE)
              .setFont(PdfFontFactory.createFont(StandardFonts.HELVETICA_BOLD))
              .setTextAlignment(TextAlignment.CENTER);
      document.add(header);

      document.add(new Paragraph(PdfConstants.NEWLINE));

      // Add invoice details
      document.add(new Paragraph(PdfConstants.INVOICE_NUMBER_LABEL + order.getOrderNumber()));
      document.add(
          new Paragraph(
              PdfConstants.DATE_LABEL
                  + LocalDateTime.now()
                      .format(DateTimeFormatter.ofPattern(PdfConstants.DATE_FORMAT_PATTERN))));
      document.add(new Paragraph(PdfConstants.CUSTOMER_EMAIL_LABEL + order.getCustomerEmail()));
      document.add(new Paragraph(PdfConstants.SHIPPING_ADDRESS_LABEL + order.getShippingAddress()));

      document.add(new Paragraph(PdfConstants.NEWLINE));

      // Add order items table
      Paragraph itemsHeader =
          new Paragraph(PdfConstants.ORDER_ITEMS_LABEL)
              .setFont(PdfFontFactory.createFont(StandardFonts.HELVETICA_BOLD));
      document.add(itemsHeader);
      Table table = new Table(4);
      table.addHeaderCell(PdfConstants.PRODUCT_HEADER);
      table.addHeaderCell(PdfConstants.QUANTITY_HEADER);
      table.addHeaderCell(PdfConstants.UNIT_PRICE_HEADER);
      table.addHeaderCell(PdfConstants.SUBTOTAL_HEADER);

      for (OrderItem item : order.getOrderItems()) {
        table.addCell(item.getProduct().getName());
        table.addCell(String.valueOf(item.getQuantity()));
        table.addCell(PdfConstants.CURRENCY_SYMBOL + item.getUnitPrice());
        table.addCell(PdfConstants.CURRENCY_SYMBOL + item.getSubtotal());
      }

      document.add(table);

      document.add(new Paragraph(PdfConstants.NEWLINE));

      // Add total
      Paragraph total =
          new Paragraph(PdfConstants.TOTAL_AMOUNT_LABEL + PdfConstants.CURRENCY_SYMBOL + order.getTotalAmount())
              .setFont(PdfFontFactory.createFont(StandardFonts.HELVETICA_BOLD))
              .setFontSize(PdfConstants.TOTAL_FONT_SIZE);
      document.add(total);

      document.add(new Paragraph(PdfConstants.NEWLINE + PdfConstants.STATUS_LABEL + order.getStatus()));
      document.add(
          new Paragraph(
              PdfConstants.ORDER_DATE_LABEL
                  + order
                      .getCreatedAt()
                      .format(DateTimeFormatter.ofPattern(PdfConstants.DATE_FORMAT_PATTERN))));

      // Close document
      document.close();

      byte[] pdfBytes = outputStream.toByteArray();
      log.info(PdfConstants.INVOICE_GENERATED_SUCCESS_LOG, order.getId(), pdfBytes.length);
      return pdfBytes;

    } catch (Exception e) {
      log.error(PdfConstants.INVOICE_GENERATION_ERROR_LOG, order.getId(), e);
      throw new RuntimeException(PdfConstants.PDF_INVOICE_GENERATION_ERROR + e.getMessage());
    }
  }

  @Override
  public byte[] generateReceiptPdf(Order order) {
    // Validate order first
    validateOrderForPdf(order);
    
    log.info(PdfConstants.GENERATING_RECEIPT_LOG, order.getId());

    try {

      // Generate PDF content
      ByteArrayOutputStream outputStream = new ByteArrayOutputStream();

      // Create PDF content (simplified implementation)
      String pdfContent = createReceiptContent(order);
      outputStream.write(pdfContent.getBytes());
      outputStream.close();

      byte[] pdfBytes = outputStream.toByteArray();

      log.info(PdfConstants.RECEIPT_GENERATED_SUCCESS_LOG, order.getId());
      return pdfBytes;

    } catch (IOException e) {
      log.error(PdfConstants.RECEIPT_GENERATION_ERROR_LOG, order.getId(), e);
      throw new RuntimeException(PdfConstants.PDF_RECEIPT_GENERATION_ERROR + e.getMessage());
    } catch (Exception e) {
      log.error(PdfConstants.RECEIPT_GENERATION_ERROR_LOG, order.getId(), e);
      throw new RuntimeException(PdfConstants.PDF_RECEIPT_GENERATION_ERROR + e.getMessage());
    }
  }

  @Override
  public byte[] generateShippingLabelPdf(Order order) {
    // Validate order first
    validateOrderForPdf(order);
    
    log.info(PdfConstants.GENERATING_SHIPPING_LABEL_LOG, order.getId());

    try {

      // Generate PDF content
      ByteArrayOutputStream outputStream = new ByteArrayOutputStream();

      // Create PDF content (simplified implementation)
      String pdfContent = createShippingLabelContent(order);
      outputStream.write(pdfContent.getBytes());
      outputStream.close();

      byte[] pdfBytes = outputStream.toByteArray();

      log.info(PdfConstants.SHIPPING_LABEL_GENERATED_SUCCESS_LOG, order.getId());
      return pdfBytes;

    } catch (IOException e) {
      log.error(PdfConstants.SHIPPING_LABEL_GENERATION_ERROR_LOG, order.getId(), e);
      throw new RuntimeException(PdfConstants.PDF_SHIPPING_LABEL_GENERATION_ERROR + e.getMessage());
    } catch (Exception e) {
      log.error(PdfConstants.SHIPPING_LABEL_GENERATION_ERROR_LOG, order.getId(), e);
      throw new RuntimeException(PdfConstants.PDF_SHIPPING_LABEL_GENERATION_ERROR + e.getMessage());
    }
  }

  private void validateOrderForPdf(Order order) {
    if (order == null) {
      throw new IllegalArgumentException(PdfConstants.ORDER_NULL_ERROR);
    }

    if (order.getId() == null) {
      throw new IllegalArgumentException(PdfConstants.ORDER_ID_NULL_ERROR);
    }

    if (order.getCustomerEmail() == null || order.getCustomerEmail().trim().isEmpty()) {
      throw new IllegalArgumentException(PdfConstants.CUSTOMER_EMAIL_REQUIRED_ERROR);
    }

    if (order.getTotalAmount() == null
        || order.getTotalAmount().compareTo(java.math.BigDecimal.ZERO) <= 0) {
      throw new IllegalArgumentException(PdfConstants.ORDER_TOTAL_AMOUNT_ERROR);
    }

    if (order.getOrderItems() == null || order.getOrderItems().isEmpty()) {
      throw new IllegalArgumentException(PdfConstants.ORDER_ITEMS_NULL_ERROR);
    }
  }

  private String createReceiptContent(Order order) {
    StringBuilder content = new StringBuilder();

    // Receipt header
    content.append(PdfConstants.RECEIPT_HEADER).append(PdfConstants.NEWLINE);
    content.append(PdfConstants.RECEIPT_SEPARATOR).append(PdfConstants.DOUBLE_NEWLINE);

    // Receipt details
    content.append(PdfConstants.RECEIPT_NUMBER_LABEL).append(order.getOrderNumber()).append(PdfConstants.NEWLINE);
    content
        .append(PdfConstants.DATE_LABEL)
        .append(LocalDateTime.now().format(DateTimeFormatter.ofPattern(PdfConstants.DATE_FORMAT_PATTERN)))
        .append(PdfConstants.NEWLINE);
    content.append(PdfConstants.CUSTOMER_ID_LABEL).append(order.getCustomerId()).append(PdfConstants.NEWLINE);
    content.append(PdfConstants.CUSTOMER_EMAIL_LABEL).append(order.getCustomerEmail()).append(PdfConstants.NEWLINE);
    content.append(PdfConstants.ORDER_STATUS_LABEL).append(order.getStatus()).append(PdfConstants.NEWLINE);
    content.append(PdfConstants.TOTAL_AMOUNT_LABEL).append(PdfConstants.CURRENCY_SYMBOL).append(order.getTotalAmount()).append(PdfConstants.NEWLINE);

    content.append(PdfConstants.NEWLINE);
    content.append(PdfConstants.PAYMENT_RECEIVED_MESSAGE).append(PdfConstants.NEWLINE);

    return content.toString();
  }

  private String createShippingLabelContent(Order order) {
    StringBuilder content = new StringBuilder();

    // Shipping label header
    content.append(PdfConstants.SHIPPING_LABEL_HEADER).append(PdfConstants.NEWLINE);
    content.append(PdfConstants.SHIPPING_LABEL_SEPARATOR).append(PdfConstants.DOUBLE_NEWLINE);

    // Shipping details
    content.append(PdfConstants.ORDER_NUMBER_LABEL).append(order.getOrderNumber()).append(PdfConstants.NEWLINE);
    content.append(PdfConstants.CUSTOMER_ID_LABEL).append(order.getCustomerId()).append(PdfConstants.NEWLINE);
    content.append(PdfConstants.CUSTOMER_EMAIL_LABEL).append(order.getCustomerEmail()).append(PdfConstants.NEWLINE);

    if (order.getShippingAddress() != null) {
      content.append(PdfConstants.SHIPPING_ADDRESS_LABEL).append(order.getShippingAddress()).append(PdfConstants.NEWLINE);
    }

    content.append(PdfConstants.NEWLINE);
    content.append(PdfConstants.HANDLE_WITH_CARE_MESSAGE).append(PdfConstants.NEWLINE);

    return content.toString();
  }
}
