package com.programming;

import com.programming.dto.ProductRequest;
import com.programming.dto.ProductResponse;
import com.programming.entity.Product;
import com.programming.repository.ProductRepository;
import com.programming.service.ValidationService;
import com.programming.service.impl.ProductServiceImpl;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.web.server.ResponseStatusException;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

public class ProductServiceImplTest {

    @InjectMocks
    private ProductServiceImpl productService;

    @Mock
    private ProductRepository productRepository;

    @Mock
    private ValidationService validationService;

    private Product product;

    @BeforeEach
    public void setUp() {
        MockitoAnnotations.openMocks(this);
        product = new Product(1L, "Product 1", "SKU1234", "image.jpg", 100.0, "Category1", "Description");
    }

    @Test
    void testAddProduct() {
        ProductRequest productRequest = new ProductRequest("Product 1", 100.0, "Category1", "Description");

        doNothing().when(validationService).validate(any());

        when(productRepository.save(any(Product.class))).thenReturn(product);

        productService.addProduct(productRequest);

        verify(productRepository, times(1)).save(any(Product.class));
    }

    @Test
    void testGetProduct() {
        when(productRepository.findById(1L)).thenReturn(Optional.of(product));

        ProductResponse response = productService.getProduct(1L);

        assertNotNull(response);
        assertEquals("Product 1", response.getName());
        assertEquals("SKU1234", response.getSkuCode());
        assertEquals(100.0, response.getPrice());
    }

    @Test
    void testGetProductNotFound() {
        when(productRepository.findById(1L)).thenReturn(Optional.empty());

        ResponseStatusException exception = assertThrows(ResponseStatusException.class, () -> productService.getProduct(1L));

        assertEquals("404 NOT_FOUND \"Product not found\"", exception.getMessage());
    }

    @Test
    void testUpdateProduct() {
        ProductRequest productRequest = new ProductRequest("Updated Product", 120.0, "Category1", "Updated Description");

        when(productRepository.findById(1L)).thenReturn(Optional.of(product));
        when(productRepository.save(any(Product.class))).thenReturn(product);

        productService.updateProduct(productRequest, 1L);

        verify(productRepository, times(1)).save(any(Product.class));
    }

    @Test
    void testDeleteProduct() {
        when(productRepository.findById(1L)).thenReturn(Optional.of(product));

        productService.deleteProduct(1L);

        verify(productRepository, times(1)).deleteById(1L);
    }

    @Test
    void testDeleteProductNotFound() {
        when(productRepository.findById(1L)).thenReturn(Optional.empty());

        ResponseStatusException exception = assertThrows(ResponseStatusException.class, () -> productService.deleteProduct(1L));

        assertEquals("404 NOT_FOUND \"Product not found\"", exception.getMessage());
    }
}
