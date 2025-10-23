package com.zcommerce.platform.config;

import com.zcommerce.platform.domain.entity.Category;
import com.zcommerce.platform.domain.entity.Product;
import com.zcommerce.platform.domain.repository.CategoryRepository;
import com.zcommerce.platform.domain.repository.ProductRepository;
import java.math.BigDecimal;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.stereotype.Component;

/**
 * Data Initializer for Demo and Testing Purposes
 * 
 * <p>This component is responsible for populating the database with sample/dummy data
 * to facilitate demonstration and testing of the Z-Commerce platform.</p>
 * 
 * <p><strong>IMPORTANT:</strong> This is for demo purposes only and should NOT be used
 * in production environments. The data generated includes:</p>
 * <ul>
 *   <li>Sample categories (Electronics, Clothing, Books)</li>
 *   <li>Sample products (iPhone, MacBook, T-Shirt, Programming Book)</li>
 * </ul>
 * 
 * <p>This component can be disabled by setting the property:
 * <code>app.data.initializer.enabled=false</code></p>
 * 
 * <p><strong>Usage:</strong></p>
 * <ul>
 *   <li>Development: Automatically runs to provide test data</li>
 *   <li>Demo: Provides sample data for showcasing features</li>
 *   <li>Testing: Ensures consistent test data across environments</li>
 * </ul>
 * 
 * @author Z-Commerce Team
 * @since 1.0.0
 * @see CommandLineRunner
 */
@Component
@Slf4j
@ConditionalOnProperty(name = "app.data.initializer.enabled", havingValue = "true", matchIfMissing = true)
public class DataInitializer implements CommandLineRunner {

  private final CategoryRepository categoryRepository;
  private final ProductRepository productRepository;

  public DataInitializer(
      CategoryRepository categoryRepository, ProductRepository productRepository) {
    this.categoryRepository = categoryRepository;
    this.productRepository = productRepository;
  }

  /**
   * Executes the data initialization process.
   * 
   * <p>This method runs automatically when the application starts and creates
   * sample data for demonstration and testing purposes.</p>
   * 
   * <p><strong>Note:</strong> This method should only be used in development,
   * testing, or demo environments. It will populate the database with dummy data
   * including categories and products.</p>
   * 
   * @param args command line arguments (unused)
   * @throws Exception if data initialization fails
   */
  @Override
  public void run(String... args) throws Exception {
    log.info("=== DEMO DATA INITIALIZER STARTING ===");
    log.info("Initializing sample data for demonstration and testing...");
    log.warn("WARNING: This is demo data - should not be used in production!");

    // Create sample categories and products for demo/testing
    log.info("Creating demo categories and products...");
    Category electronics = new Category();
    electronics.setName("Electronics");
    electronics.setDescription("Electronic devices and gadgets");
    electronics.setActive(true);
    categoryRepository.save(electronics);
    log.info("Created Electronics category");

    Category clothing = new Category();
    clothing.setName("Clothing");
    clothing.setDescription("Fashion and apparel");
    clothing.setActive(true);
    categoryRepository.save(clothing);
    log.info("Created Clothing category");

    Category books = new Category();
    books.setName("Books");
    books.setDescription("Books and literature");
    books.setActive(true);
    categoryRepository.save(books);
    log.info("Created Books category");

    // Create products (always create for testing)
    log.info("Creating products...");
    Product iphone = new Product();
    iphone.setName("iPhone 15");
    iphone.setDescription("Latest Apple smartphone");
    iphone.setPrice(new BigDecimal("999.99"));
    iphone.setStockQuantity(50);
    iphone.setSku("IPHONE15-001");
    iphone.setActive(true);
    iphone.setCategory(electronics);
    productRepository.save(iphone);
    log.info("Created iPhone 15 product");

    Product macbook = new Product();
    macbook.setName("MacBook Pro");
    macbook.setDescription("Apple laptop computer");
    macbook.setPrice(new BigDecimal("1999.99"));
    macbook.setStockQuantity(25);
    macbook.setSku("MBP-001");
    macbook.setActive(true);
    macbook.setCategory(electronics);
    productRepository.save(macbook);
    log.info("Created MacBook Pro product");

    Product tshirt = new Product();
    tshirt.setName("T-Shirt");
    tshirt.setDescription("Cotton t-shirt");
    tshirt.setPrice(new BigDecimal("29.99"));
    tshirt.setStockQuantity(100);
    tshirt.setSku("TSHIRT-001");
    tshirt.setActive(true);
    tshirt.setCategory(clothing);
    productRepository.save(tshirt);
    log.info("Created T-Shirt product");

    Product book = new Product();
    book.setName("Programming Book");
    book.setDescription("Learn Java programming");
    book.setPrice(new BigDecimal("49.99"));
    book.setStockQuantity(75);
    book.setSku("BOOK-001");
    book.setActive(true);
    book.setCategory(books);
    productRepository.save(book);
    log.info("Created Programming Book product");

    log.info("=== DEMO DATA INITIALIZATION COMPLETED ===");
    log.info("Sample data has been created for demonstration purposes");
    log.warn("Remember: This is demo data - disable in production environments!");
  }
}
