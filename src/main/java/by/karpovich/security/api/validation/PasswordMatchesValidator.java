package by.karpovich.security.api.validation;

import by.karpovich.security.api.dto.UserRegistryDto;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;

public class PasswordMatchesValidator implements ConstraintValidator<PasswordMatches, Object> {

    @Override
    public void initialize(PasswordMatches constraintAnnotation) {
    }

    @Override
    public boolean isValid(Object obj, ConstraintValidatorContext context) {
        UserRegistryDto user = (UserRegistryDto) obj;
        return user.getPassword().equals(user.getMatchingPassword());
    }
}
