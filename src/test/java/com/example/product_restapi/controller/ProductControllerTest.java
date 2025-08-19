package com.example.product_restapi.controller;

import com.example.product_restapi.entity.Product;
import com.example.product_restapi.entity.UserInfo;
import com.example.product_restapi.service.ProductService;
import com.example.product_restapi.service.UserInfoDetailService;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.test.context.support.WithMockUser;
import java.util.Arrays;
import java.util.List;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class ProductControllerTest {

    @Mock
    private ProductService productService;

    @Mock
    private UserInfoDetailService userInfoDetailService;

    @InjectMocks
    private ProductController productController;

    @Test
    @WithMockUser(roles = "ADMIN")
    void addProductItem_WithAdminRole_ShouldReturnProduct() {
        // Arrange
        Product product = new Product(null, "mobile", 10.0);
        Product savedProduct = new Product(1L, "mobile", 10.0);
        when(productService.addProduct(product)).thenReturn(savedProduct);
        // Act
        Product response = productController.addProductItem(product);
        // Assert
        assertEquals(1L, response.getId());
    }
    @Test
    @WithMockUser(roles = "USER")
    void addProductItem_WithUserRole_ShouldReturnForbidden() {
        // Act & Assert
        assertThrows(org.springframework.security.access.AccessDeniedException.class, () -> {
            productController.addProductItem(new Product());
        });
    }
    @Test
    @WithMockUser(roles = {"USER", "ADMIN"})
    void getAllProductItems_WithValidRoles_ShouldReturnProducts() {
        // Arrange
        Product product1 = new Product(1L, "Product 1", 10.0);
        Product product2 = new Product(2L, "Product 2", 20.0);
        when(productService.getAllProducts()).thenReturn(Arrays.asList(product1, product2));
        // Act
        List<Product> response = productController.getAllProductItems();
        // Assert
        assertEquals(2, response.size());
    }
    @Test
    void getAllProductItems_WithoutAuthentication_ShouldReturnUnauthorized() {
        // Act & Assert
        assertThrows(org.springframework.security.access.AccessDeniedException.class, () -> {
            productController.getAllProductItems();
        });
    }
    @Test
    @WithMockUser(roles = {"USER", "ADMIN"})
    void getProductsByIds_WithValidRoles_ShouldReturnProducts() {
        // Arrange
        Product product1 = new Product(1L, "Product 1", 10.0);
        Product product2 = new Product(2L, "Product 2", 20.0);
        when(productService.getProductsByIds(Arrays.asList(1L, 2L))).thenReturn(Arrays.asList(product1, product2));
        // Act
        List<Product> response = productController.getProductsByIds(Arrays.asList(1L, 2L));
        // Assert
        assertEquals(2, response.size());
    }
    @Test
    @WithMockUser(roles = "ADMIN")
    void deleteProductItem_WithAdminRole_ShouldCallService() {
        // Arrange
        Long productId = 1L;
        // Act
        productController.deleteProductItem(productId);
        // Assert
        verify(productService).deleteProduct(productId);
    }
    @Test
    @WithMockUser(roles = "ADMIN")
    void updateProductItem_WithAdminRole_ShouldReturnUpdatedProduct() {
        // Arrange
        Product product = new Product(null, "Updated Product", 15.0);
        Product updatedProduct = new Product(1L, "Updated Product", 15.0);
        when(productService.updateProduct(1L, product)).thenReturn(updatedProduct);
        // Act
        Product response = productController.updateProductItem(1L, product);
        // Assert
        assertEquals("Updated Product", response.getName());
    }
    @Test
    void addNewUser_ShouldCallServiceAndReturnSuccessMessage() {
        // Arrange
        UserInfo userInfo = new UserInfo();
        userInfo.setUsername("samira");
        when(userInfoDetailService.addUser(userInfo)).thenReturn("User added to system successfully.");
        // Act
        String result = productController.addNewUser(userInfo);
        // Assert
        assertEquals("User added to system successfully.", result);
    }
}