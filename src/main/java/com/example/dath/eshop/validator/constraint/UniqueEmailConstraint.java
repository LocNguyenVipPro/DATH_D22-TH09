package com.example.dath.eshop.validator.constraint;

import java.lang.reflect.Field;
import java.util.Optional;

import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.ReflectionUtils;

import com.example.dath.eshop.service.UserService;
import com.example.dath.eshop.validator.annotation.UserNameUnique;

public class UniqueEmailConstraint implements ConstraintValidator<UserNameUnique, Object> {
    private String idField;
    private String emailField;

    @Autowired
    private UserService userService;

    @Override
    public void initialize(UserNameUnique constraintAnnotation) {
        this.idField = constraintAnnotation.idField();
        this.emailField = constraintAnnotation.emailField();
    }

    @Override
    public boolean isValid(Object value, ConstraintValidatorContext context) {
        if (value == null) {
            return true; // Không cần kiểm tra khi giá trị là null
        }

        // Lấy giá trị từ các trường ID và email
        Optional<Object> idValue = getFieldValue(value, idField);
        Optional<Object> emailValue = getFieldValue(value, emailField);

        if (emailValue.isEmpty()) {
            return true; // Email không có giá trị để kiểm tra
        }

        String email = ((String) emailValue.get()).trim();
        Integer id = idValue.map(val -> (Integer) val).orElse(null);

        // Kiểm tra tính duy nhất của email
        boolean isValid = userService.checkUserNameUni(email, id);

        if (!isValid) {
            context.disableDefaultConstraintViolation();

            context.buildConstraintViolationWithTemplate("{user.email.unique}")
                    .addPropertyNode(this.emailField)
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
