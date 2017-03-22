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
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.context.web.WebAppConfiguration;

import translateit2.config.LocoServiceTestConfig;
import translateit2.persistence.dao.LocoRepository;
import translateit2.persistence.dto.LocoDto;
import translateit2.persistence.dto.TransuDto;
import translateit2.persistence.model.Loco;
import translateit2.persistence.model.Transu;
import translateit2.service.Loco2Service;
import translateit2.service.Loco2ServiceImpl;
import translateit2.service.LocoService;
import translateit2.service.LocoServiceImpl;
import translateit2.service.TransuService;
import translateit2.service.TransuServiceImpl;

//@RunWith(SpringJUnit4ClassRunner.class)
//@ContextConfiguration(classes = {LocoServiceTestConfig.class}) not needed
@RunWith(SpringRunner.class)
@SpringBootTest(classes = TranslateIt2v4Application.class)
@WebAppConfiguration
public class TranslateIt2v4ApplicationTests {
    private LocoServiceImpl locoService;    
    @Autowired
    public void setLoco2Service(LocoServiceImpl locoService) {
        this.locoService = locoService;
    }
    
    private Loco2ServiceImpl loco2Service;    
    @Autowired
    public void setLoco2Service(Loco2ServiceImpl loco2Service) {
        this.loco2Service = loco2Service;
    }
    

    private TransuServiceImpl transuService;    
	@Autowired
    public void setTransuService(TransuServiceImpl transuService) {
        this.transuService = transuService;
    }
	/*
    private LocoService locoService;    
    @Autowired
    public void setLocoService(LocoService locoService) {
        this.locoService = locoService;
    }
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
	}
	
	@Test
	public void list_all_Dtos_return_count(){
		LocoDto locoDto = loco2Service.getLocoDtoByProjectName("Translate IT 2");
		int count = loco2Service.listAllLocoDtos().size();
		assertThat(count, is(2)); // initialize database
	}
	
	@Test
	public void create_LocoDto_returnDto(){
        LocoDto locoDto = new LocoDto();
		locoDto.setProjectName("Translate IT 3");
		locoDto.setName("Juha");	
		locoDto=loco2Service.createLocoDto(locoDto);
		String received = locoDto.getName();
		assertThat(received, is(equalTo("Juha")));
	}
	
	@Test
	public void update_LocoDto_returnDto(){
		LocoDto locoDto = loco2Service.getLocoDtoByProjectName("Translate IT 2");
		locoDto.setProjectName("Translate IT 3");
		locoDto.setName("Juha");	
		locoDto=loco2Service.updateLocoDto(locoDto);
		String received = locoDto.getName();
		assertThat(received, is(equalTo("Juha")));
	}
		
	@Test
	public void create_TransuDto_returnDto(){
		LocoDto locoDto = loco2Service.getLocoDtoByProjectName("Translate IT 2");
    	TransuDto transuDto = new TransuDto();
    	
    	transuDto.setSourceSegm("sourceSegm 4");
    	transuDto.setTargetSegm("targetSegm 4");
    	transuDto.setRowId(4);
    	loco2Service.createTransuDto(transuDto,locoDto);
    	transuDto=loco2Service.getTransuDtoByRowId(4,locoDto);
    	long storedId=transuDto.getId();
    	
    	Transu received = transuService.getTransuByLocoIdAndRowId(
    			locoDto.getId(),new Integer(4));
    	long receivedId = received.getId();

    	assertEquals(receivedId, storedId);
	}

	
	/*
	@Test
	public void contextLoads() {
	}
	*/
    
	// MethodName_StateUnderTest_ExpectedBehavior
	// Sum_NegativeNumberAs1stParam_ExceptionThrown() 
    /*
	@Test
	public void get_locoDto_by_ProjectName_ReturnDto(){
		LocoDto locoDto = locoService.getLocoDtoByProjectName("Translate IT 2");
		assertEquals("Translate IT 2", locoDto.getProjectName());
	}
	
	@Test
	public void get_locoDto_by_ProjectName_ReturnNull(){
		LocoDto locoDto = locoService.getLocoDtoByProjectName("Translate");
		assertEquals(locoDto, null);;
	}
	
	@Test
	public void get_locoDto_by_ById_ReturnDto(){
		LocoDto locoDto = locoService.getLocoDtoByProjectName("Translate IT 2");
		long id = locoDto.getId(); // should be constant ??? that we dont know
		locoDto = locoService.getLocoDtoById(0);
		long received = locoDto.getId();
		assertThat(received, not(equalTo(id)));
	}
	*/
	
}

