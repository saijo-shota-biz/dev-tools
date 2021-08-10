package com.systemya_saijo.config.validation;

import com.systemya_saijo.core.exception.ValidationException;
import lombok.extern.slf4j.Slf4j;

import javax.validation.Validation;

@Slf4j
public class Validator {
  public static <T> T validate(T target) {
    var factory = Validation.buildDefaultValidatorFactory();
    var validator = factory.getValidator();
    var constraintViolations = validator.validate(target);
    var errorCount = constraintViolations.size();
    if (errorCount > 0) {
      var validationException = new ValidationException();
      constraintViolations.stream()
          .map(
              violation ->
                  new ValidationException.ValidateError(
                      violation.getPropertyPath().toString(),
                    violation.getInvalidValue() + "",
                      violation.getMessage()))
          .forEach(validationException::addValidateError);
      throw validationException;
    }
    return target;
  }
}
