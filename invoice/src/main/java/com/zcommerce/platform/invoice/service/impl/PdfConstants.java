package com.zcommerce.platform.invoice.service.impl;

// Constants for PDF generation to avoid magic strings.
public final class PdfConstants {
  
  // Content Type
  public static final String CONTENT_TYPE = "application/pdf";
  
  // Font Sizes
  public static final int HEADER_FONT_SIZE = 24;
  public static final int TOTAL_FONT_SIZE = 14;
  
  // Date Format
  public static final String DATE_FORMAT_PATTERN = "yyyy-MM-dd HH:mm:ss";
  
  // Currency Symbol
  public static final String CURRENCY_SYMBOL = "$";
  
  // PDF Headers
  public static final String INVOICE_HEADER = "INVOICE";
  public static final String RECEIPT_HEADER = "RECEIPT";
  public static final String SHIPPING_LABEL_HEADER = "SHIPPING LABEL";
  
  // Table Headers
  public static final String PRODUCT_HEADER = "Product";
  public static final String QUANTITY_HEADER = "Quantity";
  public static final String UNIT_PRICE_HEADER = "Unit Price";
  public static final String SUBTOTAL_HEADER = "Subtotal";
  
  // Labels
  public static final String INVOICE_NUMBER_LABEL = "Invoice Number: ";
  public static final String DATE_LABEL = "Date: ";
  public static final String CUSTOMER_EMAIL_LABEL = "Customer Email: ";
  public static final String SHIPPING_ADDRESS_LABEL = "Shipping Address: ";
  public static final String ORDER_ITEMS_LABEL = "Order Items:";
  public static final String TOTAL_AMOUNT_LABEL = "Total Amount: ";
  public static final String STATUS_LABEL = "Status: ";
  public static final String ORDER_DATE_LABEL = "Order Date: ";
  public static final String RECEIPT_NUMBER_LABEL = "Receipt Number: ";
  public static final String CUSTOMER_ID_LABEL = "Customer ID: ";
  public static final String ORDER_STATUS_LABEL = "Order Status: ";
  public static final String ORDER_NUMBER_LABEL = "Order Number: ";
  
  // Messages
  public static final String PAYMENT_RECEIVED_MESSAGE = "Payment received. Thank you!";
  public static final String HANDLE_WITH_CARE_MESSAGE = "Handle with care!";
  
  // Separators
  public static final String RECEIPT_SEPARATOR = "=======";
  public static final String SHIPPING_LABEL_SEPARATOR = "==============";
  public static final String NEWLINE = "\n";
  public static final String DOUBLE_NEWLINE = "\n\n";
  
  // Error Messages
  public static final String ORDER_NULL_ERROR = "Order cannot be null";
  public static final String ORDER_ID_NULL_ERROR = "Order ID cannot be null";
  public static final String CUSTOMER_EMAIL_REQUIRED_ERROR = "Customer email is required";
  public static final String ORDER_TOTAL_AMOUNT_ERROR = "Order total amount must be greater than zero";
  public static final String ORDER_ITEMS_NULL_ERROR = "Order items cannot be null or empty";
  public static final String PDF_INVOICE_GENERATION_ERROR = "Failed to generate PDF invoice: ";
  public static final String PDF_RECEIPT_GENERATION_ERROR = "Failed to generate PDF receipt: ";
  public static final String PDF_SHIPPING_LABEL_GENERATION_ERROR = "Failed to generate PDF shipping label: ";
  
  // Log Messages
  public static final String GENERATING_INVOICE_LOG = "Generating PDF invoice for order ID: {}";
  public static final String INVOICE_GENERATED_SUCCESS_LOG = "PDF invoice generated successfully for order ID: {} ({} bytes)";
  public static final String GENERATING_RECEIPT_LOG = "Generating PDF receipt for order ID: {}";
  public static final String RECEIPT_GENERATED_SUCCESS_LOG = "PDF receipt generated successfully for order ID: {}";
  public static final String GENERATING_SHIPPING_LABEL_LOG = "Generating PDF shipping label for order ID: {}";
  public static final String SHIPPING_LABEL_GENERATED_SUCCESS_LOG = "PDF shipping label generated successfully for order ID: {}";
  public static final String INVOICE_GENERATION_ERROR_LOG = "Error generating PDF invoice for order ID: {}";
  public static final String RECEIPT_GENERATION_ERROR_LOG = "Error generating PDF receipt for order ID: {}";
  public static final String SHIPPING_LABEL_GENERATION_ERROR_LOG = "Error generating PDF shipping label for order ID: {}";
  
  private PdfConstants() {
    // Utility class
  }
}
