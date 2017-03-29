package translateit2.validator;

import static org.hamcrest.CoreMatchers.equalTo;
import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.*;
import static org.mockito.Mockito.when;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.util.Optional;
import java.util.Set;

import javax.validation.Configuration;
import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorFactory;
import javax.validation.ConstraintViolation;
import javax.validation.Validation;
import javax.validation.Validator;
import javax.validation.ValidatorFactory;
import javax.validation.metadata.PropertyDescriptor;

import org.junit.Before;
import org.mockito.runners.MockitoJUnitRunner;

import translateit2.persistence.dao.LocoRepository;
import translateit2.persistence.dto.LocoDto;
import translateit2.persistence.model.Loco;

@RunWith(MockitoJUnitRunner.class)
public class LocoValidatorTest implements ConstraintValidatorFactory {
    @Mock
    private LocoRepository mockRepo;
	
	private static Validator validator;
	
	@Before
	public void setUp() throws Exception {
		MockitoAnnotations.initMocks(this);

		Configuration<?> config = Validation.byDefaultProvider().configure();
	    config.constraintValidatorFactory(this);

	    ValidatorFactory factory = config.buildValidatorFactory();
	    validator = factory.getValidator();
	}
	
	
	@Test
	public void update_with_existing_projectname_return_ok() {
		long dtoLocoId = 1L;
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
				if (message.equalsIgnoreCase((Messages.getString("LocoValidator.project_exists_already"))))
					foundViolation=true;
		}
		
		assertThat(foundViolation, is(equalTo(false)));	}
	
	
	@Test
	public void create_with_existing_projectname_return_violation() {
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
				if (message.equalsIgnoreCase((Messages.getString("LocoValidator.project_exists_already"))))
					foundViolation=true;
		}
		
		assertThat(foundViolation, is(equalTo(true)));
	}
	
	@Test
	public void create_with_too_short_projectname() {
		// just checking values
		javax.validation.metadata.BeanDescriptor beanDesc = validator.getConstraintsForClass(LocoDto.class);
		beanDesc.getConstraintDescriptors().stream().forEach(p->System.out.println(p));	
		beanDesc.getConstrainedProperties().stream().forEach(p->System.out.println(p.getPropertyName() + ": " + p));
		System.out.println(mockRepo.getClass().toString());
		// finished checking
		
		LocoDto locoDto = new LocoDto();
		// update with too short project name
		when(mockRepo.findByProjectName("Proj")).thenReturn(Optional.empty());
		locoDto.setProjectName("Proj");
		locoDto.setName("Jukka");
		locoDto.setId(0L);
		Set<ConstraintViolation<LocoDto>> constraintViolations = validator.validate(locoDto);
		
		for(ConstraintViolation<LocoDto> constraintViolation : constraintViolations){
			String message = constraintViolation.getMessage();
			System.out.println(message);
		}		 
		assertEquals(1,constraintViolations.size());
	}
	

	@Test
	public void update_with_empty_name(){		
		when(mockRepo.findByProjectName("Translate IT 2")).thenReturn(Optional.empty());
		when(mockRepo.findByName("")).thenReturn(null);
		
		LocoDto locoDto = new LocoDto();
		locoDto.setProjectName("Translate IT 2");
		locoDto.setName("");
		locoDto.setId(1L);

		 Set<ConstraintViolation<LocoDto>> constraintViolations
		 	= validator.validate(locoDto);
		 
		 for(ConstraintViolation<LocoDto> constraintViolation : constraintViolations){
			 String message = constraintViolation.getMessage();
			 System.out.println(message);
		 }
		 
		 assertEquals(1,constraintViolations.size());
	}
	

	 @Test
	 public void update_with_name_having_no_permission() {
		when(mockRepo.findByProjectName("Translate IT 3")).thenReturn(Optional.empty());
 
		LocoDto locoDto = new LocoDto();
		locoDto.setProjectName("Translate IT 3");
		locoDto.setName("Pekka");
		locoDto.setId(1L);
	  		 
		Set<ConstraintViolation<LocoDto>> constraintViolations
		 = validator.validate(locoDto);
		 
		for(ConstraintViolation<LocoDto> constraintViolation : constraintViolations){
			String message = constraintViolation.getMessage();
			System.out.println(message);
		}
		 
		assertEquals(1,constraintViolations.size());
	 }

	@SuppressWarnings("unchecked")
	@Override
	public <T extends ConstraintValidator<?, ?>> T getInstance(Class<T> key) {	    
	    try {
		    if (key == LocoValidator.class) 
		        return (T) new LocoValidator(mockRepo);		    
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
