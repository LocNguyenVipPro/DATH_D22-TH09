package com.example.dath.eshop.mapper;

import com.example.dath.eshop.model.Product;
import com.example.dath.eshop.request.ProductRequest;

public class ProductRequestMapper {

    // Convert ProductRequest to Product
    public static Product toProduct(ProductRequest productRequest) {
        if (productRequest == null) {
            return null;
        }

        Product product = new Product();

        // Gán các giá trị từ productRequest sang product
        product.setId(productRequest.getId());
        product.setIsActive(productRequest.getIsActive());
        product.setContent(productRequest.getContent());
        product.setSku(productRequest.getSku());
        product.setName(productRequest.getName());
        product.setTax(productRequest.getTax());
        product.setPrice(productRequest.getPrice());
        product.setDiscountPrice(productRequest.getDiscountPrice());
        product.setListProductCategories(productRequest.getListProductCategories());

        return product;
    }

    // Convert Product to ProductRequest
    public static ProductRequest toProductRequest(Product product) {
        if (product == null) {
            return null;
        }

        ProductRequest productRequest = new ProductRequest();

        // Gán các giá trị từ product sang productRequest
        productRequest.setId(product.getId());
        productRequest.setIsActive(product.getIsActive());
        productRequest.setImage(product.getImage());
        productRequest.setContent(product.getContent());
        productRequest.setSku(product.getSku());
        productRequest.setName(product.getName());
        productRequest.setTax(product.getTax());
        productRequest.setPrice(product.getPrice());
        productRequest.setDiscountPrice(product.getDiscountPrice());
        productRequest.setListProductCategories(product.getListProductCategories());

        return productRequest;
    }
}
