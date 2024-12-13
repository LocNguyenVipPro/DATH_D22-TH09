package com.example.sm.minh.eshop.validators.constraints;

import com.example.sm.minh.eshop.services.UserService;
import com.example.sm.minh.eshop.validators.annotations.UserNameUnique;
import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.ReflectionUtils;

import java.lang.reflect.Field;

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
            return true;
        }

        Field idField = ReflectionUtils.findField(value.getClass(), this.idField);
        Field emailField = ReflectionUtils.findField(value.getClass(), this.emailField);

        if (idField == null || emailField == null) {
            return false;
        }

        ReflectionUtils.makeAccessible(idField);
        ReflectionUtils.makeAccessible(emailField);

        try {
            Integer id = (Integer) idField.get(value);
            String email = (String) emailField.get(value);
            boolean isValid= this.userService.checkUserNameUni(email.trim(),id);

            if (!isValid) {
                context.disableDefaultConstraintViolation();

                context.buildConstraintViolationWithTemplate("Your email is already exist, Please choose another email")
                        .addPropertyNode(this.emailField)
                        .addConstraintViolation();
            }

            return isValid;

        } catch (IllegalAccessException e) {
            throw new RuntimeException(e);
        }
    }
}
