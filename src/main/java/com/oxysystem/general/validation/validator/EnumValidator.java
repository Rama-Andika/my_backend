package com.oxysystem.general.validation.validator;

import com.oxysystem.general.validation.annotation.ValidEnum;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;
import java.util.Arrays;

public class EnumValidator implements ConstraintValidator<ValidEnum, String> {
    private Class<? extends Enum<?>> enumClass;
    @Override
    public void initialize(ValidEnum constraintAnnotation) {
       this.enumClass = constraintAnnotation.enumClass();
    }

    @Override
    public boolean isValid(String value, ConstraintValidatorContext context) {
        if (value == null || enumClass == null){
            return false;
        }
        return Arrays.stream(enumClass.getEnumConstants()).anyMatch(anEnum -> anEnum.name().equals(value));
    }
}
