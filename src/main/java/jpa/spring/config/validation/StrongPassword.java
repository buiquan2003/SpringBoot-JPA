package jpa.spring.config.validation;

import java.lang.annotation.Documented;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;
import jakarta.validation.Payload;
import jakarta.validation.Constraint;

@Constraint(validatedBy = StrongPasswordValidator.class)
@Target({ElementType.METHOD, ElementType.FIELD})
@Retention(RetentionPolicy.RUNTIME)
@Documented
public @interface StrongPassword {
  String message() default "Password must be 8 characters long and combination of uppercase letters, lowercase letters, numbers, special characters";
  Class<?>[] groups() default {};
  Class<? extends Payload> [] payload() default {};
}
