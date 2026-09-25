package com.example.ecommerce.service.service;

import com.example.ecommerce.common.exception.ProductNotFoundException;
import com.example.ecommerce.common.model.Product;
import com.example.ecommerce.service.repository.ProductRepository;

import java.math.BigDecimal;
import java.util.List;

public class ProductService {

    private final ProductRepository productRepository;

    public ProductService(ProductRepository productRepository) {
        this.productRepository = productRepository;
    }

    public Product createProduct(String id, String name, BigDecimal price, int stock, String category) {
        if (productRepository.existsById(id)) {
            throw new IllegalArgumentException("Product already exists: " + id);
        }
        Product product = new Product(id, name, price, stock, category);
        return productRepository.save(product);
    }

    public Product getProduct(String id) {
        return productRepository.findById(id)
                .orElseThrow(() -> new ProductNotFoundException(id));
    }

    public List<Product> listAll() {
        return productRepository.findAll();
    }

    public List<Product> listByCategory(String category) {
        return productRepository.findByCategory(category);
    }

    public Product updatePrice(String id, BigDecimal newPrice) {
        Product product = getProduct(id);
        product.setPrice(newPrice);
        return productRepository.save(product);
    }

    public Product restock(String id, int quantity) {
        Product product = getProduct(id);
        product.increaseStock(quantity);
        return productRepository.save(product);
    }

    public void deleteProduct(String id) {
        if (!productRepository.existsById(id)) {
            throw new ProductNotFoundException(id);
        }
        productRepository.deleteById(id);
    }

    public boolean isAvailable(String id, int quantity) {
        return getProduct(id).hasStock(quantity);
    }
}
