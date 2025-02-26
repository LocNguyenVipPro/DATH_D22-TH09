package com.example.locnguyen.techzone.validators.annotations;

import java.lang.annotation.*;

import jakarta.validation.Constraint;
import jakarta.validation.Payload;

import com.example.locnguyen.techzone.validators.constraints.UniqueEmailConstraint;

@Target({ElementType.TYPE})
@Retention(RetentionPolicy.RUNTIME)
@Constraint(validatedBy = UniqueEmailConstraint.class)
@Documented
public @interface UserNameUnique {
    String message() default "";

    Class<?>[] groups() default {};

    Class<? extends Payload>[] payload() default {};

    String idField() default "";

    String emailField() default "";
}
