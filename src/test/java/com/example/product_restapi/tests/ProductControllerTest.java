package com.example.product_restapi.tests;

import com.example.product_restapi.controller.ProductController;
import com.example.product_restapi.entity.Product;
import com.example.product_restapi.entity.UserInfo;
import com.example.product_restapi.service.ProductService;
import com.example.product_restapi.service.UserInfoDetailService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import java.util.Arrays;
import java.util.List;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

class ProductControllerTest {

    private MockMvc mockMvc;

    @Mock
    private ProductService productService;

    @Mock
    private UserInfoDetailService userInfoDetailService;

    @InjectMocks
    private ProductController productController;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        mockMvc = MockMvcBuilders.standaloneSetup(productController).build();
    }

    @Test
    @WithMockUser(roles = "ADMIN")
    void addProduct_ShouldReturnProduct() throws Exception {
        Product product = new Product(1L, "Test Product", 10.0);
        when(productService.addProduct(any(Product.class))).thenReturn(product);

        mockMvc.perform(post("/api/product/add")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{\"name\":\"Test Product\",\"price\":10.0}"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.name").value("Test Product"));

        verify(productService, times(1)).addProduct(any(Product.class));
    }

    @Test
    @WithMockUser(roles = {"USER", "ADMIN"})
    void getAllProducts_ShouldReturnProductList() throws Exception {
        List<Product> products = Arrays.asList(
                new Product(1L, "Product 1", 10.0),
                new Product(2L, "Product 2", 20.0)
        );
        when(productService.getAllProducts()).thenReturn(products);

        mockMvc.perform(get("/api/product/get"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.length()").value(2));

        verify(productService, times(1)).getAllProducts();
    }

    @Test
    @WithMockUser(roles = {"USER", "ADMIN"})
    void getProductsByIds_ShouldReturnProducts() throws Exception {
        List<Product> products = Arrays.asList(
                new Product(1L, "Product 1", 10.0)
        );
        when(productService.getProductsByIds(anyList())).thenReturn(products);

        mockMvc.perform(get("/api/product/getById")
                        .param("ids", "1"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.length()").value(1));

        verify(productService, times(1)).getProductsByIds(anyList());
    }

    @Test
    @WithMockUser(roles = "ADMIN")
    void deleteProduct_ShouldReturnNoContent() throws Exception {
        doNothing().when(productService).deleteProduct(anyLong());

        mockMvc.perform(delete("/api/product/delete/1"))
                .andExpect(status().isOk());

        verify(productService, times(1)).deleteProduct(1L);
    }

    @Test
    @WithMockUser(roles = "ADMIN")
    void updateProduct_ShouldReturnUpdatedProduct() throws Exception {
        Product updatedProduct = new Product(1L, "Updated Product", 15.0);
        when(productService.updateProduct(anyLong(), any(Product.class))).thenReturn(updatedProduct);

        mockMvc.perform(put("/api/product/update/1")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{\"name\":\"Updated Product\",\"price\":15.0}"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.name").value("Updated Product"));

        verify(productService, times(1)).updateProduct(anyLong(), any(Product.class));
    }

    @Test
    void addNewUser_ShouldReturnSuccessMessage() throws Exception {
        // Mock the behavior of userInfoDetailService
        when(userInfoDetailService.addUser(any(UserInfo.class))).thenReturn("user added to system");

        mockMvc.perform(post("/api/newUser")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{\"username\":\"newuser\",\"password\":\"password\"}"))
                .andExpect(status().isOk());

        // Verify the service was called
        verify(userInfoDetailService, times(1)).addUser(any(UserInfo.class));
    }
}