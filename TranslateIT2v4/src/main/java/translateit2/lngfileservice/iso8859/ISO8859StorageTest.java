package translateit2.lngfileservice.iso8859;

import static org.hamcrest.CoreMatchers.equalTo;
import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.*;

import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Locale;

import javax.validation.Configuration;
import javax.validation.Validation;
import javax.validation.ValidatorFactory;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.mockito.runners.MockitoJUnitRunner;
import org.springframework.context.support.ReloadableResourceBundleMessageSource;

import translateit2.fileloader.storage.FileSystemStorageService;
import translateit2.fileloader.storage.StorageException;
import translateit2.util.Messages;

@RunWith(MockitoJUnitRunner.class)
public class ISO8859StorageTest {

	private ISO8859StorageImpl iso8859Storage = new ISO8859StorageImpl ();
	
	private Messages messages;
	
	@Mock
	FileSystemStorageService fileStorage;
	
	@Before
	public void setUp() throws Exception {
		MockitoAnnotations.initMocks(this);

		iso8859Storage.setFileStorage(fileStorage);
			    
	    ReloadableResourceBundleMessageSource messageSource = 
	    		new ReloadableResourceBundleMessageSource();
	    messageSource.setBasename("classpath:messages");
	    messageSource.setDefaultEncoding("ISO-8859-1");	    
	    messageSource.setFallbackToSystemLocale(false);
	    
	    messages = new Messages(messageSource);
	    messages.init(Locale.ENGLISH);
	}
	
	//@Test
	public void failFileName_IfLanguageCodeMissing() {
		Path uploadedLngFile=Paths.get("d:\\test_fi.properties");
		long locoId=0;

		try {
			iso8859Storage.checkValidity(uploadedLngFile, locoId);
			fail ("No exception thrown");
		} catch (StorageException e) {
			assertThat(e.getMessage().contains
					(messages.get("FileStorageService.code_missing"))
						,is(equalTo(true)));
		}
	}

	@Test
	public void failFileName_IfEncodingSameAsInLoco() {
		Path uploadedLngFile=Paths.get("d:\\test_fi.properties");
		long locoId=0;

		try {
			iso8859Storage.checkValidity(uploadedLngFile, locoId);
			fail ("No exception thrown");
		} catch (StorageException e) {
			assertThat(e.getMessage().contains
					(messages.get("FileStorageService.false_encoding"))
						,is(equalTo(true)));
		}
	}
}
