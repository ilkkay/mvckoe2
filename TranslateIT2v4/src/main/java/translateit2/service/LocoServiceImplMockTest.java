package translateit2.service;

import static org.hamcrest.CoreMatchers.equalTo;
import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.assertThat;
import static org.junit.Assert.assertTrue;
import static org.mockito.Mockito.when;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.MockitoAnnotations;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import translateit2.config.LocoServiceTestConfig;
import translateit2.persistence.dao.LocoRepository;
import translateit2.persistence.model.Loco;
import translateit2.persistence.model.Transu;

//https://springframework.guru/integration-testing-with-spring-and-junit/
//https://springframework.guru/mockito-mock-vs-spy-in-spring-boot-tests/
//https://dzone.com/articles/mockito-mock-vs-spy-in-spring-boot-tests
//https://www.tutorialspoint.com/design_pattern/null_object_pattern.htm

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(classes = {LocoServiceTestConfig.class})
public class LocoServiceImplMockTest {

	@Autowired
    private LocoServiceImpl locoServiceImpl;

    private LocoRepository locoRepository;
    

    private Loco loco;
    
    private void loadLocoData(LocoServiceImpl locoService){
			Loco l = new Loco();
	    	l.setProjectName("Translate IT 2");
	    	l.setName("Ilkka");
	    	
	    	Transu transu = null;
	    	transu = new Transu();
	    	transu.setSourceSegm("sourceSegm 1");
	    	transu.setTargetSegm("targetSegm 1");
	    	transu.setLoco(l);
	    	
	    	transu = new Transu();
	    	transu.setSourceSegm("sourceSegm 2");
	    	transu.setTargetSegm("targetSegm 2");
	    	transu.setLoco(l);
	    	
	    	transu = new Transu();
	    	transu.setSourceSegm("sourceSegm 3");
	    	transu.setTargetSegm("targetSegm 3");
	    	transu.setLoco(l);
	    	locoService.createLoco(l);
	}
    
    @Before
    public void setupMock() {
        MockitoAnnotations.initMocks(this);
        locoServiceImpl = new LocoServiceImpl();
        locoServiceImpl.setLocoRepo(locoRepository);
        loadLocoData(locoServiceImpl);
    }
        
    @Test
    public void shouldReturnProjectName_whenGetLocoByIdIsCalled() throws Exception {
    	
    	Loco loco = locoServiceImpl.getLocoById(3L);
    	// Act    
        String retrieved = loco.getProjectName();
        // Assert    
        assertTrue(retrieved.equals("Translate IT 2"));    }
    
    @Test
    public void shouldReturnLoco_whenGetLocoByIdIsCalled() throws Exception {
        // Arrange     
        when(locoRepository.findOne(3L)).thenReturn(loco);
        // Act    
        Loco retrievedLoco = locoServiceImpl.getLocoById(3);
        // Assert
        
        assertThat(retrievedLoco, is(equalTo(loco)));
    }
}
