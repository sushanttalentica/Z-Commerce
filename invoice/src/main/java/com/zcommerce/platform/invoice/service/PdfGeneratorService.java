package com.zcommerce.platform.invoice.service;

import com.zcommerce.platform.domain.entity.Order;

// PDF-specific implementation of InvoiceGeneratorService.
public interface PdfGeneratorService extends InvoiceGeneratorService {

  byte[] generateInvoicePdf(Order order);

  byte[] generateReceiptPdf(Order order);

  byte[] generateShippingLabelPdf(Order order);
}
