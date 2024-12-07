package com.example.dath.eshop.validator.constraint;

import java.lang.reflect.Field;
import java.util.Optional;

import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.ReflectionUtils;

import com.example.dath.eshop.service.ProductService;
import com.example.dath.eshop.validator.annotation.SkuAndNameUnique;

public class UniqueNameAndSkuConstraint implements ConstraintValidator<SkuAndNameUnique, Object> {
    @Autowired
    private ProductService productService;

    private String nameField;
    private String skuField;
    private String idField;

    @Override
    public void initialize(SkuAndNameUnique constraintAnnotation) {
        this.nameField = constraintAnnotation.nameField();
        this.skuField = constraintAnnotation.skuField();
        this.idField = constraintAnnotation.idField();
    }

    @Override
    public boolean isValid(Object value, ConstraintValidatorContext context) {
        if (value == null) {
            return true; // Không cần kiểm tra nếu giá trị là null
        }

        Optional<Object> idValue = getFieldValue(value, idField);
        Optional<Object> nameValue = getFieldValue(value, nameField);
        Optional<Object> skuValue = getFieldValue(value, skuField);

        if (nameValue.isEmpty() || skuValue.isEmpty()) {
            return true; // Nếu tên hoặc SKU không có giá trị, bỏ qua kiểm tra
        }

        String name = ((String) nameValue.get()).trim();
        String sku = ((String) skuValue.get()).trim();
        Integer id = idValue.map(val -> (Integer) val).orElse(null);

        boolean isValid = productService.checkNameAndSkuUnique(name, sku, id);

        if (!isValid) {
            context.disableDefaultConstraintViolation();
            context.buildConstraintViolationWithTemplate("{product.name.sku.unique}")
                    .addConstraintViolation();
        }

        return isValid;
    }

    /**
     * Lấy giá trị của một trường từ đối tượng thông qua Reflection.
     *
     * @param target     Đối tượng cần truy cập trường
     * @param fieldName  Tên trường
     * @return Giá trị của trường nếu tồn tại, nếu không trả về Optional.empty()
     */
    private Optional<Object> getFieldValue(Object target, String fieldName) {
        if (fieldName == null || fieldName.isEmpty()) {
            return Optional.empty();
        }

        Field field = ReflectionUtils.findField(target.getClass(), fieldName);
        if (field == null) {
            return Optional.empty();
        }

        ReflectionUtils.makeAccessible(field);

        try {
            return Optional.ofNullable(field.get(target));
        } catch (IllegalAccessException e) {
            return Optional.empty();
        }
    }
}
