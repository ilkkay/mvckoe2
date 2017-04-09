package translateit2.validator;

import java.util.Optional;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.context.properties.ConfigurationProperties;

import translateit2.persistence.dao.LocoRepository;
import translateit2.persistence.dto.LocoDto;
import translateit2.persistence.model.Loco;
import translateit2.util.Messages;

// http://dolszewski.com/spring/custom-validation-annotation-in-spring/

// the following is IMPORTANT contains node issues etc. 
// https://access.redhat.com/webassets/avalon/d/red-hat-jboss-enterprise-application-platform/7.0.0/javadocs/org/hibernate/validator/internal/engine/constraintvalidation/ConstraintValidatorContextImpl.html
@ConfigurationProperties(prefix = "translateit2.")
public class LocoValidator implements ConstraintValidator<LocoConstraint, LocoDto> {

	@Autowired
    private LocoRepository locoRepo;
	
    @Autowired
    Messages messages;
    
    public LocoValidator(LocoRepository locoRepo, Messages messages) {
		this.locoRepo = locoRepo;
		this.messages = messages;
	}

    // autowired validation settings object    
	private Integer projectNameMinSize=5; // for testing purposes
	
	private Integer projectNameMaxSize=35; // for testing purposes
	
	public void setProjectNameMinSize(Integer projectNameMinSize) {
		this.projectNameMinSize = projectNameMinSize;
	}

	public void setProjectNameMaxSize(Integer projectNameMaxSize) {
		this.projectNameMaxSize = projectNameMaxSize;
	}

	@Override
    public void initialize(final LocoConstraint constraintAnnotation) {

    }

    @Override
    public boolean isValid(final LocoDto value,
                           final ConstraintValidatorContext context) {        
    	
    	boolean isValid = true;
   
		if ((value !=null) && (value.getProjectName() != null) &&
				((value.getProjectName().length()<projectNameMinSize)
					|| (value.getProjectName().length()>projectNameMaxSize))){
			isValid = false;
			String[] args = {value.getProjectName(), projectNameMinSize.toString(),
					projectNameMaxSize.toString()};
			context.buildConstraintViolationWithTemplate(
					messages.get("LocoDto.projectName.size",args))
						.addPropertyNode("projectName") 
						.addConstraintViolation(); 
			}
    			
    	// project name is unique but if you do an update then project name will be the same
    	Optional <Loco> l = locoRepo.findByProjectName(value.getProjectName());
    	if(l.isPresent() && (l.get().getId() != value.getId())) {
    		isValid = false;
    		context.disableDefaultConstraintViolation();
    		String s = messages.get("LocoValidator.project_exists_already");
            context.buildConstraintViolationWithTemplate(s)
            	.addPropertyNode("projectName").addConstraintViolation(); //$NON-NLS-1$
    	}

    	// TODO: remove this is just for testing ...
    	// Pekka user has no permission
    	if("Pekka".equals(value.getName())) { //$NON-NLS-1$
    		isValid = false;
    		String s = messages.get("LocoValidator.no_create_permission");
            context.disableDefaultConstraintViolation();            
            context.buildConstraintViolationWithTemplate(s)
        		.addPropertyNode("name").addConstraintViolation(); //$NON-NLS-1$
        }    	    
    	    	
    	// TODO: remove this is just for testing ...
    	// name property is unique
    	Loco l2 = locoRepo.findByName(value.getName());    	
    	if((l2 != null) && (l2.getId() != value.getId())) {
    		isValid = false;
            context.disableDefaultConstraintViolation();
            String s = messages.get("LocoValidator.name_exists");
            context.buildConstraintViolationWithTemplate(s)
            	.addPropertyNode("name").addConstraintViolation(); //$NON-NLS-1$
        }    	
    	
    	return isValid ;
    	
    }
}
