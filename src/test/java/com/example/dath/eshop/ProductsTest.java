package com.example.dath.eshop;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import com.example.dath.eshop.model.Product;
import com.example.dath.eshop.repository.ProductCategoryRepository;
import com.example.dath.eshop.repository.ProductRepository;
import com.example.dath.eshop.service.ProductService;

@SpringBootTest
public class ProductsTest {
    @Autowired
    private ProductService productSerVice;

    @Autowired
    private ProductRepository producsRepository;

    @Autowired
    ProductCategoryRepository productCategoryRepository;

    @Test
    public void CreateFiveProducst() {
        Product product1 = new Product("Product 1", "SKU001", "Product 1 description", 100.0f, 0.0f, 10.0f, 100);
        Product product2 = new Product("Product 2", "SKU002", "Product 2 description", 120.0f, 90.0f, 0.0f, 200);
        Product product3 = new Product("Product 3", "SKU003", "Product 3 description", 80.0f, 0.0f, 8.0f, 150);

        product1.addProductCate(this.productCategoryRepository.findById(1).get());
        product2.addProductCate(this.productCategoryRepository.findById(1).get());
        product3.addProductCate(this.productCategoryRepository.findById(2).get());

        this.producsRepository.save(product1);
        this.producsRepository.save(product2);
        this.producsRepository.save(product3);
    }
}
