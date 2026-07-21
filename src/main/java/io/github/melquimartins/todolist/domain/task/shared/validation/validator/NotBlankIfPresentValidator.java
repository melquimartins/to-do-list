package io.github.melquimartins.todolist.domain.task.shared.validation.validator;

import io.github.melquimartins.todolist.domain.task.shared.validation.annotation.NotBlankIfPresent;
import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;

public class NotBlankIfPresentValidator
      implements ConstraintValidator<NotBlankIfPresent, String>
{
  @Override
  public boolean isValid(String value, ConstraintValidatorContext context) {
    if (value == null) {
      return false;
    }

    return !value.trim().isEmpty();
  }
}
