package translateit2.validator;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;

import translateit2.persistence.dto.TransuDto;
import translateit2.util.Messages;

// validator example
// https://www.mkyong.com/spring-mvc/spring-mvc-form-handling-example/
public class TransuValidator implements ConstraintValidator<TransuConstraint, TransuDto> {
	@Autowired
	Messages messages;

	@Value("${SegmentMinSize}")
	private Integer SegmentMinSize=5;	// for testing
	
	@Value("${SegmentMaxSize}")
	private Integer SegmentMaxSize=25;	// for testing
	
	@Override
	public void initialize(TransuConstraint arg0) {
		// TODO Auto-generated method stub		
	}

	
	@Override
	public boolean isValid(TransuDto value, ConstraintValidatorContext context) {
		// TODO Auto-generated method stub
		if (value == null) return true;
		
		boolean isValid = true;
		
		// source segment cannot be null or empty string
		if ((value.getSourceSegm() != null) &&
				((value.getSourceSegm().length()<SegmentMinSize) ||
					(value.getSourceSegm().length()>SegmentMaxSize))){
			isValid = false;
			context.disableDefaultConstraintViolation();
			String[] args = {SegmentMinSize.toString(), SegmentMaxSize.toString()};
			context.buildConstraintViolationWithTemplate(
					messages.get("TransuDto.segment_size",args))
				.addPropertyNode("sourceSegm") 
				.addConstraintViolation(); 
			}
		
		// TODO: target can be empty, but some upper limit should be set
		if ((value.getTargetSegm() != null) &&
				(value.getTargetSegm().length()>SegmentMaxSize)){
			isValid = false;
			String[] args = {SegmentMinSize.toString(), SegmentMaxSize.toString()};
			context.buildConstraintViolationWithTemplate(
					messages.get("TransuDto.segment_size",args))
				.addPropertyNode("targetSegm") 
				.addConstraintViolation(); 
			}		
		
		return isValid;
	}

}
