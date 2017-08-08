package translateit2.validator;

import java.util.Optional;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.context.properties.ConfigurationProperties;

import translateit2.persistence.dao.ProjectRepository;
import translateit2.persistence.dto.ProjectDto;
import translateit2.persistence.model.Project;
import translateit2.util.Messages;

// http://dolszewski.com/spring/custom-validation-annotation-in-spring/

// the following is IMPORTANT contains node issues etc. 
// https://access.redhat.com/webassets/avalon/d/red-hat-jboss-enterprise-application-platform/7.0.0/javadocs/org/hibernate/validator/internal/engine/constraintvalidation/ConstraintValidatorContextImpl.html
@ConfigurationProperties(prefix = "translateit2.validator")
public class ProjectValidator implements ConstraintValidator<ProjectConstraint, ProjectDto> {

    private Integer projectNameMaxSize = 35; // for testing purposes

    // TODO: autowired validation => settings object
    private Integer projectNameMinSize = 5; // for testing purposes

    @Autowired
    private ProjectRepository projectRepo;

    @Autowired
    Messages messages;

    public ProjectValidator(ProjectRepository projectRepo, Messages messages) {
        this.projectRepo = projectRepo;
        this.messages = messages;
    }

    public Integer getProjectNameMaxSize() {
        return projectNameMaxSize;
    }

    public Integer getProjectNameMinSize() {
        return projectNameMinSize;
    }

    @Override
    public void initialize(final ProjectConstraint constraintAnnotation) {

    }

    @Override
    public boolean isValid(final ProjectDto value, final ConstraintValidatorContext context) {

        if (value == null)
            return true;

        boolean isValid = true;

        // check the length of the project name
        if ((value.getName() != null) && ((value.getName().length() < projectNameMinSize)
                || (value.getName().length() > projectNameMaxSize))) {
            isValid = false;
            String[] args = { value.getName(), projectNameMinSize.toString(), projectNameMaxSize.toString() };
            context.disableDefaultConstraintViolation();
            // TODO: what if messages.get(..) does not find any string ....
            String s = messages.get("ProjectDto.projectName.size", args);
            context.buildConstraintViolationWithTemplate(s).addPropertyNode("name").addConstraintViolation();
        }

        // project name is unique but if you do an update
        // then project name will be the same
        Optional<Project> project = projectRepo.findByName(value.getName());
        if (project == null)
            return false;
        else if (project.isPresent() && (project.get().getId() != value.getId())) {
            isValid = false;
            context.disableDefaultConstraintViolation();
            String s = messages.get("ProjectValidator.project_exists_already");
            context.buildConstraintViolationWithTemplate(s).addPropertyNode("name").addConstraintViolation(); // $NON-NLS-1$
        }

        return isValid;
    }

    public void setProjectNameMaxSize(Integer projectNameMaxSize) {
        this.projectNameMaxSize = projectNameMaxSize;
    }

    public void setProjectNameMinSize(Integer projectNameMinSize) {
        this.projectNameMinSize = projectNameMinSize;
    }
}