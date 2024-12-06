package com.example.dath.eshop.validators.constraints;

import java.lang.reflect.Field;

import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.ReflectionUtils;

import com.example.dath.eshop.exceptions.ProductCategoryException;
import com.example.dath.eshop.services.ProductCategoryService;
import com.example.dath.eshop.validators.annotations.SlugAndNameUnique;

public class UniqueSlugAndNameConstraint implements ConstraintValidator<SlugAndNameUnique, Object> {

    @Autowired
    private ProductCategoryService productCategoryService;

    private String nameField;
    private String idField;
    private String slugField;

    @Override
    public void initialize(SlugAndNameUnique constraintAnnotation) {
        this.nameField = constraintAnnotation.nameField();
        this.idField = constraintAnnotation.idField();
        this.slugField = constraintAnnotation.slugField();
    }

    @Override
    public boolean isValid(Object value, ConstraintValidatorContext context) {
        if (value == null) {
            return true; // Null values are considered valid
        }

        try {
            Integer id = (Integer) getField(value, idField);
            String name = (String) getField(value, nameField);
            String slug = (String) getField(value, slugField);

            if (name == null || slug == null) {
                return true; // Null fields are not considered as validation errors
            }

            // Convert String field names to actual Field objects
            Field nameFieldObj = ReflectionUtils.findField(value.getClass(), nameField);
            Field slugFieldObj = ReflectionUtils.findField(value.getClass(), slugField);

            if (nameFieldObj == null || slugFieldObj == null) {
                throw new RuntimeException("Field(s) not found on the target class");
            }

            return productCategoryService.checkNameAndSlugUnique(
                    id, name.trim(), slug.trim(), nameFieldObj, slugFieldObj, context);

        } catch (IllegalAccessException e) {
            throw new RuntimeException("Error accessing fields for validation", e);
        } catch (ProductCategoryException e) {
            context.disableDefaultConstraintViolation();
            context.buildConstraintViolationWithTemplate(e.getMessage()).addConstraintViolation();
            return false;
        }
    }

    /**
     * Helper method to get the value of a field via reflection.
     */
    private Object getField(Object target, String fieldName) throws IllegalAccessException {
        Field field = ReflectionUtils.findField(target.getClass(), fieldName);
        if (field == null) {
            throw new RuntimeException("Field '" + fieldName + "' not found on target class");
        }
        ReflectionUtils.makeAccessible(field);
        return field.get(target);
    }
}
