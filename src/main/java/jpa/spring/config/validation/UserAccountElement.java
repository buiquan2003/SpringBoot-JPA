package jpa.spring.config.validation;

import java.lang.annotation.Documented;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;
import jakarta.validation.Constraint;
import jakarta.validation.Payload;

//Đây là annotation của Bean Validation API, xác định rằng annotation này 
//sẽ sử dụng một lớp xác thực (UserAccountValidator) để thực hiện quá trình xác thực.
@Constraint(validatedBy = UserAccountValidator.class)
// Chỉ định rằng annotation này có thể được áp dụng cho các lớp (class) hoặc
// interface.
@Target({ ElementType.TYPE })
// Chỉ định rằng annotation này sẽ được giữ lại tại thời điểm runtime. Điều này
// có
// nghĩa là annotation sẽ có thể được truy cập thông qua phản chiếu (reflection)
// trong khi chương trình đang chạy.
@Retention(RetentionPolicy.RUNTIME)
@Documented
public @interface UserAccountElement {
    String field();

    String regex();

    String message() default "Field's format is invalid!";

    Class<?>[] groups() default {};

    Class<? extends Payload>[] payload() default {};

    @Target({ ElementType.TYPE })
    @Retention(RetentionPolicy.RUNTIME)
    @interface List {
        UserAccountElement[] value();
    }
}