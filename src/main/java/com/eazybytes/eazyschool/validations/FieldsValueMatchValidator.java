package com.eazybytes.eazyschool.validations;

import com.eazybytes.eazyschool.annotations.FieldsValueMatch;
import org.springframework.beans.BeanWrapperImpl;

import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;

public class FieldsValueMatchValidator
        implements ConstraintValidator<FieldsValueMatch, Object> {
//	Why second parameter as Object -> I'm going to perform this validation on the 2 fields, 
//	I cannot mention a String because String will give me one value of a field.
//	Instead, I will accept the entire POJO where I'm using this annotation and from 
//	that POJO I will try to fetch the fields that I need
    private String field;
    private String fieldMatch; //these 2 val we are populating in the POJO class

    @Override
    public void initialize(FieldsValueMatch constraintAnnotation) {
        this.field = constraintAnnotation.field();
        this.fieldMatch = constraintAnnotation.fieldMatch();
    }

    @Override
    public boolean isValid(Object value,ConstraintValidatorContext context) {
        Object fieldValue = new BeanWrapperImpl(value)
                .getPropertyValue(field);
        Object fieldMatchValue = new BeanWrapperImpl(value)
                .getPropertyValue(fieldMatch);
        if (fieldValue != null) {
            return fieldValue.equals(fieldMatchValue);
        } else {
            return fieldMatchValue == null;
        }
    }
}