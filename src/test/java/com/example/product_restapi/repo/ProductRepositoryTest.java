package com.example.product_restapi.repo;

import com.example.product_restapi.entity.Product;
import com.example.product_restapi.repository.ProductRepository;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.test.context.ActiveProfiles;
import java.util.Arrays;
import java.util.List;
import static org.junit.jupiter.api.Assertions.*;

@DataJpaTest
@ActiveProfiles("test")
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
class ProductRepositoryTest {

    @Autowired
    private ProductRepository productRepository;

    @Test
    void findAllById_WhenProductsExist_ShouldReturnProducts() {
        // Arrange
        Product product1 = new Product(null, "Product 1", 10.0);
        Product product2 = new Product(null, "Product 2", 20.0);
        productRepository.saveAll(Arrays.asList(product1, product2));
        // Act
        List<Product> products = productRepository.findAllById
                (Arrays.asList(product1.getId(), product2.getId()));
        // Assert
        assertEquals(2, products.size());
    }
    @Test
    void findAllById_WhenProductsNotExist_ShouldReturnEmptyList() {

        List<Product> products = productRepository.findAllById(Arrays.asList(999L, 1000L));
        assertTrue(products.isEmpty());
    }
}
