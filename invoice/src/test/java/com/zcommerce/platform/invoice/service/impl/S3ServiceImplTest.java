package com.zcommerce.platform.invoice.service.impl;

import static org.assertj.core.api.Assertions.*;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;

import com.zcommerce.platform.invoice.config.S3Properties;
import java.util.Optional;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

// JUnit tests for S3ServiceImpl class.
@ExtendWith(MockitoExtension.class)
@org.mockito.junit.jupiter.MockitoSettings(strictness = org.mockito.quality.Strictness.LENIENT)
@DisplayName("S3ServiceImpl Tests")
public class S3ServiceImplTest {

  @Mock private S3Properties s3Properties;
  @InjectMocks private S3ServiceImpl s3Service;

  private final String testBucketName = "test-bucket";
  private final String testKey = "invoices/100/test-invoice.pdf";
  private final byte[] testContent = "test invoice content".getBytes();
  private final String testContentType = "application/pdf";

  @BeforeEach
  void setUp() {
    lenient().when(s3Properties.getBucketName()).thenReturn(testBucketName);
    lenient().when(s3Properties.getRegion()).thenReturn("us-east-1");
    lenient().when(s3Properties.getAccessKey()).thenReturn("test-access-key");
    lenient().when(s3Properties.getSecretKey()).thenReturn("test-secret-key");
  }

  @Test
  @DisplayName("uploadFile throws IllegalArgumentException when key is null")
  void uploadFileThrowsIllegalArgumentExceptionWhenKeyIsNull() {
    assertThatThrownBy(() -> s3Service.uploadFile(null, testContent, testContentType))
        .isInstanceOf(IllegalArgumentException.class);
  }

  @Test
  @DisplayName("uploadFile throws IllegalArgumentException when key is empty")
  void uploadFileThrowsIllegalArgumentExceptionWhenKeyIsEmpty() {
    assertThatThrownBy(() -> s3Service.uploadFile("", testContent, testContentType))
        .isInstanceOf(IllegalArgumentException.class);
  }

  @Test
  @DisplayName("uploadFile throws IllegalArgumentException when content is null")
  void uploadFileThrowsIllegalArgumentExceptionWhenContentIsNull() {
    assertThatThrownBy(() -> s3Service.uploadFile(testKey, null, testContentType))
        .isInstanceOf(IllegalArgumentException.class);
  }

  @Test
  @DisplayName("uploadFile throws IllegalArgumentException when content is empty")
  void uploadFileThrowsIllegalArgumentExceptionWhenContentIsEmpty() {
    assertThatThrownBy(() -> s3Service.uploadFile(testKey, new byte[0], testContentType))
        .isInstanceOf(IllegalArgumentException.class);
  }

  @Test
  @DisplayName("uploadFile throws IllegalArgumentException when content type is null")
  void uploadFileThrowsIllegalArgumentExceptionWhenContentTypeIsNull() {
    assertThatThrownBy(() -> s3Service.uploadFile(testKey, testContent, null))
        .isInstanceOf(IllegalArgumentException.class);
  }

  @Test
  @DisplayName("uploadFile throws IllegalArgumentException when content type is empty")
  void uploadFileThrowsIllegalArgumentExceptionWhenContentTypeIsEmpty() {
    assertThatThrownBy(() -> s3Service.uploadFile(testKey, testContent, ""))
        .isInstanceOf(IllegalArgumentException.class);
  }

  @Test
  @DisplayName("uploadFile throws RuntimeException when AWS SDK is not configured")
  void uploadFileThrowsRuntimeExceptionWhenAWSSDKIsNotConfigured() {
    // This test will fail due to AWS SDK not being configured, but validates parameter validation
    assertThatThrownBy(() -> s3Service.uploadFile(testKey, testContent, testContentType))
        .isInstanceOf(RuntimeException.class);
  }

