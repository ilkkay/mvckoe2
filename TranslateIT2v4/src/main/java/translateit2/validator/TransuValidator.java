package translateit2.validator;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;

import translateit2.persistence.dto.TransuDto;
import translateit2.util.Messages;


public class TransuValidator implements ConstraintValidator<TransuConstraint, TransuDto> {
	@Autowired
	Messages messages;

	@Value("${SegmentMinSize}")
	private Integer SegmentMinSize=5;	// for testing
	
	@Override
	public void initialize(TransuConstraint arg0) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public boolean isValid(TransuDto value, ConstraintValidatorContext context) {
		// TODO Auto-generated method stub
		boolean isValid = true;

		if ((value !=null) && (value.getTargetSegm() != null) &&
				(value.getTargetSegm().length()<SegmentMinSize)){
			isValid = false;
			String[] args = {SegmentMinSize.toString()};
			context.buildConstraintViolationWithTemplate(
					messages.get("TransuDto.segment_size",args))
				.addPropertyNode("targetSegm") 
				.addConstraintViolation(); 
			}
		
		if ((value !=null) && (value.getSourceSegm() != null) &&
				(value.getSourceSegm().length()<SegmentMinSize)){
			isValid = false;
			context.disableDefaultConstraintViolation();
			String[] args = {SegmentMinSize.toString()};
			context.buildConstraintViolationWithTemplate(
					messages.get("TransuDto.segment_size",args))
				.addPropertyNode("sourceSegm") 
				.addConstraintViolation(); 
			}
		
		return isValid;
	}

}
