package translateit2.validator;

import java.lang.annotation.Retention;
import java.lang.annotation.Target;
import java.lang.annotation.ElementType;
import java.lang.annotation.RetentionPolicy;

import javax.validation.Constraint;
import javax.validation.Payload;

@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.TYPE)
@Constraint(validatedBy = UnitValidator.class)
public @interface UnitConstraint {
    String message() default "Invalid unit entity";

    Class<?>[] groups() default {};

    Class<? extends Payload>[] payload() default {};
}
