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
import translateit2.persistence.dao.LocoRepository;
import translateit2.persistence.dto.LocoDto;
import translateit2.persistence.model.Loco;
import translateit2.util.Messages;

// JUnit 4 Rule to run individual tests with a different default locale
// https://gist.github.com/digulla/5884162
@RunWith(MockitoJUnitRunner.class)
public class LocoValidatorTest implements ConstraintValidatorFactory {
    @Mock
    private LocoRepository mockRepo;
	
	private static Validator validator;
	
	private Messages messages;
	
	@Before
	public void setUp() throws Exception {
		MockitoAnnotations.initMocks(this);

		Configuration<?> config = Validation.byDefaultProvider().configure();
	    config.constraintValidatorFactory(this);

	    ValidatorFactory factory = config.buildValidatorFactory();
	    validator = factory.getValidator();
	    
	    ReloadableResourceBundleMessageSource messageSource = 
	    		new ReloadableResourceBundleMessageSource();
	    messageSource.setBasename("classpath:messages");
	    messageSource.setDefaultEncoding("ISO-8859-1");	    
	    messageSource.setFallbackToSystemLocale(false);
	    
	    messages = new Messages(messageSource);
	    messages.init(Locale.ENGLISH);
	}
	
	@Test
	public void test_messages() throws NoSuchFieldException, SecurityException {
		String s = null;
		
		s = messages.get("LocoValidator.name_exists");
		System.out.println(s);;
		
		s = messages.get("LocoValidator.no_create_permission");
		System.out.println(s);;
		
		s = messages.get("LocoValidator.project_exists_already");
		System.out.println(s);;
		
		s = messages.get("LocoValidator.entity_missing");
		System.out.println(s);;
		
		s = messages.get("LocoValidator.name_exists");
		System.out.println(s);;
		
		s = messages.get("LocoValidator.test_name");
		System.out.println(s);;
		
		String[] args = {"Translate IT 2", "5","35"};
		s=messages.get("LocoDto.projectName.size",args);
		System.out.println(s);;
		
		s = messages.get("TransuDto.source_not_null");
		System.out.println(s);;
		s = messages.get("TransuDto.target_not_empty");
		System.out.println(s);
		String[] args2 = {"5"};
		s = messages.get("TransuDto.segment_size",args2);
		System.out.println(s);		

		s = messages.get("javax.validation.constraints.NotNull.message");
		System.out.println(s);
	}
	

	@Test
	public void dontFailToUpdateProjectName_IfExistingEntity() {		
		// GIVEN: an existing loco l
		// ASSUMED: conversion from Loco to LocoDto
		long receivedLocoId = 1l;
		Loco l = new Loco();
		l.setProjectName("Translate IT 2");
		l.setName("Ilkka");
		l.setId(receivedLocoId);
		
		// WHEN: get it with an existing project name
		when(mockRepo.findByProjectName("Translate IT 2")).thenReturn(Optional.of(l));
		
		// validate an existing entity 
		long dtoLocoId = 1L;
		LocoDto locoDto = new LocoDto();
		locoDto.setProjectName("Translate IT 2");
		locoDto.setName("Ilkka");
		locoDto.setId(dtoLocoId);
		Set<ConstraintViolation<LocoDto>> constraintViolations 
			= validator.validate(locoDto);
		
		// THEN: assert that violation is NOT found
		boolean foundViolation=false;
		for(ConstraintViolation<LocoDto> constraintViolation : constraintViolations){
			String message = constraintViolation.getMessage();
			System.out.println(constraintViolation.getPropertyPath().toString() + 
								": " + message);
			if ("projectName".equals(constraintViolation.getPropertyPath().toString()))
				if (message.equalsIgnoreCase((messages.get("LocoValidator.project_exists_already"))))
					foundViolation=true;
		}
		
		assertThat(foundViolation, is(equalTo(false)));	
	}
	

