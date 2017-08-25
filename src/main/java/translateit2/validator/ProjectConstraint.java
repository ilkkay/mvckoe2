package translateit2.validator;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

import javax.validation.Constraint;
import javax.validation.Payload;

@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.TYPE)
@Constraint(validatedBy = ProjectValidator.class)
public @interface ProjectConstraint {
    Class<?>[] groups() default {};

    String message() default "Invalid project entity";

    Class<? extends Payload>[] payload() default {};
}
