package com.example.dath.eshop.validators.annotations;

import java.lang.annotation.*;

import jakarta.validation.Constraint;
import jakarta.validation.Payload;
import com.example.dath.eshop.validators.constraints.UniqueEmailConstraint;

@Target({ElementType.TYPE})
@Retention(RetentionPolicy.RUNTIME)
@Constraint(validatedBy = UniqueEmailConstraint.class)
@Documented
public @interface UserNameUnique {

    // Cung cấp thông điệp mặc định rõ ràng khi có lỗi validation
    String message() default "Email address is already taken. Please try a different one.";

    // Các nhóm validation (có thể sử dụng cho việc phân nhóm validation)
    Class<?>[] groups() default {};

    // Payload (thường không sử dụng, nhưng có thể để truyền thêm dữ liệu khi validation xảy ra)
    Class<? extends Payload>[] payload() default {};

    // Tên trường "id" để phân biệt khi người dùng đang cập nhật, không kiểm tra tính duy nhất với chính bản thân
    String idField() default "id";

    // Tên trường email trong đối tượng cần kiểm tra tính duy nhất
    String emailField() default "email";
}
