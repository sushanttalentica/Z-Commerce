package com.zcommerce.platform.invoice.service;

import static org.assertj.core.api.Assertions.*;
import static org.mockito.Mockito.*;

import com.ecommerce.productorder.invoice.config.S3Properties;
import com.ecommerce.productorder.invoice.service.impl.S3ServiceImpl;
import java.lang.reflect.Field;
import java.util.Optional;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import software.amazon.awssdk.services.s3.S3Client;

// JUnit tests for S3Service interface.
@ExtendWith(MockitoExtension.class)
@org.mockito.junit.jupiter.MockitoSettings(strictness = org.mockito.quality.Strictness.LENIENT)
@DisplayName("S3Service Tests")
public class S3ServiceTest {

  @Mock private S3Properties s3Properties;
  @Mock private S3Client s3Client;

  @InjectMocks private S3ServiceImpl s3Service;

  @BeforeEach
  void setUp() throws Exception {
    // Inject the mocked S3Client into the service
    Field s3ClientField = S3ServiceImpl.class.getDeclaredField("s3Client");
    s3ClientField.setAccessible(true);
    s3ClientField.set(s3Service, s3Client);
  }

  @Test
  @DisplayName("uploadFile returns URL when successful")
  void uploadFileReturnsUrlWhenSuccessful() {
    when(s3Properties.getBucketName()).thenReturn("test-bucket");
    when(s3Properties.getRegion()).thenReturn("us-east-1");
    
    String key = "test-file.pdf";
    byte[] content = "test&#x20;content".getBytes();
    String contentType = "application/pdf";

    String result = s3Service.uploadFile(key, content, contentType);

    assertThat(result).isNotNull();
    assertThat(result.contains("test-bucket")).isTrue();
    assertThat(result.contains(key)).isTrue();
  }

  @Test
  @DisplayName("downloadFile returns file content when successful")
  void downloadFileReturnsFileContentWhenSuccessful() {
    when(s3Properties.getBucketName()).thenReturn("test-bucket");
    when(s3Properties.getRegion()).thenReturn("us-east-1");
    
    String key = "test-file.pdf";

    Optional<byte[]> result = s3Service.downloadFile(key);

    assertThat(result).isNotNull();
  }

  @Test
  @DisplayName("deleteFile returns true when successful")
  void deleteFileReturnsTrueWhenSuccessful() {
    when(s3Properties.getBucketName()).thenReturn("test-bucket");
    when(s3Properties.getRegion()).thenReturn("us-east-1");
    
    String key = "test-file.pdf";

    boolean result = s3Service.deleteFile(key);

    assertThat(result).isNotNull();
  }

  @Test
  @DisplayName("fileExists returns true when file exists")
  void fileExistsReturnsTrueWhenFileExists() {
    when(s3Properties.getBucketName()).thenReturn("test-bucket");
    when(s3Properties.getRegion()).thenReturn("us-east-1");
    
    String key = "test-file.pdf";

    boolean result = s3Service.fileExists(key);

    assertThat(result).isNotNull();
  }

  @Test
  @DisplayName("getFileUrl returns URL when successful")
  void getFileUrlReturnsUrlWhenSuccessful() {
    when(s3Properties.getBucketName()).thenReturn("test-bucket");
    when(s3Properties.getRegion()).thenReturn("us-east-1");
    
    String key = "test-file.pdf";

    Optional<String> result = s3Service.getFileUrl(key);

    assertThat(result).isNotNull();
  }

  @Test
  @DisplayName("generatePresignedUrl returns presigned URL when successful")
  void generatePresignedUrlReturnsPresignedUrlWhenSuccessful() {
    when(s3Properties.getBucketName()).thenReturn("test-bucket");
    when(s3Properties.getRegion()).thenReturn("us-east-1");
    
    String key = "test-file.pdf";
    int expirationMinutes = 60;

    Optional<String> result = s3Service.generatePresignedUrl(key, expirationMinutes);

    assertThat(result).isNotNull();
  }

  @Test
  @DisplayName("setBucketPolicy returns true when successful")
  void setBucketPolicyReturnsTrueWhenSuccessful() {
    when(s3Properties.getBucketName()).thenReturn("test-bucket");
    when(s3Properties.getRegion()).thenReturn("us-east-1");
    
    String bucketName = "test-bucket";
    String policy = "{\"Version\":\"2012-10-17\",\"Statement\":[]}";

    boolean result = s3Service.setBucketPolicy(bucketName, policy);

    assertThat(result).isNotNull();
  }

  @Test
  @DisplayName("uploadFile throws exception for null key")
  void uploadFileThrowsExceptionForNullKey() {
    when(s3Properties.getBucketName()).thenReturn("test-bucket");
    when(s3Properties.getRegion()).thenReturn("us-east-1");
    
    byte[] content = "test content".getBytes();
    String contentType = "application/pdf";

    assertThatThrownBy(IllegalArgumentException.class, () -> s3Service.uploadFile(null, content, contentType));
  }

