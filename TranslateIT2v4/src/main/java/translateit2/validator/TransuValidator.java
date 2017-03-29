package translateit2.validator;

import java.lang.annotation.Annotation;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;

import translateit2.persistence.dto.LocoDto;
import translateit2.persistence.dto.TransuDto;

public class TransuValidator implements ConstraintValidator<TransuConstraint, TransuDto> {

	@Override
	public void initialize(TransuConstraint arg0) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public boolean isValid(TransuDto value, ConstraintValidatorContext context) {
		// TODO Auto-generated method stub
		boolean isValid = true;

		if ((value !=null) && (value.getTargetSegm() != null) &&
				(value.getTargetSegm().length()<2)){
			isValid = false;
			context.disableDefaultConstraintViolation();
			context.buildConstraintViolationWithTemplate(Messages.getString("Must contain atleast 2 characters"))
			.addPropertyNode("targetSegm") 
			.addConstraintViolation(); //$NON-NLS-1$
			}
		
		if ((value !=null) && (value.getSourceSegm() != null) &&
				(value.getSourceSegm().length()<2)){
			isValid = false;
			context.disableDefaultConstraintViolation();
			context.buildConstraintViolationWithTemplate(Messages.getString("Must contain atleast 2 characters"))
			.addPropertyNode("sourceSegm") 
			.addConstraintViolation(); //$NON-NLS-1$
			}
		
		return isValid;
	}

}
