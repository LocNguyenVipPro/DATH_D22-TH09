package com.example.dath.eshop.validators.annotations;

import java.lang.annotation.*;
import jakarta.validation.Constraint;
import jakarta.validation.Payload;
import com.example.dath.eshop.validators.constraints.UniqueSlugAndNameConstraint;

@Target({ElementType.TYPE})
@Retention(RetentionPolicy.RUNTIME)
@Constraint(validatedBy = UniqueSlugAndNameConstraint.class)
@Documented
public @interface SlugAndNameUnique {

    // Cung cấp một thông điệp mặc định cho lỗi validation
    String message() default "Slug or Name must be unique";

    // Nhóm của validation, có thể sử dụng cho các nhóm khác nhau (ví dụ: @NotNull, @Size, ...)
    Class<?>[] groups() default {};

    // Payload, dùng để truyền thêm dữ liệu vào khi validation xảy ra (không phải lúc nào cũng cần thiết)
    Class<? extends Payload>[] payload() default {};

    // Tên trường trong đối tượng cần kiểm tra (có thể là "name", "title", ...)
    String nameField() default "";

    // Trường ID dùng để kiểm tra (có thể là "id")
    String idField() default "";

    // Trường Slug dùng để kiểm tra tính duy nhất
    String slugField() default "";
}
