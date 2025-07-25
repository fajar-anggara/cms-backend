package com.backendapp.cms.security.validation.annotation;

import com.backendapp.cms.security.validation.annotationValidator.UniqueEmailValidator;
import jakarta.validation.Constraint;
import jakarta.validation.Payload;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Target(ElementType.FIELD)
@Retention(RetentionPolicy.RUNTIME)
@Constraint(validatedBy = UniqueEmailValidator.class)
public @interface UniqueEmail {
    String message() default "Email sudah terdaftar atau pernah terdaftar";
    Class<?>[] groups() default {};
    Class<? extends Payload>[] payload() default {};
}
