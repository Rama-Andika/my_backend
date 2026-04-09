package com.oxysystem.general.validation.validator;

import com.oxysystem.general.validation.annotation.ValidNIK;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;

public class NIKValidator implements ConstraintValidator<ValidNIK, String> {
    @Override
    public void initialize(ValidNIK constraintAnnotation) {
        ConstraintValidator.super.initialize(constraintAnnotation);
    }

    @Override
    public boolean isValid(String value, ConstraintValidatorContext context) {
        if(value == null || value.isEmpty()){
            return false;
        }

        if(value.length() != 16 || !value.matches("\\d+")) return false;

        return true;
    }
}
