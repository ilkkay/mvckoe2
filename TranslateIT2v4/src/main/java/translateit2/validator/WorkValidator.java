package translateit2.validator;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.context.properties.ConfigurationProperties;

import translateit2.persistence.dao.ProjectRepository;
import translateit2.persistence.dao.WorkRepository;
import translateit2.persistence.dto.WorkDto;
import translateit2.util.Messages;

// http://dolszewski.com/spring/custom-validation-annotation-in-spring/

// the following is IMPORTANT contains node issues etc. 
// https://access.redhat.com/webassets/avalon/d/red-hat-jboss-enterprise-application-platform/7.0.0/javadocs/org/hibernate/validator/internal/engine/constraintvalidation/ConstraintValidatorContextImpl.html
@ConfigurationProperties(prefix = "translateit2.")
public class WorkValidator implements ConstraintValidator<WorkConstraint, WorkDto> {

    @Autowired
    private WorkRepository workRepo;

    @Autowired
    Messages messages;

    public WorkValidator(ProjectRepository projectRepo, Messages messages) {
        this.workRepo = workRepo;
        this.messages = messages;
    }

    // autowired validation settings object
    // private Integer projectNameMinSize=5; // for testing purposes

    // private Integer projectNameMaxSize=35; // for testing purposes

    /*
     * public void setProjectNameMinSize(Integer projectNameMinSize) {
     * this.projectNameMinSize = projectNameMinSize; }
     * 
     * public void setProjectNameMaxSize(Integer projectNameMaxSize) {
     * this.projectNameMaxSize = projectNameMaxSize; }
     */

    @Override
    public void initialize(WorkConstraint constraintAnnotation) {
        // TODO Auto-generated method stub

    }

    @Override
    public boolean isValid(WorkDto value, ConstraintValidatorContext context) {
        
        if (value == null)
            return true;

        boolean isValid = true;

        // ....

        return isValid;
    }
}