  @Test
  @DisplayName("uploadFile throws exception for null content")
  void uploadFileThrowsExceptionForNullContent() {
    when(s3Properties.getBucketName()).thenReturn("test-bucket");
    when(s3Properties.getRegion()).thenReturn("us-east-1");
    
    String key = "test-file.pdf";
    String contentType = "application/pdf";

    assertThatThrownBy(IllegalArgumentException.class, () -> s3Service.uploadFile(key, null, contentType));
  }

  @Test
  @DisplayName("uploadFile throws exception for null content type")
  void uploadFileThrowsExceptionForNullContentType() {
    when(s3Properties.getBucketName()).thenReturn("test-bucket");
    when(s3Properties.getRegion()).thenReturn("us-east-1");
    
    String key = "test-file.pdf";
    byte[] content = "test content".getBytes();

    assertThatThrownBy(IllegalArgumentException.class, () -> s3Service.uploadFile(key, content, null));
  }

  @Test
  @DisplayName("downloadFile throws exception for null key")
  void downloadFileThrowsExceptionForNullKey() {
    when(s3Properties.getBucketName()).thenReturn("test-bucket");
    when(s3Properties.getRegion()).thenReturn("us-east-1");
    
    assertThatThrownBy(IllegalArgumentException.class, () -> s3Service.downloadFile(null));
  }

  @Test
  @DisplayName("deleteFile throws exception for null key")
  void deleteFileThrowsExceptionForNullKey() {
    when(s3Properties.getBucketName()).thenReturn("test-bucket");
    when(s3Properties.getRegion()).thenReturn("us-east-1");
    
    assertThatThrownBy(IllegalArgumentException.class, () -> s3Service.deleteFile(null));
  }

  @Test
  @DisplayName("fileExists throws exception for null key")
  void fileExistsThrowsExceptionForNullKey() {
    when(s3Properties.getBucketName()).thenReturn("test-bucket");
    when(s3Properties.getRegion()).thenReturn("us-east-1");
    
    assertThatThrownBy(IllegalArgumentException.class, () -> s3Service.fileExists(null));
  }

  @Test
  @DisplayName("getFileUrl throws exception for null key")
  void getFileUrlThrowsExceptionForNullKey() {
    when(s3Properties.getBucketName()).thenReturn("test-bucket");
    when(s3Properties.getRegion()).thenReturn("us-east-1");
    
    assertThatThrownBy(IllegalArgumentException.class, () -> s3Service.getFileUrl(null));
  }

  @Test
  @DisplayName("generatePresignedUrl throws exception for null key")
  void generatePresignedUrlThrowsExceptionForNullKey() {
    when(s3Properties.getBucketName()).thenReturn("test-bucket");
    when(s3Properties.getRegion()).thenReturn("us-east-1");
    
    assertThatThrownBy(IllegalArgumentException.class, () -> s3Service.generatePresignedUrl(null, 60));
  }

  @Test
  @DisplayName("generatePresignedUrl throws exception for invalid expiration")
  void generatePresignedUrlThrowsExceptionForInvalidExpiration() {
    when(s3Properties.getBucketName()).thenReturn("test-bucket");
    when(s3Properties.getRegion()).thenReturn("us-east-1");
    
    String key = "test-file.pdf";

    assertThatThrownBy(IllegalArgumentException.class, () -> s3Service.generatePresignedUrl(key, -1));
    assertThatThrownBy(IllegalArgumentException.class, () -> s3Service.generatePresignedUrl(key, 0));
    assertThatThrownBy(IllegalArgumentException.class, () -> s3Service.generatePresignedUrl(key, 10081));
  }

  @Test
  @DisplayName("setBucketPolicy throws exception for null bucket name")
  void setBucketPolicyThrowsExceptionForNullBucketName() {
    when(s3Properties.getBucketName()).thenReturn("test-bucket");
    when(s3Properties.getRegion()).thenReturn("us-east-1");
    
    String policy = "{\"Version\":\"2012-10-17\",\"Statement\":[]}";

    assertThatThrownBy(IllegalArgumentException.class, () -> s3Service.setBucketPolicy(null, policy));
  }

  @Test
  @DisplayName("setBucketPolicy throws exception for null policy")
  void setBucketPolicyThrowsExceptionForNullPolicy() {
    when(s3Properties.getBucketName()).thenReturn("test-bucket");
    when(s3Properties.getRegion()).thenReturn("us-east-1");
    
    String bucketName = "test-bucket";

    assertThatThrownBy(IllegalArgumentException.class, () -> s3Service.setBucketPolicy(bucketName, null));
  }
}
