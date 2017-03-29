package translateit2.validator;

import java.lang.annotation.Retention;
import java.lang.annotation.Target;
import java.lang.annotation.ElementType;
import java.lang.annotation.RetentionPolicy;

import javax.validation.Constraint;
import javax.validation.Payload;

@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.TYPE)
@Constraint(validatedBy = TransuValidator.class)
public @interface TransuConstraint {
    String message() default "Invalid translation unit (transu)";
    Class<?>[] groups() default { };
    Class<? extends Payload>[] payload() default { };
}
