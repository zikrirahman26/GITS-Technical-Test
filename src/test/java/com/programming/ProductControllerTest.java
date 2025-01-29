package com.programming;

import com.programming.dto.ProductRequest;
import com.programming.dto.ProductResponse;
import com.programming.service.ProductService;
import com.programming.controller.ProductController;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@ExtendWith(MockitoExtension.class)
public class ProductControllerTest {

    @InjectMocks
    private ProductController productController;

    @Mock
    private ProductService productService;

    private MockMvc mockMvc;

    @BeforeEach
    public void setUp() {
        mockMvc = MockMvcBuilders.standaloneSetup(productController).build();
    }

    @Test
    void testAddProduct() throws Exception {
        ProductRequest productRequest = new ProductRequest("Product 1", 100.0, "Category1", "Description");

        doNothing().when(productService).addProduct(any(ProductRequest.class));

        mockMvc.perform(post("/api/products")
                        .contentType("application/json")
                        .content("{\"name\":\"Product 1\",\"price\":100.0,\"category\":\"Category1\",\"description\":\"Description\"}"))
                .andExpect(status().isOk())
                .andExpect(content().string("Successfully Added"));

        verify(productService, times(1)).addProduct(any(ProductRequest.class));
    }

    @Test
    void testGetProductById() throws Exception {
        ProductResponse productResponse = new ProductResponse("Product 1", "SKU1234", "image.jpg", 100.0, "Category1", "Description");

        when(productService.getProduct(1L)).thenReturn(productResponse);

        mockMvc.perform(get("/api/products/1"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.name").value("Product 1"))
                .andExpect(jsonPath("$.skuCode").value("SKU1234"))
                .andExpect(jsonPath("$.price").value(100.0));

        verify(productService, times(1)).getProduct(1L);
    }

    @Test
    void testUpdateProduct() throws Exception {
        ProductRequest productRequest = new ProductRequest("Updated Product", 120.0, "Category1", "Updated Description");

        doNothing().when(productService).updateProduct(any(ProductRequest.class), eq(1L));

        mockMvc.perform(patch("/api/products/1")
                        .contentType("application/json")
                        .content("{\"name\":\"Updated Product\",\"price\":120.0,\"category\":\"Category1\",\"description\":\"Updated Description\"}"))
                .andExpect(status().isOk())
                .andExpect(content().string("Successfully Updated"));

        verify(productService, times(1)).updateProduct(any(ProductRequest.class), eq(1L));
    }

    @Test
    void testDeleteProduct() throws Exception {
        doNothing().when(productService).deleteProduct(1L);

        mockMvc.perform(delete("/api/products/1"))
                .andExpect(status().isOk())
                .andExpect(content().string("Successfully Deleted"));

        verify(productService, times(1)).deleteProduct(1L);
    }
}
