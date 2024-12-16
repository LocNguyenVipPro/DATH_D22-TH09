package com.example.sm.minh.eshop.validators.annotations;

import java.lang.annotation.*;

import jakarta.validation.Constraint;
import jakarta.validation.Payload;

import com.example.sm.minh.eshop.validators.constraints.UniqueNamelAndSkuConstraint;

@Target({ElementType.TYPE})
@Retention(RetentionPolicy.RUNTIME)
@Constraint(validatedBy = UniqueNamelAndSkuConstraint.class)
@Documented
public @interface SkuAndNameUnique {
    String message() default "";

    Class<?>[] groups() default {};

    Class<? extends Payload>[] payload() default {};

    String nameField() default "";

    String skuField() default "";

    String idField() default "";
}
