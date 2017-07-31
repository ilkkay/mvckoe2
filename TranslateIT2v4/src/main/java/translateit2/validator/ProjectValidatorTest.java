package translateit2.validator;

import static org.hamcrest.CoreMatchers.equalTo;

import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.*;
import static org.mockito.Mockito.when;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.util.Locale;
import java.util.Optional;
import java.util.Set;

import javax.validation.Configuration;
import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorFactory;
import javax.validation.ConstraintViolation;
import javax.validation.Validation;
import javax.validation.Validator;
import javax.validation.ValidatorFactory;
import org.junit.Before;
import org.mockito.runners.MockitoJUnitRunner;
import org.springframework.context.support.ReloadableResourceBundleMessageSource;

import translateit2.lngfileservice.LanguageFileFormat;
import translateit2.lngfileservice.LanguageFileType;
import translateit2.persistence.dao.ProjectRepository;
import translateit2.persistence.dto.ProjectDto;
import translateit2.persistence.model.Project;
import translateit2.util.Messages;

// JUnit 4 Rule to run individual tests with a different default locale
// https://gist.github.com/digulla/5884162
@RunWith(MockitoJUnitRunner.class)
public class ProjectValidatorTest implements ConstraintValidatorFactory {
    @Mock
    private ProjectRepository mockRepo;

    private static Validator validator;

    private Messages messages;

    @Before
    public void setUp() throws Exception {
        MockitoAnnotations.initMocks(this);

        Configuration<?> config = Validation.byDefaultProvider().configure();
        config.constraintValidatorFactory(this);

        ValidatorFactory factory = config.buildValidatorFactory();
        validator = factory.getValidator();

        ReloadableResourceBundleMessageSource messageSource = new ReloadableResourceBundleMessageSource();
        messageSource.setBasename("classpath:messages");
        messageSource.setDefaultEncoding("ISO-8859-1");
        messageSource.setFallbackToSystemLocale(false);

        messages = new Messages(messageSource);
        messages.init(Locale.ENGLISH);
    }

    @Test
    public void test_messages() throws NoSuchFieldException, SecurityException {
        String s = null;

        s = messages.get("ProjectValidator.name_exists");
        System.out.println(s);
        ;

        s = messages.get("ProjectValidator.no_create_permission");
        System.out.println(s);
        ;

        s = messages.get("ProjectValidator.project_exists_already");
        System.out.println(s);
        ;

        s = messages.get("ProjectValidator.entity_missing");
        System.out.println(s);
        ;

        s = messages.get("ProjectValidator.name_exists");
        System.out.println(s);
        ;

        s = messages.get("ProjectValidator.test_name");
        System.out.println(s);
        ;

        String[] args = { "Translate IT 2", "5", "35" };
        s = messages.get("ProjectDto.projectName.size", args);
        System.out.println(s);
        ;

        s = messages.get("Source.segment_not_null");
        System.out.println(s);
        ;
        s = messages.get("Target.segment_not_empty");
        System.out.println(s);
        String[] args2 = { "5", "666" };
        s = messages.get("Unit.segment_size", args2);
        System.out.println(s);

        s = messages.get("javax.validation.constraints.NotNull.message");
        System.out.println(s);

        s = messages.get("org.hibernate.validator.constraints.NotBlank.message");
        System.out.println(s);

        s = messages.get("org.hibernate.validator.constraints.NotEmpty.message");
        System.out.println(s);

    }

    @Test
    public void dontFailToUpdateProjectName_IfExistingEntity() {
        // GIVEN: an existing project l
        // ASSUMED: conversion from project to ProjectDto
        long receivedProjectId = 1l;
        Project prj = new Project();
        prj.setName("Translate IT 2");
        prj.setId(receivedProjectId);

        // WHEN: get it with an existing project name
        when(mockRepo.findByName("Translate IT 2")).thenReturn(Optional.of(prj));

        // validate an existing entity
        long dtoProjectId = 1L;
        ProjectDto projectDto = new ProjectDto();
        projectDto.setName("Translate IT 2");
        projectDto.setId(dtoProjectId);
        projectDto.setFormat(LanguageFileFormat.PROPERTIES);
        projectDto.setType(LanguageFileType.UTF_8);
        projectDto.setSourceLocale(new Locale("en_EN"));
        Set<ConstraintViolation<ProjectDto>> constraintViolations = validator.validate(projectDto);

        // THEN: assert that violation is NOT found
        boolean foundViolation = false;
        for (ConstraintViolation<ProjectDto> constraintViolation : constraintViolations) {
            String message = constraintViolation.getMessage();
            System.out.println(constraintViolation.getPropertyPath().toString() + ": " + message);
            if ("projectName".equals(constraintViolation.getPropertyPath().toString())) {
                if (message.equalsIgnoreCase((messages.get("ProjectValidator.project_exists_already"))))
                    foundViolation = true;
            } else {
                fail("Did not find correct PropertyPath");
            }
        }

        assertThat(foundViolation, is(equalTo(false)));
    }

