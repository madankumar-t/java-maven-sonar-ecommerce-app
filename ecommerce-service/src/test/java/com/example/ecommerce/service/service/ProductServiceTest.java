package com.example.ecommerce.service.service;

import com.example.ecommerce.common.exception.ProductNotFoundException;
import com.example.ecommerce.common.model.Product;
import com.example.ecommerce.service.repository.ProductRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.math.BigDecimal;
import java.util.Arrays;
import java.util.Collections;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class ProductServiceTest {

    @Mock
    private ProductRepository productRepository;

    private ProductService productService;

    @BeforeEach
    void setUp() {
        productService = new ProductService(productRepository);
    }

    @Test
    void createProductSavesNewProduct() {
        when(productRepository.existsById("p1")).thenReturn(false);
        when(productRepository.save(any(Product.class))).thenAnswer(invocation -> invocation.getArgument(0));

        Product created = productService.createProduct("p1", "Laptop", BigDecimal.TEN, 5, "Electronics");

        assertEquals("p1", created.getId());
        verify(productRepository).save(any(Product.class));
    }

    @Test
    void createProductRejectsDuplicateId() {
        when(productRepository.existsById("p1")).thenReturn(true);
        assertThrows(IllegalArgumentException.class,
                () -> productService.createProduct("p1", "Laptop", BigDecimal.TEN, 5, "Electronics"));
    }

    @Test
    void getProductReturnsExistingProduct() {
        Product product = new Product("p1", "Laptop", BigDecimal.TEN, 5, "Electronics");
        when(productRepository.findById("p1")).thenReturn(Optional.of(product));

        assertEquals(product, productService.getProduct("p1"));
    }

    @Test
    void getProductThrowsWhenMissing() {
        when(productRepository.findById("missing")).thenReturn(Optional.empty());
        assertThrows(ProductNotFoundException.class, () -> productService.getProduct("missing"));
    }

    @Test
    void listAllDelegatesToRepository() {
        Product product = new Product("p1", "Laptop", BigDecimal.TEN, 5, "Electronics");
        when(productRepository.findAll()).thenReturn(Collections.singletonList(product));

        assertEquals(1, productService.listAll().size());
    }

    @Test
    void listByCategoryDelegatesToRepository() {
        Product p1 = new Product("p1", "Laptop", BigDecimal.TEN, 5, "Electronics");
        Product p2 = new Product("p2", "Mouse", BigDecimal.ONE, 5, "Electronics");
        when(productRepository.findByCategory("Electronics")).thenReturn(Arrays.asList(p1, p2));

        assertEquals(2, productService.listByCategory("Electronics").size());
    }

    @Test
    void updatePriceChangesAndSavesProduct() {
        Product product = new Product("p1", "Laptop", BigDecimal.TEN, 5, "Electronics");
        when(productRepository.findById("p1")).thenReturn(Optional.of(product));
        when(productRepository.save(product)).thenReturn(product);

        Product updated = productService.updatePrice("p1", BigDecimal.valueOf(20));

        assertEquals(BigDecimal.valueOf(20), updated.getPrice());
        verify(productRepository).save(product);
    }

    @Test
    void restockIncreasesStockAndSaves() {
        Product product = new Product("p1", "Laptop", BigDecimal.TEN, 5, "Electronics");
        when(productRepository.findById("p1")).thenReturn(Optional.of(product));
        when(productRepository.save(product)).thenReturn(product);

        Product updated = productService.restock("p1", 3);

        assertEquals(8, updated.getStockQuantity());
    }

    @Test
    void deleteProductRemovesExistingProduct() {
        when(productRepository.existsById("p1")).thenReturn(true);
        productService.deleteProduct("p1");
        verify(productRepository, times(1)).deleteById("p1");
    }

    @Test
    void deleteProductThrowsWhenMissing() {
        when(productRepository.existsById("missing")).thenReturn(false);
        assertThrows(ProductNotFoundException.class, () -> productService.deleteProduct("missing"));
    }

    @Test
    void isAvailableReturnsStockStatus() {
        Product product = new Product("p1", "Laptop", BigDecimal.TEN, 5, "Electronics");
        when(productRepository.findById("p1")).thenReturn(Optional.of(product));

        assertTrue(productService.isAvailable("p1", 5));
        assertFalse(productService.isAvailable("p1", 6));
    }
}
