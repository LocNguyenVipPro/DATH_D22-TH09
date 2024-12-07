package com.example.dath.eshop.validator.annotation;

import java.lang.annotation.*;

import jakarta.validation.Constraint;
import jakarta.validation.Payload;
import com.example.dath.eshop.validator.constraint.UniqueNameAndSkuConstraint;

@Target({ElementType.TYPE})
@Retention(RetentionPolicy.RUNTIME)
@Constraint(validatedBy = UniqueNameAndSkuConstraint.class)
@Documented
public @interface SkuAndNameUnique {

    // Cung cấp thông điệp mặc định rõ ràng khi lỗi xảy ra
    String message() default "Product name or SKU must be unique";

    // Nhóm validation (có thể sử dụng để phân nhóm validation)
    Class<?>[] groups() default {};

    // Payload, cho phép truyền thêm dữ liệu khi validation xảy ra (thường không cần thiết)
    Class<? extends Payload>[] payload() default {};

    // Tên trường "name" của đối tượng cần kiểm tra tính duy nhất
    String nameField() default "name";

    // Tên trường "sku" của đối tượng cần kiểm tra tính duy nhất
    String skuField() default "sku";

    // Trường ID (để có thể bỏ qua đối tượng hiện tại khi cập nhật, ví dụ: id != null)
    String idField() default "id";
}