    @Test
    public void failToCreateProjectName_IfExistingEntity() {
        // GIVEN: an existing project id 0
        // ASSUMED: conversion from project to ProjectDto
        long dtoProjectId = 0L;
        long receivedProjectId = 1l;
        Project prj = new Project();
        prj.setName("Translate IT 2");
        prj.setId(receivedProjectId);

        // WHEN: get it with an existing project name
        when(mockRepo.findByName("Translate IT 2")).thenReturn(Optional.of(prj));

        // validate an existing entity
        ProjectDto projectDto = new ProjectDto();
        projectDto.setName("Translate IT 2");
        projectDto.setId(dtoProjectId);
        projectDto.setFormat(LanguageFileFormat.PROPERTIES);
        projectDto.setType(LanguageFileType.UTF_8);
        projectDto.setSourceLocale(new Locale("en_EN"));
        Set<ConstraintViolation<ProjectDto>> constraintViolations = validator.validate(projectDto);

        boolean foundViolation = false;
        for (ConstraintViolation<ProjectDto> constraintViolation : constraintViolations) {
            String message = constraintViolation.getMessage();
            System.out.println(constraintViolation.getPropertyPath().toString() + ": " + message);
            if ("name".equals(constraintViolation.getPropertyPath().toString())) {
                if (message.contains(messages.getPart("ProjectValidator.project_exists_already")))
                    foundViolation = true;
            } else {
                fail("Did not find correct PropertyPath");
            }
        }

        assertThat(foundViolation, is(equalTo(true)));
    }

    @Test
    public void failToCreateEntity_ifProjectNameShort() {
        // just checking values
        javax.validation.metadata.BeanDescriptor beanDesc = validator.getConstraintsForClass(ProjectDto.class);
        beanDesc.getConstraintDescriptors().stream().forEach(p -> System.out.println(p));
        beanDesc.getConstrainedProperties().stream().forEach(p -> System.out.println(p.getPropertyName() + ": " + p));
        System.out.println(mockRepo.getClass().toString());
        // finished checking

        long dtoProjectId = 0L;
        ProjectDto projectDto = new ProjectDto();
        // update with too short project name
        when(mockRepo.findByName("Proj")).thenReturn(Optional.empty());
        projectDto.setName("Proj");
        projectDto.setId(dtoProjectId);
        projectDto.setFormat(LanguageFileFormat.PROPERTIES);
        projectDto.setType(LanguageFileType.UTF_8);
        projectDto.setSourceLocale(new Locale("en_EN"));
        Set<ConstraintViolation<ProjectDto>> constraintViolations = validator.validate(projectDto);

        boolean foundViolation = false;
        for (ConstraintViolation<ProjectDto> constraintViolation : constraintViolations) {
            String message = constraintViolation.getMessage();
            if ("name".equals(constraintViolation.getPropertyPath().toString())) {
                if (message.contains(messages.getPart("ProjectDto.projectName.size")))
                    foundViolation = true;
            } else {
                fail("Did not find correct PropertyPath");
            }
        }
        assertThat(foundViolation, is(equalTo(true)));
    }

    @Test
    public void failToUpdateEntity_ifNameEmpty() {
        when(mockRepo.findByName("")).thenReturn(null);

        ProjectDto projectDto = new ProjectDto();
        projectDto.setName("");
        projectDto.setId(1L);
        projectDto.setFormat(LanguageFileFormat.PROPERTIES);
        projectDto.setType(LanguageFileType.UTF_8);
        projectDto.setSourceLocale(new Locale("en_EN"));

        Set<ConstraintViolation<ProjectDto>> constraintViolations = validator.validate(projectDto);

        boolean foundViolation = false;
        for (ConstraintViolation<ProjectDto> constraintViolation : constraintViolations) {
            String message = constraintViolation.getMessage();
            if ("name".equals(constraintViolation.getPropertyPath().toString())) {
                if (message.contains(messages.getPart("ProjectDto.projectName.size")))
                    foundViolation = true;
            } else {
                fail("Did not find correct PropertyPath");
            }
        }
        assertThat(foundViolation, is(equalTo(true)));
    }

    @Test
    public void failToCreateEntity_ifLocaleEmpty() {
        // GIVEN: an existing project id 0
        // ASSUMED: conversion from project to ProjectDto
        long dtoProjectId = 0L;
        long receivedProjectId = 1l;
        Project prj = new Project();
        prj.setName("Translate IT 2");
        prj.setId(receivedProjectId);

        // WHEN: get it with an existing project name
        when(mockRepo.findByName("Translate IT 2")).thenReturn(Optional.of(prj));

        // validate an existing entity
        ProjectDto projectDto = new ProjectDto();
        projectDto.setName("Translate IT 2");
        projectDto.setId(dtoProjectId);
        Set<ConstraintViolation<ProjectDto>> constraintViolations = validator.validate(projectDto);

        boolean foundViolation = false;
        for (ConstraintViolation<ProjectDto> constraintViolation : constraintViolations) {
            String messageTemplate = constraintViolation.getMessageTemplate();
            String messageTemplateExpected = "{" + "javax.validation.constraints.NotNull.message" + "}";
            if (messageTemplateExpected.equals(messageTemplate))
                foundViolation = true;
        }
        assertThat(foundViolation, is(equalTo(true)));
    }

    @SuppressWarnings("unchecked")
    @Override
    public <T extends ConstraintValidator<?, ?>> T getInstance(Class<T> key) {
        try {
            if (key == ProjectValidator.class)
                return (T) new ProjectValidator(mockRepo, messages);
            else
                return key.newInstance();
        } catch (InstantiationException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
            throw new IllegalArgumentException("getInstance() gave InstantiationException");
        } catch (IllegalAccessException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
            throw new IllegalArgumentException("getInstance() gave IllegalAccessException");
        }
    }

    @Override
    public void releaseInstance(ConstraintValidator<?, ?> arg0) {
        // TODO Auto-generated method stub

    }

}