package com.zcommerce.platform.invoice.config;

import static org.assertj.core.api.Assertions.*;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

// JUnit tests for S3Properties configuration class.
@DisplayName("S3Properties Tests")
public class S3PropertiesTest {

  private S3Properties s3Properties;

  @BeforeEach
  void setUp() {
    s3Properties = new S3Properties();
  }

  @Test
  @DisplayName("S3Properties creates with default values")
  void s3PropertiesCreatesWithDefaultValues() {
    assertThat(s3Properties).isNotNull();
    assertThat(s3Properties.getBucketName()).isNull();
    assertThat(s3Properties.getRegion()).isNull();
    assertThat(s3Properties.getAccessKey()).isNull();
    assertThat(s3Properties.getSecretKey()).isNull();
  }

  @Test
  @DisplayName("setBucketName and getBucketName work correctly")
  void setBucketNameAndGetBucketNameWorkCorrectly() {
    String bucketName = "test-bucket";
    s3Properties.setBucketName(bucketName);
    assertThat(s3Properties.getBucketName()).isEqualTo(bucketName);
  }

  @Test
  @DisplayName("setRegion and getRegion work correctly")
  void setRegionAndGetRegionWorkCorrectly() {
    String region = "us-east-1";
    s3Properties.setRegion(region);
    assertThat(s3Properties.getRegion()).isEqualTo(region);
  }

  @Test
  @DisplayName("setAccessKey and getAccessKey work correctly")
  void setAccessKeyAndGetAccessKeyWorkCorrectly() {
    String accessKey = "test-access-key";
    s3Properties.setAccessKey(accessKey);
    assertThat(s3Properties.getAccessKey()).isEqualTo(accessKey);
  }

  @Test
  @DisplayName("setSecretKey and getSecretKey work correctly")
  void setSecretKeyAndGetSecretKeyWorkCorrectly() {
    String secretKey = "test-secret-key";
    s3Properties.setSecretKey(secretKey);
    assertThat(s3Properties.getSecretKey()).isEqualTo(secretKey);
  }

  @Test
  @DisplayName("S3Properties handles null values correctly")
  void s3PropertiesHandlesNullValuesCorrectly() {
    s3Properties.setBucketName(null);
    s3Properties.setRegion(null);
    s3Properties.setAccessKey(null);
    s3Properties.setSecretKey(null);

    assertThat(s3Properties.getBucketName()).isNull();
    assertThat(s3Properties.getRegion()).isNull();
    assertThat(s3Properties.getAccessKey()).isNull();
    assertThat(s3Properties.getSecretKey()).isNull();
  }

  @Test
  @DisplayName("S3Properties handles empty strings correctly")
  void s3PropertiesHandlesEmptyStringsCorrectly() {
    s3Properties.setBucketName("");
    s3Properties.setRegion("");
    s3Properties.setAccessKey("");
    s3Properties.setSecretKey("");

    assertThat(s3Properties.getBucketName()).isEqualTo("");
    assertThat(s3Properties.getRegion()).isEqualTo("");
    assertThat(s3Properties.getAccessKey()).isEqualTo("");
    assertThat(s3Properties.getSecretKey()).isEqualTo("");
  }
}
