package com.example.dath.eshop.mapper;

import com.example.dath.eshop.model.ProductCategory;
import com.example.dath.eshop.request.CategoryRequest;

public class CategoryRequestMapper {
    public static ProductCategory toCategory(CategoryRequest categoryRequest) {
        ProductCategory productCategory = new ProductCategory();
        productCategory.setId(categoryRequest.getId());
        productCategory.setIsActive(categoryRequest.getIsActive());
        productCategory.setDescription(categoryRequest.getDescription());
        productCategory.setSlug(categoryRequest.getSlug());
        productCategory.setName(categoryRequest.getName());
        return productCategory;
    }

    public static CategoryRequest toCategoryRequest(ProductCategory productCategory) {
        CategoryRequest categoryRequest = new CategoryRequest();
        categoryRequest.setId(productCategory.getId());
        categoryRequest.setIsActive(productCategory.getIsActive());
        categoryRequest.setDescription(productCategory.getDescription());
        categoryRequest.setImage(productCategory.getImage());
        categoryRequest.setSlug(productCategory.getSlug());
        categoryRequest.setName(productCategory.getName());
        return categoryRequest;
    }
}
