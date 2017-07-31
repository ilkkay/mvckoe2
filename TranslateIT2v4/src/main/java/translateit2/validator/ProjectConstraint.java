package translateit2.validator;

import java.lang.annotation.Retention;
import java.lang.annotation.Target;
import java.lang.annotation.ElementType;
import java.lang.annotation.RetentionPolicy;

import javax.validation.Constraint;
import javax.validation.Payload;

@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.TYPE)
@Constraint(validatedBy = ProjectValidator.class)
public @interface ProjectConstraint {
    String message() default "Invalid project entity";

    Class<?>[] groups() default {};

    Class<? extends Payload>[] payload() default {};
}
