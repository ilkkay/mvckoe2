package translateit2;

import static org.hamcrest.CoreMatchers.equalTo;

import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.*;
import static org.junit.Assert.assertThat;

import java.io.IOException;
import java.util.Locale;
import javax.validation.ConstraintViolationException;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.context.junit4.SpringRunner;

import translateit2.persistence.dto.Loco2Dto;

//import javax.ejb.EJBException;

import translateit2.persistence.dto.LocoDto;
import translateit2.persistence.dto.PersonDto;
import translateit2.persistence.dto.ProjectDto;
import translateit2.persistence.dto.TransuDto;
import translateit2.persistence.dto.TransuDto2;
import translateit2.persistence.dto.TranveDto;
import translateit2.persistence.model.Transu;
import translateit2.persistence.model2.Person;
import translateit2.persistence.model2.Transu2;
import translateit2.persistence.model2.Tranve;
import translateit2.service.LocoService;
import translateit2.service.Transu2ServiceImpl;
import translateit2.service.TransuServiceImpl;
import translateit2.service.WorkService;
import translateit2.util.ISO8859Loader;
import translateit2.util.Messages;

import static org.assertj.core.api.Assertions.*;
import static org.junit.Assert.fail;
import java.util.UUID;

// http://www.journaldev.com/2668/spring-validation-example-mvc-validator
// https://www.petrikainulainen.net/programming/spring-framework/spring-from-the-trenches-adding-validation-to-a-rest-api/

@ConfigurationProperties(prefix = "test.translateit2")
@TestPropertySource("test.properties")
//@TestPropertySource(properties = {"ProjectNameMaxSize = 666","ProjectNameMinSize = 3"})
@RunWith(SpringRunner.class)
@SpringBootTest(classes = TranslateIt2v4Application.class)
public class WorkIntegrationTest {
	private WorkService workService;	
	@Autowired
    public void setWorkService(WorkService workService) {
        this.workService = workService;
    }
    
    private Transu2ServiceImpl transu2Service;    
	@Autowired
    public void setTransuService(Transu2ServiceImpl transu2Service) {
        this.transu2Service = transu2Service;
    }
	
    @Autowired
    Messages messages;
    
    @Before
    public void setup() {
    	Locale.setDefault(Locale.ENGLISH);	// for javax validation
    	messages.init(Locale.ENGLISH);   	// for custom validation 	
    }
    
	//@Test
	public void createProject() {
		/* from different service
		PersonDto personDto = new PersonDto();
		personDto.setFullName("James Bond");
		*/
		
		ProjectDto projectDto = new ProjectDto();
		projectDto.setProjectName("Translate IT 3");
		projectDto.setManager("Juha");	

		projectDto=workService.createProjectDto(projectDto); 
		
		ProjectDto prj = workService.getProjectDtoById(projectDto.getId());
		assertEquals("Translate IT 3", prj.getProjectName());
		
		ProjectDto prj2 = workService.getProjectDtoByProjectName("Translate IT 3");
		assertEquals("Translate IT 3", prj2.getProjectName());
	}
	
	//@Test
	public void createTranve_and_addTransus() {
		ProjectDto projectDto = new ProjectDto();
		projectDto.setProjectName("Translate IT 3");
		projectDto.setManager("Juha");	
		projectDto=workService.createProjectDto(projectDto); 
		
		ProjectDto prj2 = workService.getProjectDtoByProjectName("Translate IT 3");
		
        TranveDto tranveDto = new TranveDto();
        tranveDto.setProject(prj2.getId());;
        tranveDto.setLocale("en_EN");	
        tranveDto.setVersion("007");
        tranveDto=workService.createTranveDto(tranveDto);  
        
    	int countStart = 0;
    	
    	// create a new transu2
    	TransuDto2 transuDto2 = new TransuDto2();
    	transuDto2.setSegmentId("SourceSegm 1");
    	transuDto2.setSegment("TargetSegm 1");
    	transuDto2.setRowId(1);
    	tranveDto = workService.createTransuDto(transuDto2,tranveDto);
    	
    	transuDto2 = new TransuDto2();
    	transuDto2.setSegmentId("SourceSegm 2");
    	transuDto2.setSegment("TargetSegm 2");
    	transuDto2.setRowId(2);
    	tranveDto = workService.createTransuDto(transuDto2,tranveDto);
    	
    	countStart = workService.listAllTransuDtos(tranveDto).size();
    	assertThat(countStart, is(2));
	}
		
