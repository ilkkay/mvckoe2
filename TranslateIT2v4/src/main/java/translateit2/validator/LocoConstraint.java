package translateit2.validator;

import java.lang.annotation.Retention;
import java.lang.annotation.Target;
import java.lang.annotation.ElementType;
import java.lang.annotation.RetentionPolicy;

import javax.validation.Constraint;
import javax.validation.Payload;

@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.TYPE)
@Constraint(validatedBy = LocoValidator.class)
public @interface LocoConstraint {
	    String message() default "Invalid localized object (loco)";
	    Class<?>[] groups() default { };
	    Class<? extends Payload>[] payload() default { };
}
