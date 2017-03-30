package translateit2;

import static org.hamcrest.CoreMatchers.equalTo;
import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.*;

import java.util.Set;

import javax.validation.ConstraintViolation;
import javax.validation.ConstraintViolationException;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.context.web.WebAppConfiguration;

//import javax.ejb.EJBException;

import translateit2.persistence.dto.LocoDto;
import translateit2.persistence.dto.TransuDto;
import translateit2.service.Loco2ServiceImpl;
import translateit2.validator.Messages;


// http://www.journaldev.com/2668/spring-validation-example-mvc-validator
// https://www.petrikainulainen.net/programming/spring-framework/spring-from-the-trenches-adding-validation-to-a-rest-api/


@RunWith(SpringRunner.class)
@SpringBootTest(classes = TranslateIt2v4Application.class)
@WebAppConfiguration

public class LocoExceptionIntegrationTests {
    private Loco2ServiceImpl loco2Service;    
    @Autowired
    public void setLoco2Service(Loco2ServiceImpl loco2Service) {
        this.loco2Service = loco2Service;
    }
    

    
	@Test
	public void create_locodto_with_empty_projectname() throws Exception{
		// TODO: insert assertj.core.api and use java 8 style i.e 
		// assertThatExceptionOfType(ConstraintViolationException.class)
		// .isThrownBy(() -> loco2Service.createLocoDto(locoDto)
		// .matches(e -> e.getConstraintViolations().size() == 1)
		// .matches(e -> e.getConstraintViolations().stream()
		// .allMatch(v -> v.getMessage().contains("characters long"))));
		
		LocoDto locoDto = new LocoDto();
		locoDto.setProjectName("");
		locoDto.setName("Ilkka");	
		boolean foundViolation=false;
		try {
			locoDto=loco2Service.createLocoDto(locoDto);		
		} catch(ConstraintViolationException  e) {        	
        	Set<ConstraintViolation<?>> constraintViolations = e.getConstraintViolations();    		
    		for(ConstraintViolation<?> constraintViolation : constraintViolations){
    			String message = constraintViolation.getMessage();
    			if ("projectName".equals(constraintViolation.getPropertyPath().toString()))
    				if (message.contains("characters long"))
    					foundViolation=true;
    		}		 
		}
		assertThat(foundViolation, is(equalTo(true)));			
	}
	
	@Test
	public void create_locodto_with_Pekka_name() throws Exception{
        LocoDto locoDto = new LocoDto();
		locoDto.setProjectName("");
		locoDto.setName("Pekka");
		boolean foundViolation=false;
		try{
			locoDto=loco2Service.createLocoDto(locoDto);			
		} catch(ConstraintViolationException  e){
        	Set<ConstraintViolation<?>> constraintViolations = e.getConstraintViolations();    		
    		for(ConstraintViolation<?> constraintViolation : constraintViolations){
    			String message = constraintViolation.getMessage();
    			if ("name".equals(constraintViolation.getPropertyPath().toString()))
    				if (message.equalsIgnoreCase((Messages.getString("LocoValidator.no_create_permission"))))
    					foundViolation=true;
    		}
        }
		
		assertThat(foundViolation, is(equalTo(true)));	
	}
	
	@Test
	public void create_locodto_with_existing_projectname() throws Exception{
		boolean foundViolation=false;
		try{
	        LocoDto locoDto = new LocoDto();
			locoDto.setProjectName("Translate IT 2");
			locoDto.setName(Messages.getString("LocoValidator.test_name"));	
			locoDto=loco2Service.createLocoDto(locoDto);			
		}
        catch(ConstraintViolationException  e){
        	Set<ConstraintViolation<?>> constraintViolations = e.getConstraintViolations();    		
    		for(ConstraintViolation<?> constraintViolation : constraintViolations){
    			String message = constraintViolation.getMessage();
    			if ("projectName".equals(constraintViolation.getPropertyPath().toString()))
    				if (message.equalsIgnoreCase((Messages.getString("LocoValidator.project_exists_already"))))
    					foundViolation=true;
    		}
        }
		
		assertThat(foundViolation, is(equalTo(true)));
	}

	@Test
	public void create_locodto_with_empty_and_null_segment() throws Exception{
		try{
			LocoDto locoDto = loco2Service.getLocoDtoByProjectName("Translate IT 2");
	    	TransuDto transuDto = new TransuDto();

	    	transuDto.setSourceSegm("");
	    	transuDto.setTargetSegm(null);
	    	transuDto.setRowId(4);
	    	locoDto = loco2Service.createTransuDto(transuDto,locoDto.getId());			
		}
        catch(ConstraintViolationException  e){
        	e.getConstraintViolations().forEach(m->System.out.println(m.getMessage()));
        }
		
		int count = loco2Service.listAllLocoDtos().size();
		assertThat(count, is(2));
	}
	
	/*
	@Test(expected=DataIntegrityViolationException.class)
	public void create_locodto_with_existing_projectname_return_exception(){		
        LocoDto locoDto = new LocoDto();
		locoDto.setProjectName("Translate IT 2");
		locoDto.setName("Pekka");	
		locoDto=loco2Service.createLocoDto(locoDto);
		
		int count = loco2Service.listAllLocoDtos().size();
		assertThat(count, is(2));
	}
	*/
}