	@Test
	public void createProject_Tranve_Loco_and_addTransus(){
		ProjectDto projectDto = new ProjectDto();
		projectDto.setProjectName("Translate IT 3");
		projectDto.setManager("Juha");	
		projectDto=workService.createProjectDto(projectDto); 
		
		ProjectDto prj2 = workService.getProjectDtoByProjectName("Translate IT 3");
		
        TranveDto tranveDto = new TranveDto();
        tranveDto.setProject(prj2.getId());
        tranveDto.setFilename("dotcms");
        tranveDto.setLocale("en_EN");
        tranveDto.setFormat("properties");
        tranveDto.setType("ISO8859-1");
        tranveDto.setVersion("0.07");
        tranveDto.setFid(UUID.randomUUID().toString());
        try {
        	tranveDto=workService.createTranveDto(tranveDto);  
        } catch (ConstraintViolationException  e) {
        		e.printStackTrace();
        }
	
    	int countStart = 0;
    	
    	// create a new transu2
    	TransuDto2 transuDto2 = new TransuDto2();
    	transuDto2.setSegmentId("text.required.msg");
    	transuDto2.setSegment("Value is required");
    	transuDto2.setRowId(1);
    	tranveDto = workService.createTransuDto(transuDto2,tranveDto);
    	
    	transuDto2 = new TransuDto2();
    	transuDto2.setSegmentId("current-url.placeholder");
    	transuDto2.setSegment("Current URL Value");
    	transuDto2.setRowId(2);
    	tranveDto = workService.createTransuDto(transuDto2,tranveDto);
    	
    	countStart = workService.listAllTransuDtos(tranveDto).size();
    	assertThat(countStart, is(2));

    	/**
    	 *	loco creation starts 
    	 */
    	
    	Loco2Dto loco2Dto = new Loco2Dto();
		loco2Dto.setTranslator("Ilkka");
		loco2Dto.setLocale("fi_FI");
		loco2Dto.setTranve(tranveDto.getId());
		try {
			loco2Dto=workService.createLocoDto(loco2Dto);
		} catch (ConstraintViolationException  e) {
			e.printStackTrace();
		}
		
    	int locoCountStart = 0; 
    	
    	// create a new transu2
    	transuDto2 = new TransuDto2();
    	transuDto2.setSegmentId("text.required.msg");
    	transuDto2.setSegment("Arvo tarvitaan");
    	transuDto2.setRowId(1);
    	try {
    		loco2Dto = workService.createTransuDto(transuDto2,loco2Dto.getId());
		} catch (ConstraintViolationException  e) {
			e.printStackTrace();
		}
    	
    	transuDto2.setSegmentId("current-url.placeholder");
    	transuDto2.setSegment("Nykyinen URL-arvo");
    	transuDto2.setRowId(2);
    	loco2Dto = workService.createTransuDto(transuDto2,loco2Dto.getId());

    	locoCountStart = workService.listAllTransuDtos(loco2Dto).size();
    	assertThat(locoCountStart, is(2));
    	
		// update row line 1
    	transuDto2=workService.getTransuDtoByRowId(1,loco2Dto.getId());    	
    	transuDto2.setSegmentId("Thats the way");
    	transuDto2.setSegment("Oh yeaah");
    	loco2Dto = workService.updateTransuDto(transuDto2);
    	
    	// receive values
    	transuDto2 = workService.getTransuDtoByRowId(1,loco2Dto.getId());
    	long storedId=transuDto2.getId();
    	long storedLocoId=transuDto2.getWork();
    	String storedStr=transuDto2.getSegmentId();
    	
    	Transu2 received = transu2Service.getTransuByWorkIdAndRowId(
    			loco2Dto.getId(),new Integer(1));
    	long receivedId = received.getId();
    	long receivedLocoId = received.getWork().getId();
    	String receivedStr=received.getSegmentId();
    	
    	assertEquals(receivedId, storedId);
    	assertEquals(receivedLocoId, storedLocoId);
    	assertThat(receivedStr, is("Thats the way"));
    	
    	// remove the first row
    	transuDto2=workService.getTransuDtoByRowId(1,loco2Dto.getId());
    	loco2Dto = workService.removeTransuDto(transuDto2);
    	
    	//
		// remove all
    	//
		int count = workService.listAllTransuDtos(loco2Dto).size();
		assertThat(count, is(1));
    	
		// remove => back to original state
		loco2Dto=workService.removeLocoDto(loco2Dto);
		count = workService.listAllLocoDtos().size();
		assertThat(count, is(0));
		
		tranveDto=workService.removeTranveDto(tranveDto);
		count = workService.listAllTranveDtos().size();
		assertThat(count, is(0));
		
		projectDto=workService.removeProjectDto(projectDto);
		count = workService.listAllProjectDtos().size();
		assertThat(count, is(0));
		
	}

}
