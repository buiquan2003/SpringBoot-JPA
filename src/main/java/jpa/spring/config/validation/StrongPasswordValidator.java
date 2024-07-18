package jpa.spring.config.validation;

import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;

public class StrongPasswordValidator implements ConstraintValidator<StrongPassword, String> {

    @Override
    public boolean isValid(String value, ConstraintValidatorContext context) {
        if (value == null) {
            return false; 
        }
        return value.matches("^(?=.*[a-z])(?=.*[A-Z])(?=.*[@#$%^&++!*()]).{8,}$");
    }

    
}
