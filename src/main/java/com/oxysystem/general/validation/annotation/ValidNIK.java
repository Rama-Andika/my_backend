package com.oxysystem.general.validation.annotation;

import com.oxysystem.general.validation.validator.NIKValidator;

import javax.validation.Constraint;
import javax.validation.Payload;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Constraint(validatedBy = NIKValidator.class)
@Target({ElementType.FIELD, ElementType.PARAMETER})
@Retention(RetentionPolicy.RUNTIME)
public @interface ValidNIK {
    String message() default "invalid NIK format";
    Class<?>[] groups() default {};
    Class<? extends Payload>[] payload() default {};
}
