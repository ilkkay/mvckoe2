package translateit2;

import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.*;

import javax.validation.ConstraintViolationException;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.context.web.WebAppConfiguration;

//import javax.ejb.EJBException;

import translateit2.persistence.dto.LocoDto;
import translateit2.service.Loco2ServiceImpl;

// TODO: validate add valid annotation
// https://spring.io/guides/gs/validating-form-input/
// JSR-303 annotation + hibernate validator 
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
    

	@Test(expected=ConstraintViolationException.class)
	public void create_locodto_with_empty_projectname_return_exception(){		
        LocoDto locoDto = new LocoDto();
		locoDto.setProjectName("");
		locoDto.setName("Ilkka");	
		locoDto=loco2Service.createLocoDto(locoDto);		
		
		int count = loco2Service.listAllLocoDtos().size();
		assertThat(count, is(2));
	}
	
	@Test
	public void create_locodto_with_Pekka_name() throws Exception{	
		try{
	        LocoDto locoDto = new LocoDto();
			locoDto.setProjectName("");
			locoDto.setName("Pekka");	
			locoDto=loco2Service.createLocoDto(locoDto);			
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
