package com.example.dath.eshop.validators.annotations;

import com.example.dath.eshop.validators.constraints.UniqueSlugAndNameConstraint;
import jakarta.validation.Constraint;
import jakarta.validation.Payload;

import java.lang.annotation.*;


@Target({ElementType.TYPE})
@Retention(RetentionPolicy.RUNTIME)
@Constraint(validatedBy = UniqueSlugAndNameConstraint.class)
@Documented
public @interface SlugAndNameUnique {
    String message() default "";
    Class<?>[] groups() default {};
    Class<? extends Payload>[] payload() default {};

    String nameField()default "";
    String idField()default "";
    String SlugField()default "";
}
