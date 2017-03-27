package translateit2.validator;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;

import translateit2.persistence.dto.LocoDto;

// http://dolszewski.com/spring/custom-validation-annotation-in-spring/
public class LocoValidator implements ConstraintValidator<LocoConstraint, LocoDto> {

    @Override
    public void initialize(final LocoConstraint constraintAnnotation) {

    }

    @Override
    public boolean isValid(final LocoDto value,
                           final ConstraintValidatorContext context) {        
    	
    	boolean isValid = !("Pekka".equals(value.getName()));
        
    	if(!isValid) {
            context.disableDefaultConstraintViolation();
            context.buildConstraintViolationWithTemplate( "Pekka has not required for this operation"  ).addConstraintViolation();
        }
        
    	return isValid ;
    	
    }
}
