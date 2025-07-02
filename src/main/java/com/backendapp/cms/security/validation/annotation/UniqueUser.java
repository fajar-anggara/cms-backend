package com.backendapp.cms.security.validation.annotation;

import com.backendapp.cms.security.validation.annotationValidator.UniqueUserValidator;
import jakarta.validation.Constraint;
import jakarta.validation.Payload;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Target(ElementType.FIELD)
@Retention(RetentionPolicy.RUNTIME)
@Constraint(validatedBy = UniqueUserValidator.class)
public @interface UniqueUser {
    String message() default "User sudah terdaftar atau pernah terdaftar";
    Class<?>[] groups() default {};
    Class<? extends Payload>[] payload() default {};
}