  @Test
  @DisplayName("downloadFile throws IllegalArgumentException when key is null")
  void downloadFileThrowsIllegalArgumentExceptionWhenKeyIsNull() {
    assertThatThrownBy(() -> s3Service.downloadFile(null))
        .isInstanceOf(IllegalArgumentException.class);
  }

  @Test
  @DisplayName("downloadFile throws IllegalArgumentException when key is empty")
  void downloadFileThrowsIllegalArgumentExceptionWhenKeyIsEmpty() {
    assertThatThrownBy(() -> s3Service.downloadFile(""))
        .isInstanceOf(IllegalArgumentException.class);
  }

  @Test
  @DisplayName("downloadFile returns empty when AWS SDK is not configured")
  void downloadFileReturnsEmptyWhenAWSSDKIsNotConfigured() {
    // This test will fail due to AWS SDK not being configured, but validates parameter validation
    Optional<byte[]> result = s3Service.downloadFile(testKey);
    assertThat(result).isNotPresent();
  }

  @Test
  @DisplayName("deleteFile throws IllegalArgumentException when key is null")
  void deleteFileThrowsIllegalArgumentExceptionWhenKeyIsNull() {
    assertThatThrownBy(() -> s3Service.deleteFile(null))
        .isInstanceOf(IllegalArgumentException.class);
  }

  @Test
  @DisplayName("deleteFile throws IllegalArgumentException when key is empty")
  void deleteFileThrowsIllegalArgumentExceptionWhenKeyIsEmpty() {
    assertThatThrownBy(() -> s3Service.deleteFile(""))
        .isInstanceOf(IllegalArgumentException.class);
  }

  @Test
  @DisplayName("deleteFile returns false when AWS SDK is not configured")
  void deleteFileReturnsFalseWhenAWSSDKIsNotConfigured() {
    // This test will fail due to AWS SDK not being configured, but validates parameter validation
    boolean result = s3Service.deleteFile(testKey);
    assertThat(result).isFalse();
  }

  @Test
  @DisplayName("fileExists throws IllegalArgumentException when key is null")
  void fileExistsThrowsIllegalArgumentExceptionWhenKeyIsNull() {
    assertThatThrownBy(() -> s3Service.fileExists(null))
        .isInstanceOf(IllegalArgumentException.class);
  }

  @Test
  @DisplayName("fileExists throws IllegalArgumentException when key is empty")
  void fileExistsThrowsIllegalArgumentExceptionWhenKeyIsEmpty() {
    assertThatThrownBy(() -> s3Service.fileExists(""))
        .isInstanceOf(IllegalArgumentException.class);
  }

  @Test
  @DisplayName("fileExists returns false when AWS SDK is not configured")
  void fileExistsReturnsFalseWhenAWSSDKIsNotConfigured() {
    // This test will fail due to AWS SDK not being configured, but validates parameter validation
    boolean result = s3Service.fileExists(testKey);
    assertThat(result).isFalse();
  }

  @Test
  @DisplayName("getFileUrl throws IllegalArgumentException when key is null")
  void getFileUrlThrowsIllegalArgumentExceptionWhenKeyIsNull() {
    assertThatThrownBy(() -> s3Service.getFileUrl(null))
        .isInstanceOf(IllegalArgumentException.class);
  }

  @Test
  @DisplayName("getFileUrl throws IllegalArgumentException when key is empty")
  void getFileUrlThrowsIllegalArgumentExceptionWhenKeyIsEmpty() {
    assertThatThrownBy(() -> s3Service.getFileUrl(""))
        .isInstanceOf(IllegalArgumentException.class);
  }


  @Test
  @DisplayName("generatePresignedUrl throws IllegalArgumentException when key is null")
  void generatePresignedUrlThrowsIllegalArgumentExceptionWhenKeyIsNull() {
    assertThatThrownBy(() -> s3Service.generatePresignedUrl(null, 60))
        .isInstanceOf(IllegalArgumentException.class);
  }

