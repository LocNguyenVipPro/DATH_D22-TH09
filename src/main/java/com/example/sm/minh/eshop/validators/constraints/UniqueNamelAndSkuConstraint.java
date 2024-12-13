package com.example.sm.minh.eshop.validators.constraints;

import com.example.sm.minh.eshop.services.ProductService;
import com.example.sm.minh.eshop.validators.annotations.SkuAndNameUnique;
import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.ReflectionUtils;

import java.lang.reflect.Field;

public class UniqueNamelAndSkuConstraint implements ConstraintValidator<SkuAndNameUnique,Object> {
    @Autowired
    private ProductService productService;
    private String nameField;
    private String skuField;
    private String idField;
    @Override
    public void initialize(SkuAndNameUnique constraintAnnotation) {
        nameField= constraintAnnotation.nameField();
        skuField= constraintAnnotation.skuField();
        idField= constraintAnnotation.idField();
        ConstraintValidator.super.initialize(constraintAnnotation);
    }

    @Override
    public boolean isValid(Object value, ConstraintValidatorContext context) {
        if (value == null) {
            return true;
        }

        Field idField = ReflectionUtils.findField(value.getClass(), this.idField);
        Field nameField = ReflectionUtils.findField(value.getClass(), this.nameField);
        Field skuField = ReflectionUtils.findField(value.getClass(), this.skuField);

        if (idField == null || nameField == null||skuField == null) {
            return false;
        }

        ReflectionUtils.makeAccessible(idField);
        ReflectionUtils.makeAccessible(nameField);
        ReflectionUtils.makeAccessible(skuField);

        try {
            Integer id = (Integer) idField.get(value);
            String name = (String) nameField.get(value);
            String sku = (String) skuField.get(value);
            return this.productService.checkNameAndSkuUnique(name.trim(),sku.trim(),id,this.nameField,this.skuField,context);
        } catch (IllegalAccessException e) {
            throw new RuntimeException(e);
        }
    }

}
