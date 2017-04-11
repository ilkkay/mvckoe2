package translateit2;

import static org.hamcrest.CoreMatchers.equalTo;
import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.CoreMatchers.not;
import static org.junit.Assert.*;

import java.util.List;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.ApplicationContext;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.context.web.WebAppConfiguration;

import translateit2.persistence.dto.LocoDto;
import translateit2.persistence.dto.TransuDto;
import translateit2.persistence.model.Transu;
import translateit2.service.LocoService;
import translateit2.service.LocoServiceImpl;
import translateit2.service.TransuServiceImpl;

//https://springframework.guru/integration-testing-with-spring-and-junit/
//https://springframework.guru/mockito-mock-vs-spy-in-spring-boot-tests/
//https://dzone.com/articles/mockito-mock-vs-spy-in-spring-boot-tests
//https://www.tutorialspoint.com/design_pattern/null_object_pattern.htm

@RunWith(SpringRunner.class)
@SpringBootTest(classes = TranslateIt2v4Application.class)
@WebAppConfiguration
public class TranslateIt2v4ApplicationTests {    
    private LocoService loco2Service;    
    @Autowired
    public void setLoco2Service(LocoService loco2Service) {
        this.loco2Service = loco2Service;
    }
    
    private TransuServiceImpl transuService;    
	@Autowired
    public void setTransuService(TransuServiceImpl transuService) {
        this.transuService = transuService;
    }
	
    @Autowired
    private ApplicationContext applicationContext;

    @Test
    public void whenApplicationStarts_thenContextIsInitialized() {
        assertThat(applicationContext, is(not(equalTo(null))));
    }
    
	/*
	 * So far only "positive" tests
	 */
	
	@Test
	public void get_locoDto_by_ProjectName_ReturnDto(){
		LocoDto locoDto = loco2Service.getLocoDtoByProjectName("Translate IT 2");
		assertEquals("Translate IT 2", locoDto.getProjectName());
	}
	
	@Test
	public void get_locoDto_by_ById_ReturnDto(){
		LocoDto locoDto = loco2Service.getLocoDtoByProjectName("Translate IT 2");
		long id = locoDto.getId(); 
		locoDto = loco2Service.getLocoDtoById(id);
		long received = locoDto.getId();
		assertThat(received, is(equalTo(id)));
		assertEquals("Translate IT 2", locoDto.getProjectName());
	}
		
	@Test
	public void create_locodto_return_original_loco(){
        LocoDto locoDto = new LocoDto();
		locoDto.setProjectName("Translate IT 3");
		locoDto.setName("Juha");	
		locoDto=loco2Service.createLocoDto(locoDto);
		
		locoDto = loco2Service.getLocoDtoByProjectName("Translate IT 3");
		String receivedName = locoDto.getName();
		String receivedProject= locoDto.getProjectName();
		assertThat(receivedName, is(equalTo("Juha")));
		assertThat(receivedProject, is(equalTo("Translate IT 3")));
		
		// remove => back to original state
		loco2Service.removeLocoDto(locoDto);
		int count = loco2Service.listAllLocoDtos().size();
		assertThat(count, is(2));
	}
	
	@Test
	public void update_locodto_return_original_loco(){
		LocoDto locoDto = loco2Service.getLocoDtoByProjectName("Translate IT 2");
		locoDto.setProjectName("Translate IT 3");
		locoDto.setName("Juha");	
		locoDto=loco2Service.updateLocoDto(locoDto);
		
		locoDto = loco2Service.getLocoDtoByProjectName("Translate IT 3");
		String receivedName = locoDto.getName();
		String receivedProject= locoDto.getProjectName();
		assertThat(receivedName, is(equalTo("Juha")));
		assertThat(receivedProject, is(equalTo("Translate IT 3")));
		
		int count = loco2Service.listAllLocoDtos().size();
		assertThat(count, is(2));
		
		locoDto.setProjectName("Translate IT 2");
		locoDto.setName("Ilkka");	
		locoDto=loco2Service.updateLocoDto(locoDto);
		
		receivedName = locoDto.getName();
		receivedProject= locoDto.getProjectName();
		assertThat(receivedName, is(equalTo("Ilkka")));
		assertThat(receivedProject, is(equalTo("Translate IT 2")));
	}

	@Test
	public void create_transudto_return_increment_transu_count_by_one(){
		LocoDto locoDto = loco2Service.getLocoDtoByProjectName("Translate IT 2");
    	TransuDto transuDto = new TransuDto();

    	int locoCountStart = loco2Service.listAllTransuDtos(locoDto).size();
    	
    	// create a new transu
    	transuDto.setSourceSegm("SourceSegm 4");
    	transuDto.setTargetSegm("TargetSegm 4");
    	transuDto.setRowId(4);
    	locoDto = loco2Service.createTransuDto(transuDto,locoDto.getId());
    	
    	// receive values
    	transuDto=loco2Service.getTransuDtoByRowId(4,locoDto.getId());
    	long storedId=transuDto.getId();
    	long storedLocoId=transuDto.getLoco();
    	String storedStr=transuDto.getSourceSegm();
    	
    	Transu received = transuService.getTransuByLocoIdAndRowId(
    			locoDto.getId(),new Integer(4));
    	long receivedId = received.getId();
    	long receivedLocoId = received.getLoco().getId();
    	String receivedStr=received.getSourceSegm();
    	
    	assertEquals(receivedId, storedId);
    	assertEquals(receivedLocoId, storedLocoId);
    	assertThat(storedStr, is("SourceSegm 4"));
    	assertThat(receivedStr, is("SourceSegm 4"));
    	
    	// number of transu lines in data base
    	int locoCount = loco2Service.listAllTransuDtos(locoDto).size();
    	int transuCount = ((List<Transu>) transuService.getTransusByLocoId(locoDto.getId())).size();
    	
    	assertThat(transuCount, is(locoCount));
    	assertThat(transuCount, is(locoCountStart+1));
    	assertThat(locoCount, is(locoCountStart+1)); 
	}
	
