package com.eazybytes.eazyschool.annotations;

import com.eazybytes.eazyschool.validations.FieldsValueMatchValidator;

import jakarta.validation.Constraint;
import jakarta.validation.Payload;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Constraint(validatedBy = FieldsValueMatchValidator.class)
@Target({ ElementType.TYPE }) // give me flexibility to me to mention this 
                              // annotation on top of my Java class itself.
@Retention(RetentionPolicy.RUNTIME)
public @interface FieldsValueMatch {

    Class<?>[] groups() default {};
    Class<? extends Payload>[] payload() default {};

    String message() default "Fields values don't match!";

    /*
     * I am trying to validate the content of 2 fields since
     * I'm going to use this annotation to validate the 
     * content of 2 different fields, I need to write this.
     * */
    String field(); //these 2 values we are setting in the Person
    String fieldMatch();

    @Target({ ElementType.TYPE })
    @Retention(RetentionPolicy.RUNTIME)
    @interface List {
        FieldsValueMatch[] value();
    }
}