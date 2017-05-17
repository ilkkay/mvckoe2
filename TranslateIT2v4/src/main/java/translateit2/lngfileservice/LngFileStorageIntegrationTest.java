package translateit2.lngfileservice;

import static org.assertj.core.api.Assertions.assertThat;
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
import translateit2.lngfileservice.factory.LngFileServiceFactory;
import translateit2.lngfileservice.factory.LngFileServiceFactoryImpl;

@RunWith(SpringRunner.class)
@SpringBootTest(classes = TranslateIt2v4Application.class)
@WebAppConfiguration
public class LngFileStorageIntegrationTest {
	
	private LngFileServiceFactory lngFileServiceFactory;
	@Autowired
	public void setLngFileServiceFactory(LngFileServiceFactoryImpl lngFileServiceFactory) {
		this.lngFileServiceFactory = lngFileServiceFactory;
	}	
	
	@Test
	public void getFactoryService_cached() {
		LngFileStorage service  = null;
		
		service = lngFileServiceFactory.getService(LngFileFormat.XLIFF).get();
		assertThat(service.getFileFormat(), is(equalTo(LngFileFormat.XLIFF)));
		
		service = lngFileServiceFactory.getService(LngFileFormat.PROPERTIES).get();
		assertThat(service.getFileFormat(), is(equalTo(LngFileFormat.PROPERTIES)));
		
		service = lngFileServiceFactory.getService(LngFileFormat.DEFAULT).get();
		assertThat(service.getFileFormat(), is(equalTo(LngFileFormat.DEFAULT)));
				
		try {
			service = lngFileServiceFactory.getService(LngFileFormat.PO).get();
			fail("No exception was thrown");
		} catch (Exception e) {
			assertThat(e).hasMessageContaining("No value present");
		}
		
		Path p = service.getPath("dotcms_en.properties");		
		assertThat(p.getParent().toString(), is(equalTo("upload-dir3")));
		
		lngFileServiceFactory.listFormatsSupported().stream()
			.forEach(System.out::println);
	}

	// https://blog.goyello.com/2015/10/01/different-ways-of-testing-exceptions-in-java-and-junit/
	@Test
	public void failToUpLoad_ifFormatNotSupported() {

		try {
			lngFileServiceFactory.getService(LngFileFormat.PO).get();
			fail("No exception was thrown");
		} catch (Exception e) {
			assertThat(e)
            	.isInstanceOf(RuntimeException.class);
		}
		
	}

}
