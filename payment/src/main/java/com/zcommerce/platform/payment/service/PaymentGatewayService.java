package com.zcommerce.platform.payment.service;

import com.zcommerce.platform.payment.domain.entity.Payment;
import com.zcommerce.platform.payment.dto.request.ProcessPaymentRequest;
import com.zcommerce.platform.payment.dto.response.PaymentResponse;
import java.math.BigDecimal;

public interface PaymentGatewayService {

  PaymentResponse processPayment(Payment payment, ProcessPaymentRequest request);

  PaymentResponse processRefund(Payment payment, BigDecimal refundAmount);

  boolean validatePaymentMethod(ProcessPaymentRequest request);

  java.util.List<String> getSupportedPaymentMethods();

  boolean isGatewayHealthy();
}
