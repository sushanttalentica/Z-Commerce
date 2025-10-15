package com.zcommerce.platform.invoice.service;

import com.zcommerce.platform.domain.entity.Order;

public interface PdfGeneratorService {

  byte[] generateInvoicePdf(Order order);

  byte[] generateReceiptPdf(Order order);

  byte[] generateShippingLabelPdf(Order order);
}
