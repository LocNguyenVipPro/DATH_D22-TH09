package com.example.locnguyen.techzone.validators.annotations;

import java.lang.annotation.*;

import jakarta.validation.Constraint;
import jakarta.validation.Payload;

import com.example.locnguyen.techzone.validators.constraints.UniqueSlugAndNameConstraint;

@Target({ElementType.TYPE})
@Retention(RetentionPolicy.RUNTIME)
@Constraint(validatedBy = UniqueSlugAndNameConstraint.class)
@Documented
public @interface SlugAndNameUnique {
    String message() default "";

    Class<?>[] groups() default {};

    Class<? extends Payload>[] payload() default {};

    String nameField() default "";

    String idField() default "";

    String SlugField() default "";
}
