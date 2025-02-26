package com.example.locnguyen.techzone.dto;

import com.example.locnguyen.techzone.models.Product;

import lombok.Getter;

@Getter
public class ProductDTO {
    private Product product;
    private Long quantityProduct;

    public Product getProduct() {
        return product;
    }

    public Long getQuantityProduct() {
        return quantityProduct;
    }

    public void setProduct(Product product) {
        this.product = product;
    }

    public void setQuantityProduct(Long quantityProduct) {
        this.quantityProduct = quantityProduct;
    }

    public ProductDTO(Product product, Long quantiryPurchase) {
        this.product = product;
        this.quantityProduct = quantiryPurchase;
    }
}