	@Test
	public void update_TransuDto_returnDto(){
		LocoDto locoDto = loco2Service.getLocoDtoByProjectName("Translate IT 2");
    	int locoCountStart = loco2Service.listAllTransuDtos(locoDto).size();
		
		// update row line 3
    	TransuDto transuDto=loco2Service.getTransuDtoByRowId(3,locoDto.getId());    	
    	transuDto.setSourceSegm("Thats the way");
    	transuDto.setTargetSegm("Oh yeaah");
    	locoDto = loco2Service.updateTransuDto(transuDto);
    	
    	// receive values
    	transuDto = loco2Service.getTransuDtoByRowId(3,locoDto.getId());
    	long storedId=transuDto.getId();
    	long storedLocoId=transuDto.getLoco();
    	String storedStr=transuDto.getSourceSegm();
    	
    	Transu received = transuService.getTransuByLocoIdAndRowId(
    			locoDto.getId(),new Integer(3));
    	long receivedId = received.getId();
    	long receivedLocoId = received.getLoco().getId();
    	String receivedStr=received.getSourceSegm();
    	
    	assertEquals(receivedId, storedId);
    	assertEquals(receivedLocoId, storedLocoId);
    	assertThat(receivedStr, is("Thats the way"));
    	
    	// line count in data base
    	int locoCount = loco2Service.listAllTransuDtos(locoDto).size();
    	int transuCount = ((List<Transu>) transuService.getTransusByLocoId(locoDto.getId())).size();
    	
    	assertThat(transuCount, is(locoCount));
    	assertThat(transuCount, is(locoCountStart));
    	assertThat(locoCount, is(locoCountStart));    	
	}
	
	//@Test
	public void remove_TransuDto_return_decrement_by_one(){
		LocoDto locoDto = loco2Service.getLocoDtoByProjectName("Translate IT 2");
    	int locoCountStart = loco2Service.listAllTransuDtos(locoDto).size();

    	// remove the first row
    	TransuDto transuDto=loco2Service.getTransuDtoByRowId(1,locoDto.getId());
    	locoDto = loco2Service.removeTransuDto(transuDto);
    	
    	int locoCount = loco2Service.listAllTransuDtos(locoDto).size();
    	int transuCount = ((List<Transu>) transuService.getTransusByLocoId(locoDto.getId())).size();
    	
    	assertThat(transuCount, is(locoCount));
    	assertThat(transuCount, is(locoCountStart-1));
    	assertThat(locoCount, is(locoCountStart-1));    	
	}
	
	@Test
	public void list_loco_dtos_return_fields(){
		List<LocoDto> dtos = loco2Service.listInOrderAllLocoDtos();

		String receivedName = dtos.get(0).getName();
		String receivedProject= dtos.get(0).getProjectName();
		assertThat(receivedName, is(equalTo("Ilkka")));
		assertThat(receivedProject, is(equalTo("Translate IT 2")));
		
		receivedName = dtos.get(1).getName();
		receivedProject= dtos.get(1).getProjectName();
		assertThat(receivedName, is(equalTo("Jukka")));
		assertThat(receivedProject, is(equalTo("Translate IT 4")));
	}
	
	
	/*
	@Test
	public void list_transu_dtos_return_fields(){
		LocoDto locoDto = loco2Service.getLocoDtoByProjectName("Translate IT 2");
		List<TransuDto> dtos = loco2Service.listAllTransuDtos(locoDto);

		String receivedSegment ="";
		String receivedTarget= "";
		
		receivedSegment = dtos.get(0).getSourceSegm();
		receivedTarget = dtos.get(0).getTargetSegm();
		assertThat(receivedSegment, is(equalTo("SourceSegm 1")));
		assertThat(receivedTarget, is(equalTo("TargetSegm 1")));
		
		receivedSegment = dtos.get(1).getSourceSegm();
		receivedTarget = dtos.get(1).getTargetSegm();
		assertThat(receivedSegment, is(equalTo("SourceSegm 2")));
		assertThat(receivedTarget, is(equalTo("TargetSegm 2")));
		
		receivedSegment = dtos.get(2).getSourceSegm();
		receivedTarget = dtos.get(2).getTargetSegm();
		assertThat(receivedSegment, is(equalTo("SourceSegm 3")));
		assertThat(receivedTarget, is(equalTo("TargetSegm 3")));
		
	}
	*/
	
}

