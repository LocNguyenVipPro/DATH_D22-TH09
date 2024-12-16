package com.example.sm.minh.eshop;

import java.util.ArrayList;
import java.util.List;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import com.example.sm.minh.eshop.models.Product;
import com.example.sm.minh.eshop.models.ProductCategory;
import com.example.sm.minh.eshop.repositories.ProductCategoryRepository;

@SpringBootTest
public class productCategoryTest {
    @Autowired
    ProductCategoryRepository productCategoryRepository;

    @Test
    public void Test() {
        ProductCategory productCategories =
                this.productCategoryRepository.findById(1).get();
        for (Product product : productCategories.getListProducts()) {
            System.out.println(product);
        }
    }

    @Test
    public void findall() {
        List<ProductCategory> productCategories = this.productCategoryRepository.findAll();
        for (ProductCategory product : productCategories) {
            System.out.println(product);
        }
    }

    @Test
    public void CreateProducCategory() {
        ArrayList<ProductCategory> list = new ArrayList<>();
        ProductCategory Electronics = new ProductCategory(
                "Electronics",
                "electronics",
                "This category includes all electronic devices such as smartphones, laptops, and cameras.");
        ProductCategory Clothing = new ProductCategory(
                "Clothing",
                "clothing",
                "Find a wide range of clothing items here, from t-shirts and jeans to dresses and jackets.");
        ProductCategory Books = new ProductCategory(
                "Books",
                "books",
                "Explore our collection of books covering various genres from fiction to non-fiction");
        ProductCategory Home12 = new ProductCategory(
                "\tHome & Garden",
                "home-garden",
                "Everything you need for your home and garden, including furniture, decor, and tools..");
        ProductCategory Sports = new ProductCategory(
                "Sports\t",
                "sports",
                "\tGet your sports gear here, from equipment for various sports to activewear and accessories");
        list.addAll(List.of(Electronics, Clothing, Books, Home12, Sports));
        this.productCategoryRepository.saveAll(this.productCategoryRepository.saveAll(list));
    }
}
