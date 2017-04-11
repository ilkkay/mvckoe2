package translateit2.persistence.dto;

import static org.hamcrest.CoreMatchers.equalTo;
import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.CoreMatchers.not;
import static org.junit.Assert.assertThat;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.modelmapper.convention.MatchingStrategies;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.context.web.WebAppConfiguration;

import translateit2.TranslateIt2v4Application;
import translateit2.persistence.model.Loco;
import translateit2.persistence.model2.Loco2;
import translateit2.persistence.model2.Project;

//http://www.vogella.com/tutorials/JUnit/article.html
@RunWith(SpringRunner.class)
@SpringBootTest(classes = TranslateIt2v4Application.class)
@WebAppConfiguration
public class WorkMapperTest {
	@Autowired
	private WorkMapper modelMapper;

	@Test
	public void shouldInstantiateMapper() {
		assertThat(modelMapper, is(not(equalTo(null))));
	}

	@Test
	public void test() {
		//modelMapper.getConfiguration().setMatchingStrategy(MatchingStrategies.LOOSE);
		modelMapper.validate();
	}	

	@Test
	public void map_project_return_name_projectname() {
		// given
		final Project prj = new Project();
		prj.setManager("John Doe");
		prj.setProjectName("Translate It 2");

		// when
		final ProjectDto result = modelMapper.map(prj, ProjectDto.class);
		
		// then
		assertThat(prj.getManager(), is(equalTo(result.getManager())));
		assertThat(prj.getProjectName(), is(equalTo(result.getProjectName())));        
	}
}