	@Test
	public void failToCreateProjectName_IfExistingEntity() {
		long dtoLocoId = 0L;
		long receivedLocoId = 1l;
		
		// then (Loco => LocoDto) is assumed
		Loco l = new Loco();
		l.setProjectName("Translate IT 2");
		l.setName("Ilkka");
		l.setId(receivedLocoId);
		
		// get with an existing project name
		when(mockRepo.findByProjectName("Translate IT 2")).thenReturn(Optional.of(l));
		
		LocoDto locoDto = new LocoDto();
		locoDto.setProjectName("Translate IT 2");
		locoDto.setId(dtoLocoId);

		Set<ConstraintViolation<LocoDto>> constraintViolations = null;
		constraintViolations = validator.validate(locoDto);
		
		boolean foundViolation=false;
		for(ConstraintViolation<LocoDto> constraintViolation : constraintViolations){
			String message = constraintViolation.getMessage();
			System.out.println(constraintViolation.getPropertyPath().toString() + 
								": " + message);
			if ("projectName".equals(constraintViolation.getPropertyPath().toString()))
				if (message.equalsIgnoreCase((messages.get("LocoValidator.project_exists_already"))))
					foundViolation=true;
		}
		
		assertThat(foundViolation, is(equalTo(true)));
	}
	
	@Test
	public void failToCreateEntity_ifProjectNameShort() {
		// just checking values
		javax.validation.metadata.BeanDescriptor beanDesc = validator.getConstraintsForClass(LocoDto.class);
		beanDesc.getConstraintDescriptors().stream().forEach(p->System.out.println(p));	
		beanDesc.getConstrainedProperties().stream().forEach(p->System.out.println(p.getPropertyName() + ": " + p));
		System.out.println(mockRepo.getClass().toString());
		// finished checking
		
		long dtoLocoId = 0L;
		LocoDto locoDto = new LocoDto();
		// update with too short project name
		when(mockRepo.findByProjectName("Proj")).thenReturn(Optional.empty());
		locoDto.setProjectName("Proj");
		locoDto.setName("Jukka");
		locoDto.setId(dtoLocoId);
		Set<ConstraintViolation<LocoDto>> constraintViolations = validator.validate(locoDto);
		
		boolean foundViolation=false;
		for(ConstraintViolation<LocoDto> constraintViolation : constraintViolations){
			String message = constraintViolation.getMessage();
			if ("projectName".equals(constraintViolation.getPropertyPath().toString()))
				if (message.contains("characters long"))
					foundViolation=true;
		}		 
		assertThat(foundViolation, is(equalTo(true)));
	}
	

	@Test
	public void failToUpdateEntity_ifNameMissing(){		
		when(mockRepo.findByProjectName("Translate IT 2")).thenReturn(Optional.empty());
		when(mockRepo.findByName("")).thenReturn(null);
		
		LocoDto locoDto = new LocoDto();
		locoDto.setProjectName("Translate IT 2");
		locoDto.setName("");
		locoDto.setId(1L);

		Set<ConstraintViolation<LocoDto>> constraintViolations
			= validator.validate(locoDto);
		 
		boolean foundViolation=false;
		for(ConstraintViolation<LocoDto> constraintViolation : constraintViolations){
			String message = constraintViolation.getMessage();
			if ("name".equals(constraintViolation.getPropertyPath().toString()))
				if (message.contains("may not be empty"))
					foundViolation=true;
		}		 
		assertThat(foundViolation, is(equalTo(true)));

	}
	
	 @Test
	 public void failToCreateEntity_ifRequiredPermissionMissing() {
		when(mockRepo.findByProjectName("Translate IT 2")).thenReturn(Optional.empty());
		when(mockRepo.findByName("Pekka")).thenReturn(null);
			
		LocoDto locoDto = new LocoDto();
		locoDto.setProjectName("Translate IT 2");
		locoDto.setName("Pekka");
		locoDto.setId(1L);

		Set<ConstraintViolation<LocoDto>> constraintViolations
			= validator.validate(locoDto);
		
		boolean foundViolation=false;
		for(ConstraintViolation<LocoDto> constraintViolation : constraintViolations){
			String message = constraintViolation.getMessage();
			if ("name".equals(constraintViolation.getPropertyPath().toString()))
				if (message.equalsIgnoreCase(messages.get("LocoValidator.no_create_permission")))
					foundViolation=true;
		}
		 
		assertThat(foundViolation, is(equalTo(true)));
	 }

	 
	@SuppressWarnings("unchecked")
	@Override
	public <T extends ConstraintValidator<?, ?>> T getInstance(Class<T> key) {	    
	    try {
		    if (key == LocoValidator.class) 
		        return (T) new LocoValidator(mockRepo,messages);		    
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
