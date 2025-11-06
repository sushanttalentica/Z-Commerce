package com.zcommerce.platform.invoice.service;

import com.zcommerce.platform.domain.entity.Order;

// Generic invoice generator service interface.
public interface InvoiceGeneratorService {

  // Generate invoice content for the given order
  byte[] generateInvoice(Order order);

  // Get the content type of the generated invoice
  String getContentType();
}
