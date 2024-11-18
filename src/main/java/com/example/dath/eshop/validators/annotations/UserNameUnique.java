package com.example.dath.eshop.validators.annotations;

import com.example.dath.eshop.validators.constraints.UniqueEmailConstraint;
import jakarta.validation.Constraint;
import jakarta.validation.Payload;

import java.lang.annotation.*;

@Target({ElementType.TYPE})
@Retention(RetentionPolicy.RUNTIME)
@Constraint(validatedBy = UniqueEmailConstraint.class)
@Documented
public @interface UserNameUnique {
    String message() default "";
    Class<?>[] groups() default {};
    Class<? extends Payload>[] payload() default {};

    String idField()default "";
    String emailField()default "";
}