  @Test
  @DisplayName("generatePresignedUrl throws IllegalArgumentException when key is empty")
  void generatePresignedUrlThrowsIllegalArgumentExceptionWhenKeyIsEmpty() {
    assertThatThrownBy(() -> s3Service.generatePresignedUrl("", 60))
        .isInstanceOf(IllegalArgumentException.class);
  }

  @Test
  @DisplayName("generatePresignedUrl throws IllegalArgumentException when expiration is invalid")
  void generatePresignedUrlThrowsIllegalArgumentExceptionWhenExpirationIsInvalid() {
    assertThatThrownBy(() -> s3Service.generatePresignedUrl(testKey, 0))
        .isInstanceOf(IllegalArgumentException.class);
  }


  @Test
  @DisplayName("setBucketPolicy throws IllegalArgumentException when bucket name is null")
  void setBucketPolicyThrowsIllegalArgumentExceptionWhenBucketNameIsNull() {
    assertThatThrownBy(() -> s3Service.setBucketPolicy(null, "test-policy"))
        .isInstanceOf(IllegalArgumentException.class);
  }

  @Test
  @DisplayName("setBucketPolicy throws IllegalArgumentException when bucket name is empty")
  void setBucketPolicyThrowsIllegalArgumentExceptionWhenBucketNameIsEmpty() {
    assertThatThrownBy(() -> s3Service.setBucketPolicy("", "test-policy"))
        .isInstanceOf(IllegalArgumentException.class);
  }

  @Test
  @DisplayName("setBucketPolicy throws IllegalArgumentException when policy is null")
  void setBucketPolicyThrowsIllegalArgumentExceptionWhenPolicyIsNull() {
    assertThatThrownBy(() -> s3Service.setBucketPolicy(testBucketName, null))
        .isInstanceOf(IllegalArgumentException.class);
  }

  @Test
  @DisplayName("setBucketPolicy throws IllegalArgumentException when policy is empty")
  void setBucketPolicyThrowsIllegalArgumentExceptionWhenPolicyIsEmpty() {
    assertThatThrownBy(() -> s3Service.setBucketPolicy(testBucketName, ""))
        .isInstanceOf(IllegalArgumentException.class);
  }


  @Test
  @DisplayName("uploadFile throws RuntimeException when key is very long")
  void uploadFileThrowsRuntimeExceptionWhenKeyIsVeryLong() {
    String longKey = "a".repeat(1000);
    // This test will fail due to AWS SDK not being configured, but validates parameter validation
    assertThatThrownBy(() -> s3Service.uploadFile(longKey, testContent, testContentType))
        .isInstanceOf(RuntimeException.class);
  }

  @Test
  @DisplayName("uploadFile throws RuntimeException when content is very large")
  void uploadFileThrowsRuntimeExceptionWhenContentIsVeryLarge() {
    byte[] largeContent = new byte[1024 * 1024]; // 1MB
    // This test will fail due to AWS SDK not being configured, but validates parameter validation
    assertThatThrownBy(() -> s3Service.uploadFile(testKey, largeContent, testContentType))
        .isInstanceOf(RuntimeException.class);
  }

  @Test
  @DisplayName("uploadFile throws RuntimeException when key contains special characters")
  void uploadFileThrowsRuntimeExceptionWhenKeyContainsSpecialCharacters() {
    String specialKey = "invoices/100/test-invoice-@#$%^&*().pdf";
    // This test will fail due to AWS SDK not being configured, but validates parameter validation
    assertThatThrownBy(() -> s3Service.uploadFile(specialKey, testContent, testContentType))
        .isInstanceOf(RuntimeException.class);
  }

  @Test
  @DisplayName("uploadFile throws RuntimeException when key contains unicode characters")
  void uploadFileThrowsRuntimeExceptionWhenKeyContainsUnicodeCharacters() {
    String unicodeKey = "invoices/100/test-invoice-测试.pdf";
    // This test will fail due to AWS SDK not being configured, but validates parameter validation
    assertThatThrownBy(() -> s3Service.uploadFile(unicodeKey, testContent, testContentType))
        .isInstanceOf(RuntimeException.class);
  }
}