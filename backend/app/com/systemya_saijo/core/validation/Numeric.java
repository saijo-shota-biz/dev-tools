package com.systemya_saijo.core.validation;

import javax.validation.Constraint;
import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;
import javax.validation.Payload;
import java.lang.annotation.*;

import static java.lang.annotation.ElementType.*;
import static java.lang.annotation.RetentionPolicy.RUNTIME;

@Documented
@Constraint(validatedBy = {Numeric.NumericValidator.class})
@Target({METHOD, FIELD, ANNOTATION_TYPE, CONSTRUCTOR, PARAMETER, TYPE_USE})
@Retention(RUNTIME)
public @interface Numeric {
  String message() default "数値にしてください";

  Class<?>[] groups() default {};

  Class<? extends Payload>[] payload() default {};

  @Target({ElementType.FIELD})
  @Retention(RetentionPolicy.RUNTIME)
  @interface List {
    Numeric[] value();
  }

  class NumericValidator implements ConstraintValidator<Numeric, String> {

    @Override
    public boolean isValid(String value, ConstraintValidatorContext context) {
      if (value == null || value.isEmpty()) {
        return true;
      }
      try {
        Integer.valueOf(value);
        return true;
      } catch (Exception e) {
        return false;
      }
    }
  }
}
