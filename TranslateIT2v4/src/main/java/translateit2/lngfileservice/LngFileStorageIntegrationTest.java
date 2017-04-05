package translateit2.lngfileservice;

import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.CoreMatchers.equalTo;
import static org.hamcrest.CoreMatchers.instanceOf;
import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.*;

import java.nio.file.Path;
import java.util.Arrays;
import java.util.Optional;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.context.web.WebAppConfiguration;

import translateit2.TranslateIt2v4Application;
import translateit2.lngfileservice.defformat.DefaultLngStorageImpl;
import translateit2.lngfileservice.factory.LngFileServiceFactory;
import translateit2.lngfileservice.factory.LngFileServiceFactoryImpl;
import translateit2.lngfileservice.factory.LngFileServiceFactoryImpl2;
import translateit2.lngfileservice.factory.LngFileServiceProvider;
import translateit2.lngfileservice.iso8859.ISO8859Storage;
import translateit2.lngfileservice.iso8859.ISO8859StorageImpl;
import translateit2.lngfileservice.xliff.XLIFFStorageImpl;

@RunWith(SpringRunner.class)
@SpringBootTest(classes = TranslateIt2v4Application.class)
@WebAppConfiguration
public class LngFileStorageIntegrationTest {

	@Autowired
	LngFileServiceFactoryImpl2 lngFileServiceFactory2;

	@Autowired
	LngFileServiceProvider lngFileServiceProvider;
	
	@Test
    public void printBeans() {
        System.out.println(Arrays.asList(lngFileServiceProvider.getContext()
        		.getBeanDefinitionNames()));
    }
	    
	@Test
	public void getProviderService() {
		Optional <LngFileStorage>  service = null;
		service = lngFileServiceProvider.getService("iso8859Service");	
		assertThat(service.get(), instanceOf(ISO8859StorageImpl.class));
		
		service = lngFileServiceProvider.getService("xliffService");	
		assertThat(service.get(), instanceOf(XLIFFStorageImpl.class));
		
		service = lngFileServiceProvider.getService("defaultLngStorageImpl");	
		assertThat(service.get(), instanceOf(DefaultLngStorageImpl.class));
		
		try {
			service = lngFileServiceProvider.getService("PO");
			fail("No exception was thrown");
		} catch (Exception e) {
			assertThat(e).hasMessageContaining("No bean");
		}
	}

	
	@Test
	public void getFactoryService_cached() {
		LngFileStorage service  = null;
		
		service = LngFileServiceFactoryImpl.getService("XLIFF");
		assertThat(service.getGreetings(), is(equalTo("XLIFFServiceImpl")));
		assertThat(service, instanceOf(XLIFFStorageImpl.class));
		
		service = LngFileServiceFactoryImpl.getService("ISO8859");
		assertThat(service.getGreetings(), is(equalTo("ISO8859ServiceImpl")));
		assertThat(service, instanceOf(ISO8859StorageImpl.class));
		
		service = LngFileServiceFactoryImpl.getService("Default");
		assertThat(service.getGreetings(), is(equalTo("DefaultLngServiceImpl")));
		assertThat(service, instanceOf(DefaultLngStorageImpl.class));
				
		try {
			service = LngFileServiceFactoryImpl.getService("PO");
			fail("No exception was thrown");
		} catch (Exception e) {
			assertThat(e).hasMessageContaining("Unknown service type");
		}
		
		Path p = service.getPath("dotcms_en.properties");		
		assertThat(p.getParent().toString(), is(equalTo("upload-dir2")));
		
		LngFileServiceFactoryImpl.listFormatsSupported().stream()
			.forEach(System.out::println);
	}
	
	
	@Test
	public void getFactoryService() {
		LngFileStorage service = null;
		
		service = lngFileServiceFactory2.getService("XLIFF").get();
		assertThat(service.getGreetings(), is(equalTo("XLIFFServiceImpl")));
		
		service = lngFileServiceFactory2.getService("ISO8859").get();;
		assertThat(service.getGreetings(), is(equalTo("ISO8859ServiceImpl")));
		
		service = lngFileServiceFactory2.getService("Default").get();;
		assertThat(service.getGreetings(), is(equalTo("DefaultLngServiceImpl")));
		
		try {
			service = lngFileServiceFactory2.getService("PO").get();;
			fail("No exception was thrown");
		} catch (Exception e) {
			//assertThat(e).hasMessageContaining("Unknown service type");
			assertThat(e).hasMessageContaining("No value present");
		}
		
		Path p = service.getPath("dotcms_en.properties");		
		assertThat(p.getParent().toString(), is(equalTo("upload-dir2")));
		
		LngFileServiceFactoryImpl.listFormatsSupported().stream()
			.forEach(System.out::println);
	}
	
	// https://blog.goyello.com/2015/10/01/different-ways-of-testing-exceptions-in-java-and-junit/
	@Test
	public void failToUpLoad_ifFormatNotSupported() {
		
		boolean exceptionThrown=false;
		try {
			LngFileStorage service = 
					LngFileServiceFactoryImpl.getService("xxx");
		} catch (Exception e) {
			exceptionThrown=true;
			assertThat(e)
            	.isInstanceOf(RuntimeException.class);
		}
		
		assertThat(exceptionThrown, is(equalTo(true)));
		
	}

}
