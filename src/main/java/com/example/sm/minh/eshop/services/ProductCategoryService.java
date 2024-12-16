package com.example.sm.minh.eshop.services;

import java.io.IOException;
import java.lang.reflect.Field;
import java.util.Date;
import java.util.List;
import java.util.Optional;

import jakarta.transaction.Transactional;
import jakarta.validation.ConstraintValidatorContext;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import com.example.sm.minh.eshop.exceptions.ProductCategoryException;
import com.example.sm.minh.eshop.exceptions.ProductException;
import com.example.sm.minh.eshop.models.Product;
import com.example.sm.minh.eshop.models.ProductCategory;
import com.example.sm.minh.eshop.repositories.ProductCategoryRepository;
import com.example.sm.minh.eshop.repositories.ProductRepository;
import com.example.sm.minh.eshop.utilities.FileUploadUltil;

@Service
public class ProductCategoryService {

    @Autowired
    private ProductCategoryRepository productCategoryRepository;

    @Autowired
    private ProductRepository productRepository;

    @Autowired
    private ProductService productService;

    public List<ProductCategory> findAll(String searchValue, Boolean isHide) {

        if (searchValue != null && !searchValue.trim().isEmpty()) {
            return productCategoryRepository.findByNameContaining(searchValue, isHide);
        }

        return productCategoryRepository.findAll(isHide);
    }

    public ProductCategory saveImage(ProductCategory productCategory, MultipartFile multipartFile) throws IOException {

        if (multipartFile != null && !multipartFile.isEmpty()) {
            String fileName = multipartFile.getOriginalFilename();
            productCategory.setImage(fileName);
            this.productCategoryRepository.save(productCategory);
            String directory = "public/images/categories/" + productCategory.getId();
            FileUploadUltil.saveFile(directory, fileName, multipartFile, null);
        }
        return productCategory;
    }

    public void saveCategory(ProductCategory productCategory, MultipartFile multipartFile)
            throws IOException, ProductCategoryException {

        if (productCategory.getId() == null || productCategory.getId().equals(0)) {
            productCategory = trimCategory(productCategory);
            productCategory.setCreatedAt(new Date());
            productCategory = this.saveImage(productCategory, multipartFile);
        } else {
            ProductCategory existingCategory = this.productCategoryRepository
                    .findById(productCategory.getId())
                    .orElseThrow(() -> new ProductCategoryException("Category not found."));

            existingCategory = this.setDataForProductCategory(existingCategory, productCategory);
            productCategory = this.saveImage(existingCategory, multipartFile);
        }
        this.productCategoryRepository.save(productCategory);
    }

    public ProductCategory trimCategory(ProductCategory productCategory) {
        productCategory.setName(productCategory.getName().trim());
        productCategory.setDescription(productCategory.getDescription().trim());
        productCategory.setSlug(productCategory.getSlug().trim());
        return productCategory;
    }

    @Transactional
    public void deleteCategory(Integer categoryId) throws ProductCategoryException, ProductException {
        Optional<ProductCategory> categoryOptional = this.productCategoryRepository.findById(categoryId);
        ProductCategory deleteCategory = categoryOptional.orElseThrow(
                () -> new ProductCategoryException("Category with ID " + categoryId + " not found"));
        // unlink category from product
        List<Product> listProducts = this.productRepository.findAll(categoryId, true);

        for (Product product : listProducts) {
            productService.delete(product.getId());
        }

        deleteCategory.setDeletedAt(new Date());
        deleteCategory.setIsActive(false);
        this.productCategoryRepository.save(deleteCategory);
    }

    public ProductCategory findById(Integer id, Boolean isHide) throws ProductCategoryException {
        Optional<ProductCategory> categoryOptional = null;

        // check if category is deleted or no
        if (isHide == null) {
            categoryOptional = this.productCategoryRepository.findById(id);
        } else {
            categoryOptional = this.productCategoryRepository.findById(id, isHide);
        }

        return categoryOptional.orElseThrow(
                () -> new ProductCategoryException("Category with ID " + id + " not found"));
    }

    public boolean checkNameAndSlugUnique(
            Integer id, String name, String slug, Field nameField, Field slugField, ConstraintValidatorContext context)
            throws ProductCategoryException {
        String nameErrorMessage = null;
        String slugErrorMessage = null;

        // Check if the category already exists in the database
        if (id == null || id == 0) {
            if (this.productCategoryRepository.findByName(name) != null) {
                nameErrorMessage = "Category already exists with the provided name";
            } else if (this.productCategoryRepository.findBySlug(slug) != null) {
                slugErrorMessage = "Category already exists with the provided slug";
            }
        } else {

            // If the id exists, check if the name or slug already exists in the database (excluding the current
            // category)
            ProductCategory existingCategoryByName = this.productCategoryRepository.findByName(name);
            ProductCategory existingCategoryBySlug = this.productCategoryRepository.findBySlug(slug);
            Optional<ProductCategory> currentCategory = this.productCategoryRepository.findById(id);

            // If the current category is not found
            if (currentCategory.isEmpty()) {
                throw new ProductCategoryException("Product category with ID " + id + " not found");
            }

            // Check if the name already exists
            if (existingCategoryByName != null
                    && !existingCategoryByName.getId().equals(id)) {
                nameErrorMessage = "Category Name conflicts with existing products";
            }

            // Check if the slug already exists
            if (existingCategoryBySlug != null
                    && !existingCategoryBySlug.getId().equals(id)) {
                slugErrorMessage = "Category Slug conflicts with existing products";
            }
        }

        // If there are errors, assign error messages to each field
        if (nameErrorMessage != null) {
            context.disableDefaultConstraintViolation();
            context.buildConstraintViolationWithTemplate(nameErrorMessage)
                    .addPropertyNode(nameField.getName())
                    .addConstraintViolation();
        }

        if (slugErrorMessage != null) {
            context.disableDefaultConstraintViolation();
            context.buildConstraintViolationWithTemplate(slugErrorMessage)
                    .addPropertyNode(slugField.getName())
                    .addConstraintViolation();
        }

        // Return the validation result: true if there are no errors, false if there are errors
        return nameErrorMessage == null && slugErrorMessage == null;
    }

    public void restoreCategory(Integer id) throws ProductCategoryException {
        ProductCategory restoreCategory = this.findById(id, null);
        restoreCategory.setIsActive(true);
        restoreCategory.setDeletedAt(new Date());
        this.productCategoryRepository.save(restoreCategory);
    }

    public ProductCategory setDataForProductCategory(ProductCategory productCategories, ProductCategory EditCategory) {
        productCategories.setName(EditCategory.getName());
        productCategories.setSlug(EditCategory.getSlug());
        productCategories.setDescription(EditCategory.getDescription());
        productCategories.setUpdatedAt(new Date());
        return productCategories;
    }
}
