package com.example.locnguyen.techzone.controllers;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.example.locnguyen.techzone.dto.ProductDTO;
import com.example.locnguyen.techzone.services.ProductService;

@RestController
public class ProductRestController {
    @Autowired
    private ProductService productService;

    @GetMapping("/products/load-product")
    public List<ProductDTO> loadingProduct(
            @RequestParam(value = "rangePrice", required = false) String rangePrice,
            @RequestParam(value = "rangeSalePercent", required = false) String rangeSalePercent,
            @RequestParam(value = "categoryId", required = false) String categoryId) {

        if (rangePrice == null || rangePrice.trim().isEmpty()) {
            rangePrice = null;
        }

        if (rangeSalePercent == null || rangeSalePercent.trim().isEmpty()) {
            rangeSalePercent = null;
        }

        if (categoryId == null || categoryId.trim().isEmpty()) {
            categoryId = null;
        }

        List<ProductDTO> listProduct = productService.findByPrice(rangePrice, rangeSalePercent, categoryId);
        List<ProductDTO> productContain = new ArrayList<>();

        for (ProductDTO product : listProduct) {
            if (product.getQuantityProduct() == null) product.setQuantityProduct(0L);
            product.getProduct().setListProductCategories(null);
            productContain.add(product);
        }

        return productContain;
    }
}
