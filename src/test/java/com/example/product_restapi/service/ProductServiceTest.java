package com.example.product_restapi.service;

import com.example.product_restapi.entity.Product;
import com.example.product_restapi.repository.ProductRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class ProductServiceTest {

    @Mock
    private ProductRepository productRepository;

    @InjectMocks
    private ProductService productService;

    @Test
    void addProduct_ShouldReturnSavedProduct() {
        // Arrange
        Product product = new Product(null, "mobile", 10.0);
        Product savedProduct = new Product(1L, "mobile", 10.0);
        when(productRepository.save(product)).thenReturn(savedProduct);
        // Act
        Product result = productService.addProduct(product);
        // Assert
        assertEquals(1L, result.getId());
        assertEquals("mobile", result.getName());
        assertEquals(10.0, result.getPrice());
    }
    @Test
    void getAllProducts_ShouldReturnAllProducts() {
        // Arrange
        Product product1 = new Product(1L, "Product 1", 10.0);
        Product product2 = new Product(2L, "Product 2", 20.0);
        when(productRepository.findAll()).thenReturn(Arrays.asList(product1, product2));
        // Act
        List<Product> products = productService.getAllProducts();
        // Assert
        assertEquals(2, products.size());
    }
    @Test
    void deleteProduct_ShouldCallRepositoryDelete() {
        // Arrange
        Long productId = 1L;
        // Act
        productService.deleteProduct(productId);
        // Assert
        verify(productRepository).deleteById(productId);
    }
    @Test
    void getProductsByIds_ShouldReturnMatchingProducts() {
        // Arrange
        Product product1 = new Product(1L, "Product 1", 10.0);
        Product product2 = new Product(2L, "Product 2", 20.0);
        when(productRepository.findAllById(Arrays.asList(1L, 2L))).thenReturn
                (Arrays.asList(product1, product2));
        // Act
        List<Product> products = productService.getProductsByIds(Arrays.asList(1L, 2L));
        // Assert
        assertEquals(2, products.size());
    }
    @Test
    void updateProduct_WhenProductExists_ShouldReturnUpdatedProduct() {
        // Arrange
        Product existingProduct = new Product(1L, "mac laptop", 10.0);
        Product updatedProduct = new Product(null, "asus laptop", 20.0);
        when(productRepository.findById(1L)).thenReturn(Optional.of(existingProduct));
        when(productRepository.save(existingProduct)).thenReturn(existingProduct);
        // Act
        Product result = productService.updateProduct(1L, updatedProduct);
        // Assert
        assertEquals(1L, result.getId());
        assertEquals("asus laptop", result.getName());
        assertEquals(20.0, result.getPrice());
    }
    @Test
    void updateProduct_WhenProductNotExists_ShouldThrowException() {
        // Arrange
        when(productRepository.findById(1L)).thenReturn(Optional.empty());
        // Act & Assert
        assertThrows(RuntimeException.class, () -> {
            productService.updateProduct(1L, new Product());
        });
    }
}