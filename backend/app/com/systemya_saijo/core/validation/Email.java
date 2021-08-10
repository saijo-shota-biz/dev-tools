package com.systemya_saijo.core.validation;

import org.apache.commons.validator.routines.EmailValidator;
import play.data.validation.Constraints;

import javax.validation.Constraint;
import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;
import javax.validation.Payload;
import java.lang.annotation.*;

import static java.lang.annotation.ElementType.*;
import static java.lang.annotation.ElementType.TYPE_USE;
import static java.lang.annotation.RetentionPolicy.RUNTIME;

@Documented
@Constraint(validatedBy = {Email.EmailValidator.class})
@Target({METHOD, FIELD, ANNOTATION_TYPE, CONSTRUCTOR, PARAMETER, TYPE_USE})
@Retention(RUNTIME)
public @interface Email {
  String message() default "{javax.validation.constraints.Email.message}";

  Class<?>[] groups() default {};

  Class<? extends Payload>[] payload() default {};

  @Target({ElementType.FIELD})
  @Retention(RetentionPolicy.RUNTIME)
  @interface List {
    Email[] value();
  }

  class EmailValidator implements ConstraintValidator<Email, String> {

    @Override
    public boolean isValid(String value, ConstraintValidatorContext context) {
      if (value == null || value.isEmpty()) {
        return true;
      }
      return org.apache.commons.validator.routines.EmailValidator.getInstance().isValid(value);
    }
  }
}
