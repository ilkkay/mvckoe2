package translateit2.validator;

import java.util.Optional;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;

import org.springframework.beans.factory.annotation.Autowired;

import translateit2.persistence.dao.LocoRepository;
import translateit2.persistence.dto.LocoDto;
import translateit2.persistence.model.Loco;

// http://dolszewski.com/spring/custom-validation-annotation-in-spring/
// https://access.redhat.com/webassets/avalon/d/red-hat-jboss-enterprise-application-platform/7.0.0/javadocs/org/hibernate/validator/internal/engine/constraintvalidation/ConstraintValidatorContextImpl.html
public class LocoValidator implements ConstraintValidator<LocoConstraint, LocoDto> {

	@Autowired
    private LocoRepository locoRepo;
	
	public LocoValidator(LocoRepository locoRepo) {
		this.locoRepo = locoRepo;
	}
	
    @Override
    public void initialize(final LocoConstraint constraintAnnotation) {

    }

    @Override
    public boolean isValid(final LocoDto value,
                           final ConstraintValidatorContext context) {        
    	
    	boolean isValid = true;
   
    	// project name is unique but if you do an update then project name will be the same
    	Optional <Loco> l = locoRepo.findByProjectName(value.getProjectName());
    	if(l.isPresent() && (l.get().getId() != value.getId())) {
    		isValid = false;
    		context.disableDefaultConstraintViolation();
    		context.buildConstraintViolationWithTemplate(Messages.getString("LocoValidator.project_exists_already")  )
    			.addPropertyNode("projectName") .addConstraintViolation(); //$NON-NLS-1$
    	}

    	// TODO: remove this is just for testing ...
    	// Pekka user has no permission
    	if(Messages.getString("LocoValidator.test_name").equals(value.getName())) { //$NON-NLS-1$
    		isValid = false;
            context.disableDefaultConstraintViolation();
            context.buildConstraintViolationWithTemplate(Messages.getString("LocoValidator.no_create_permission")  ).addConstraintViolation(); //$NON-NLS-1$
        }    	    
    	    	
    	// TODO: remove this is just for testing ...
    	// name property is unique
    	Loco l2 = locoRepo.findByName(value.getName());    	
    	if(l2 != null) {
    		isValid = false;
            context.disableDefaultConstraintViolation();
            context.buildConstraintViolationWithTemplate(Messages.getString("Name already exists"))
            .addPropertyNode("name") .addConstraintViolation(); //$NON-NLS-1$
        }    	
    	
    	return isValid ;
    	
    }
}
