package translateit2.persistence.dto;

import static org.hamcrest.CoreMatchers.equalTo;
import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.CoreMatchers.not;
import static org.junit.Assert.*;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.context.web.WebAppConfiguration;

import translateit2.TranslateIt2v4Application;
import translateit2.persistence.model.Loco;

// http://www.vogella.com/tutorials/JUnit/article.html
@RunWith(SpringRunner.class)
@SpringBootTest(classes = TranslateIt2v4Application.class)
@WebAppConfiguration
public class LocoMapperTest {
	@Autowired
    private LocoMapper modelMapper;
	
    @Test
    public void shouldInstantiateMapper() {
    	assertThat(modelMapper, is(not(equalTo(null))));
    }

	@Test
	public void test() {
		modelMapper.validate();
	}	
	
    @Test
    public void map_loco_return_name_projectname() {
        // given
        final Loco loco = new Loco();
        loco.setName("John Doe");
        loco.setProjectName("Translate It 2");

        // when
        final LocoDto result = modelMapper.map(loco, LocoDto.class);

        // then
        assertThat(loco.getName(), is(equalTo(result.getName())));
        assertThat(loco.getProjectName(), is(equalTo(result.getProjectName())));        
    }
}
