package translateit2;

import static org.hamcrest.CoreMatchers.equalTo;
import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.*;

import java.io.IOException;
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
import translateit2.service.Loco2Service;
import translateit2.service.Loco2ServiceImpl;
import translateit2.validator.LocoValidator;
import translateit2.util.Messages;

import static org.mockito.Mockito.*;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.assertj.core.api.Assertions.*;
import static org.hamcrest.Matchers.*;
import static org.junit.Assert.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.junit.Assert.fail;


// http://www.journaldev.com/2668/spring-validation-example-mvc-validator
// https://www.petrikainulainen.net/programming/spring-framework/spring-from-the-trenches-adding-validation-to-a-rest-api/


@RunWith(SpringRunner.class)
@SpringBootTest(classes = TranslateIt2v4Application.class)
@WebAppConfiguration
public class LocoExceptionIntegrationTests {
    private Loco2Service loco2Service;    
    @Autowired
    public void setLoco2Service(Loco2Service loco2Service) {
        this.loco2Service = loco2Service;
    }
    
    @Autowired
    Messages messages;
    
    @Test
    public void create_locodto_with_too_short_projectname() throws Exception {
    	LocoDto locoDto = new LocoDto();
    	locoDto.setProjectName("prj");
    	locoDto.setName("Ilkka");	
    	try {
    		locoDto=loco2Service.createLocoDto(locoDto);
    		fail("No Constraint Violation Exception thrown"); 
    	} catch(ConstraintViolationException  e) {  		
    		assertThat(e.getConstraintViolations().stream()
    			.filter(v->v.getMessage().contains("characters long")).count()
    				, is(equalTo(1L)));
    		
    		assertThat(e.getConstraintViolations().stream()
        			.filter(v->v.getMessage().contains("Name already exists")).count()
        				, is(equalTo(1L)));   		
    	}
    }
        
	@Test
	public void create_locodto_with_empty_projectname() throws Exception {
		LocoDto locoDto = new LocoDto();
		locoDto.setProjectName("prj");
		locoDto.setName("Ilkka");	
		try {
			locoDto=loco2Service.createLocoDto(locoDto);	
			fail("No Constraint Violation Exception thrown"); 
		} catch(ConstraintViolationException  e) {
    		assertThat(e.getConstraintViolations().stream()
        			.filter(v->v.getMessage().contains("characters long")).count()
        				, is(equalTo(1L)));  	 
		}			
	}
	
	@Test
	public void create_locodto_with_Pekka_name() throws Exception{
        LocoDto locoDto = new LocoDto();
		locoDto.setProjectName("");
		locoDto.setName("Pekka");
		
		try{
			locoDto=loco2Service.createLocoDto(locoDto);	
			fail("No Constraint Violation Exception thrown");
		} catch(ConstraintViolationException  e){
    		assertThat(e.getConstraintViolations().stream()
        			.filter(v->v.getMessage().contains(
        					messages.get("LocoValidator.no_create_permission")))
        						.count(), is(equalTo(1L))); 
        }
	}
	
	@Test
	public void create_locodto_with_existing_projectname() throws Exception{
        LocoDto locoDto = new LocoDto();
		locoDto.setProjectName("Translate IT 2");
		locoDto.setName(messages.get("LocoValidator.test_name"));	

		try{
			locoDto=loco2Service.createLocoDto(locoDto);
			fail("No Constraint Violation Exception thrown");
		}
        catch(ConstraintViolationException  e){
    		assertThat(e.getConstraintViolations().stream()
        			.filter(v->v.getMessage().contains(
        					messages.get("LocoValidator.project_exists_already")))
        						.count(), is(equalTo(1L))); 
        }
	}

	@Test
	public void create_locodto_with_empty_and_null_segment() throws Exception{
    	TransuDto transuDto = new TransuDto();
    	transuDto.setSourceSegm("");
    	transuDto.setTargetSegm(null);
    	transuDto.setRowId(4);

    	try{
			LocoDto locoDto = loco2Service.getLocoDtoByProjectName("Translate IT 2");
	    	locoDto = loco2Service.createTransuDto(transuDto,locoDto.getId());		    	
	    	fail("No Constraint Violation Exception thrown");
		}
        catch(ConstraintViolationException  e){
    		assertThat(e.getConstraintViolations().stream()
        			.filter(v->v.getMessage().contains("segment cannot be empty")).count()
        				, is(equalTo(1L)));  
    		
    		assertThat(e.getConstraintViolations().stream()
        			.filter(v->v.getMessage().contains("Must contain atleast")).count()
        				, is(equalTo(1L)));  
        }
	}
	
}
