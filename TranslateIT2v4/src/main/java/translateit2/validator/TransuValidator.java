package translateit2.validator;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.context.properties.ConfigurationProperties;

import translateit2.persistence.dto.TransuDto;
import translateit2.util.Messages;

// validator example
// https://www.mkyong.com/spring-mvc/spring-mvc-form-handling-example/
@ConfigurationProperties(prefix = "translateit2")
public class TransuValidator implements ConstraintValidator<TransuConstraint, TransuDto> {
	@Autowired
	Messages messages;

	//@Value("${translateit2.segmentMinSize}")
	private Integer segmentMinSize=5;	// for testing
	
	//@Value("${translateit2.segmentMaxSize}")
	private Integer segmentMaxSize=25;	// for testing
	
	public void setSegmentMinSize(Integer segmentMinSize) {
		this.segmentMinSize = segmentMinSize;
	}

	public void setSegmentMaxSize(Integer segmentMaxSize) {
		this.segmentMaxSize = segmentMaxSize;
	}


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
				((value.getSourceSegm().length()<segmentMinSize) ||
					(value.getSourceSegm().length()>segmentMaxSize))){
			isValid = false;
			context.disableDefaultConstraintViolation();
			String[] args = {segmentMinSize.toString(), segmentMaxSize.toString()};
			context.buildConstraintViolationWithTemplate(
					messages.get("TransuDto.segment_size",args))
				.addPropertyNode("sourceSegm") 
				.addConstraintViolation(); 
			}
		
		// TODO: target can be empty, but some upper limit should be set
		if ((value.getTargetSegm() != null) &&
				(value.getTargetSegm().length()>segmentMaxSize)){
			isValid = false;
			String[] args = {segmentMinSize.toString(), segmentMaxSize.toString()};
			context.buildConstraintViolationWithTemplate(
					messages.get("TransuDto.segment_size",args))
				.addPropertyNode("targetSegm") 
				.addConstraintViolation(); 
			}		
		
		return isValid;
	}

}
