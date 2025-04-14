package com.example.product_restapi.tests;

import com.example.product_restapi.entity.Product;
import com.example.product_restapi.repository.ProductRepository;
import com.example.product_restapi.service.ProductService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class ProductServiceTest {

    @Mock
    private ProductRepository productRepository;

    @InjectMocks
    private ProductService productService;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void addProduct_ShouldReturnSavedProduct() {
        Product product = new Product(null, "Test Product", 10.0);
        Product savedProduct = new Product(1L, "Test Product", 10.0);

        when(productRepository.save(product)).thenReturn(savedProduct);

        Product result = productService.addProduct(product);

        assertEquals(1L, result.getId());
        assertEquals("Test Product", result.getName());
        verify(productRepository, times(1)).save(product);
    }

    @Test
    void getAllProducts_ShouldReturnAllProducts() {
        List<Product> products = Arrays.asList(
                new Product(1L, "Product 1", 10.0),
                new Product(2L, "Product 2", 20.0)
        );

        when(productRepository.findAll()).thenReturn(products);

        List<Product> result = productService.getAllProducts();

        assertEquals(2, result.size());
        verify(productRepository, times(1)).findAll();
    }

    @Test
    void getProductsByIds_ShouldReturnRequestedProducts() {
        List<Product> products = Arrays.asList(
                new Product(1L, "Product 1", 10.0)
        );

        when(productRepository.findAllById(Arrays.asList(1L))).thenReturn(products);

        List<Product> result = productService.getProductsByIds(Arrays.asList(1L));

        assertEquals(1, result.size());
        verify(productRepository, times(1)).findAllById(anyList());
    }

    @Test
    void deleteProduct_ShouldCallRepositoryDelete() {
        doNothing().when(productRepository).deleteById(1L);

        productService.deleteProduct(1L);

        verify(productRepository, times(1)).deleteById(1L);
    }

    @Test
    void updateProduct_ShouldUpdateExistingProduct() {
        Product existingProduct = new Product(1L, "Old Name", 10.0);
        Product updatedDetails = new Product(null, "New Name", 15.0);
        Product savedProduct = new Product(1L, "New Name", 15.0);

        when(productRepository.findById(1L)).thenReturn(Optional.of(existingProduct));
        when(productRepository.save(existingProduct)).thenReturn(savedProduct);

        Product result = productService.updateProduct(1L, updatedDetails);

        assertEquals("New Name", result.getName());
        assertEquals(15.0, result.getPrice());
        verify(productRepository, times(1)).findById(1L);
        verify(productRepository, times(1)).save(existingProduct);
    }
}