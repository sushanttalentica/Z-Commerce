package com.zcommerce.platform.invoice;

import com.zcommerce.platform.invoice.domain.entity.InvoiceTest;
import com.zcommerce.platform.invoice.domain.repository.InvoiceRepositoryTest;
import com.zcommerce.platform.invoice.service.impl.InvoiceServiceImplTest;
import com.zcommerce.platform.invoice.service.impl.PdfGeneratorServiceImplTest;
import com.zcommerce.platform.invoice.service.impl.S3ServiceImplTest;
import org.junit.platform.suite.api.SelectClasses;
import org.junit.platform.suite.api.Suite;
import org.junit.platform.suite.api.SuiteDisplayName;

// Comprehensive test suite for the Invoice module.
@Suite
@SuiteDisplayName("Invoice Module Test Suite")
@SelectClasses({
  InvoiceTest.class,
  InvoiceRepositoryTest.class,
  InvoiceServiceImplTest.class,
  PdfGeneratorServiceImplTest.class,
  S3ServiceImplTest.class
})
public class InvoiceModuleTestSuite {}
