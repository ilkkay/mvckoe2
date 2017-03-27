package translateit2.lngfileservice;

import static org.hamcrest.CoreMatchers.equalTo;
import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.*;

import java.nio.file.Path;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.context.web.WebAppConfiguration;

import translateit2.TranslateIt2v4Application;

@RunWith(SpringRunner.class)
@SpringBootTest(classes = TranslateIt2v4Application.class)
@WebAppConfiguration
public class LngFileStorageIntegrationTest {
	
	@Test
	public void test() {
		LngFileStorage service  = null;
		service = LngFileServiceFactory.getService("XLIFF");
		assertThat(service.getGreetings(), is(equalTo("XLIFFServiceImpl")));
		
		service = LngFileServiceFactory.getService("ISO8859");
		assertThat(service.getGreetings(), is(equalTo("ISO8859ServiceImpl")));
		
		service = LngFileServiceFactory.getService("Default");
		assertThat(service.getGreetings(), is(equalTo("DefaultLngServiceImpl")));
		
		Path p = service.getPath("dotcms_en.properties");		
		assertThat(p.getParent().toString(), is(equalTo("upload-dir2")));
		
	}

}
