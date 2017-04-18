package translateit2.util;

import static org.hamcrest.CoreMatchers.equalTo;
import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.*;
import static org.mockito.Mockito.when;

import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Locale;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.mockito.runners.MockitoJUnitRunner;
import org.springframework.context.support.ReloadableResourceBundleMessageSource;

import translateit2.fileloader.storage.StorageException;
import translateit2.persistence.dto.TranveDto;
import translateit2.service.WorkService;
import translateit2.util.ISO8859util;
import translateit2.util.LngFileType;
import translateit2.util.Messages;

@RunWith(MockitoJUnitRunner.class)
public class ISO8859utilTest {
	private Messages messages;
	
	private ISO8859util iso8859util;

	@Mock
	WorkService workService;

	@Before
	public void setUp() throws Exception {
		MockitoAnnotations.initMocks(this);

		ReloadableResourceBundleMessageSource messageSource = 
				new ReloadableResourceBundleMessageSource();
		messageSource.setBasename("classpath:messages");
		messageSource.setDefaultEncoding("ISO-8859-1");	    
		messageSource.setFallbackToSystemLocale(false);

		messages = new Messages(messageSource);
		messages.init(Locale.ENGLISH);
		
		iso8859util = new ISO8859util();
		iso8859util.setMessages(messages);
		iso8859util.setWorkService(workService);
	}

	@Test
	public void failFileName_IfFileExtensionMissing() {
		Path uploadedLngFile=Paths.get("d:\\test_fi.propertiesXXX");
		long locoId=0;

		try {
			iso8859util.checkFileExtension(uploadedLngFile, locoId);
			fail ("No exception thrown");
		} catch (StorageException e) {
			assertThat(e.getMessage().contains
					(messages.getPart("FileStorageService.not_properties_file"))
					,is(equalTo(true)));
		}

		uploadedLngFile=Paths.get("d:\\test_fi.properties");
		try {
			iso8859util.checkFileExtension(uploadedLngFile, locoId);
		} catch (StorageException e) {
			fail ("Exception was thrown when it shoud have not");
		}
	}

	@Test
	public void failFileName_IfLocaleIsMissing() {
		TranveDto tranveDto = null;
		// WHEN: get tranveDto using respective tranveId)
		tranveDto = new TranveDto();
		tranveDto.setId(1L);
		tranveDto.setFormat("properties");
		tranveDto.setType(LngFileType.ISO8859_1);
		
		Path uploadedLngFile=Paths.get("d:\\test.properties");
		// WHEN expect ???
		when(workService.getTranveDtoById(tranveDto.getId())).thenReturn(tranveDto);

		long locoId = 1L;
		
		try {
			iso8859util.checkFileNameFormat(uploadedLngFile, locoId);
			fail ("No exception thrown");
		} catch (StorageException e) {
			assertThat(e.getMessage().contains
					(messages.getPart("FileStorageService.code_missing"))
					,is(equalTo(true)));
		}

		uploadedLngFile=Paths.get("d:\\test_fi.properties");
		try {
			iso8859util.checkFileNameFormat(uploadedLngFile, locoId);
		} catch (StorageException e) {
			fail ("Exception was thrown when it shoud have not");
		}

		uploadedLngFile=Paths.get("d:\\test_fi_FI.properties");
		try {
			iso8859util.checkFileNameFormat(uploadedLngFile, locoId);
		} catch (StorageException e) {
			fail ("Exception was thrown when it shoud have not");
		}

		uploadedLngFile=Paths.get("d:\\messages_fi_utf8.properties");
		try {
			iso8859util.checkFileNameFormat(uploadedLngFile, locoId);
		} catch (StorageException e) {
			assertThat(e.getMessage().contains
					(messages.getPart("FileStorageService.code_missing"))
					,is(equalTo(true)));
		}
	}

