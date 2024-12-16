package com.example.sm.minh.eshop.validators.constraints;

import java.lang.reflect.Field;

import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.ReflectionUtils;

import com.example.sm.minh.eshop.exceptions.ProductCategoryException;
import com.example.sm.minh.eshop.services.ProductCategoryService;
import com.example.sm.minh.eshop.validators.annotations.SlugAndNameUnique;

public class UniqueSlugAndNameConstraint implements ConstraintValidator<SlugAndNameUnique, Object> {
    @Autowired
    private ProductCategoryService productCategoriesSerVice;

    private String nameField;
    private String idField;
    private String SlugField;

    @Override
    public void initialize(SlugAndNameUnique constraintAnnotation) {
        this.nameField = constraintAnnotation.nameField();
        this.idField = constraintAnnotation.idField();
        this.SlugField = constraintAnnotation.SlugField();
        ConstraintValidator.super.initialize(constraintAnnotation);
    }

    @Override
    public boolean isValid(Object value, ConstraintValidatorContext context) {
        if (value == null) {
            return true;
        }

        Field idField = ReflectionUtils.findField(value.getClass(), this.idField);
        Field nameField = ReflectionUtils.findField(value.getClass(), this.nameField);
        Field slugField = ReflectionUtils.findField(value.getClass(), this.SlugField);

        if (idField == null || nameField == null || slugField == null) {
            return false;
        }

        ReflectionUtils.makeAccessible(idField);
        ReflectionUtils.makeAccessible(nameField);
        ReflectionUtils.makeAccessible(slugField);

        try {
            Integer id = (Integer) idField.get(value);
            String name = (String) nameField.get(value);
            String slug = (String) slugField.get(value);
            return this.productCategoriesSerVice.checkNameAndSlugUnique(
                    id, name.trim(), slug.trim(), nameField, slugField, context);
        } catch (IllegalAccessException | ProductCategoryException e) {
            throw new RuntimeException(e);
        }
    }
}
