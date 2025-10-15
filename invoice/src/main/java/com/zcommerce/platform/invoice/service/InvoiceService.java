package com.zcommerce.platform.invoice.service;

import com.zcommerce.platform.domain.entity.Order;
import java.util.Optional;

public interface InvoiceService {

  Optional<String> generateInvoice(Order order);

  Optional<String> getInvoiceUrl(Long orderId);

  boolean deleteInvoice(Long orderId);

  boolean invoiceExists(Long orderId);
}