	@Test
	public void failFileName_IfNoKeyValuePairsInTranve() {
		TranveDto tranveDto = null;
		// WHEN: get tranveDto using respective tranveId)
		tranveDto = new TranveDto();
		tranveDto.setId(1L);
		tranveDto.setFormat("properties");
		tranveDto.setType(LngFileType.UTF_8);

		// WHEN expect ISO8859
		when(workService.getTranveDtoById(tranveDto.getId())).thenReturn(tranveDto);

		// THEN throw exception if the upload file contains only empty or comment lines
		Path uploadedLngFile=Paths.get("d:\\empty-test_fi-utf8.properties");
		try {
			iso8859util.checkEmptyFile(uploadedLngFile, 1L);
			fail ("No exception thrown");
		} catch (StorageException e) {
			assertThat(e.getMessage().contains
					(messages.getPart("FileStorageService.empty_properties_file"))
					,is(equalTo(true)));
		}
	}

	@Test
	public void failFileName_IfEncodingNotSameAsInTranve() {
		TranveDto tranveDto = null;
		// WHEN: get tranveDto using respective tranveId)
		tranveDto = new TranveDto();
		tranveDto.setId(1L);
		tranveDto.setFilename("dotcms");
		tranveDto.setLocale("en_EN");
		tranveDto.setFormat("properties");
		tranveDto.setType(LngFileType.ISO8859_1);

		// WHEN expect ISO8859
		when(workService.getTranveDtoById(tranveDto.getId())).thenReturn(tranveDto);

		// THEN throw exception if the upload file is UTF-8
		Path uploadedLngFile=Paths.get("d:\\messages_fi-UTF8.properties");
		try {
			iso8859util.checkFileCharSet(uploadedLngFile, 1L);
			fail ("No exception thrown");
		} catch (StorageException e) {
			assertThat(e.getMessage().contains
					(messages.getPart("FileStorageService.false_ISO8859_encoding"))
					//It should be ISO8859
					,is(equalTo(true)));
		}

		// WHEN expect UTF-8 
		tranveDto = new TranveDto();
		tranveDto.setId(2L);
		tranveDto.setType(LngFileType.UTF_8);

		when(workService.getTranveDtoById(tranveDto.getId())).thenReturn(tranveDto);

		// THEN throw exception if the upload file is ISO8859
		uploadedLngFile=Paths.get("d:\\messages_fi.properties");
		try {
			iso8859util.checkFileCharSet(uploadedLngFile, 2L);
			fail ("No exception thrown");
		} catch (StorageException e) {
			assertThat(e.getMessage().contains
					(messages.getPart("FileStorageService.false_UTF8_encoding"))
					//It should be UTF-8
					,is(equalTo(true)));
		}

		// WHEN expect UTF-8 
		tranveDto = new TranveDto();
		tranveDto.setId(3L);
		tranveDto.setType(LngFileType.UTF_8);

		when(workService.getTranveDtoById(tranveDto.getId())).thenReturn(tranveDto);

		// THEN don't throw any exception if the upload file is UTF-8
		uploadedLngFile=Paths.get("d:\\messages_fi-UTF8.properties");
		try {
			iso8859util.checkFileCharSet(uploadedLngFile, 3L);
		} catch (StorageException e) {
			fail ("An exception was thrown");
		}

		// WHEN expect ISO8859 
		tranveDto = new TranveDto();
		tranveDto.setId(4L);
		tranveDto.setType(LngFileType.ISO8859_1);

		when(workService.getTranveDtoById(tranveDto.getId())).thenReturn(tranveDto);

		// THEN don't throw any exception if the upload file is ISO8859 
		uploadedLngFile=Paths.get("d:\\messages_fi.properties");
		try {
			iso8859util.checkFileCharSet(uploadedLngFile, 4L);
			//iso8859util.checkValidity(uploadedLngFile, 4L);

		} catch (StorageException e) {
			fail ("An exception was thrown");
		}
	}
}
