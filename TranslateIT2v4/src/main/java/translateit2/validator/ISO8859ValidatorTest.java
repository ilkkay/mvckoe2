package translateit2.validator;

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
import translateit2.lngfileservice.LanguageFileFormat;
import translateit2.lngfileservice.LanguageFileType;
import translateit2.persistence.dto.ProjectDto;
import translateit2.persistence.dto.WorkDto;
import translateit2.service.ProjectService;
import translateit2.util.Messages;

@RunWith(MockitoJUnitRunner.class)
public class ISO8859ValidatorTest {
    private Messages messages;

    private LanguageFileValidator iso8859validator;

    @Mock
    ProjectService projectService;

    @Before
    public void setUp() throws Exception {
        MockitoAnnotations.initMocks(this);

        ReloadableResourceBundleMessageSource messageSource = new ReloadableResourceBundleMessageSource();
        messageSource.setBasename("classpath:messages");
        messageSource.setDefaultEncoding("ISO-8859-1");
        messageSource.setFallbackToSystemLocale(false);

        messages = new Messages(messageSource);
        messages.init(Locale.ENGLISH);

        iso8859validator = new ISO8859ValidatorImpl();
        ((ISO8859ValidatorImpl) iso8859validator).setMessages(messages);
        ((ISO8859ValidatorImpl) iso8859validator).setProjectService(projectService);
    }

    @Test
    public void failFileName_IfFileExtensionMissing() {
        Path uploadedLngFile = Paths.get("d:\\test_fi.propertiesXXX");

        try {
            iso8859validator.checkFileExtension(uploadedLngFile);
            fail("No exception thrown");
        } catch (StorageException e) {
            assertThat(e.getMessage().contains(messages.getPart("FileStorageService.not_properties_file")),
                    is(equalTo(true)));
        }

        uploadedLngFile = Paths.get("d:\\test_fi.properties");
        try {
            iso8859validator.checkFileExtension(uploadedLngFile);
        } catch (StorageException e) {
            fail("Exception was thrown when it shoud have not");
        }
    }

    @Test
    public void failFileName_IfLocaleIsMissing() {
        // WHEN language code is missing from filename
        Path uploadedLngFile = Paths.get("d:\\test.properties");

        try {
            iso8859validator.checkFileNameFormat(uploadedLngFile);
            fail("No exception thrown");
        } catch (StorageException e) {
            assertThat(e.getMessage().contains(messages.getPart("FileStorageService.code_missing")), is(equalTo(true)));
        }

        // WHEN using correct file naming = application name + language
        uploadedLngFile = Paths.get("d:\\test_fi.properties");
        try {
            iso8859validator.checkFileNameFormat(uploadedLngFile);
        } catch (StorageException e) {
            fail("Exception was thrown when it shoud have not");
        }

        // WHEN using correct file naming = application name + language +
        // country
        uploadedLngFile = Paths.get("d:\\test_fi_FI.properties");
        try {
            iso8859validator.checkFileNameFormat(uploadedLngFile);
        } catch (StorageException e) {
            fail("Exception was thrown when it shoud have not");
        }

        // WHEN using correct file naming = application name + language +
        // country
        uploadedLngFile = Paths.get("d:\\dotcms_fi_FI-utf8.properties");
        try {
            iso8859validator.checkFileNameFormat(uploadedLngFile);
        } catch (StorageException e) {
            fail("Exception was thrown when it shoud have not");
        }

        // WHEN locale has more than two characters in country
        uploadedLngFile = Paths.get("d:\\messages_fi_utf8.properties");
        try {
            iso8859validator.checkFileNameFormat(uploadedLngFile);
        } catch (StorageException e) {
            assertThat(e.getMessage().contains(messages.getPart("FileStorageService.code_missing")), is(equalTo(true)));
        }
    }

    @Test
    public void failFileName_IfNoKeyValuePairsInTranve() {
        // given project ...
        final long projectId = 666L;
        ProjectDto prj = new ProjectDto();
        prj.setName("Translate IT 2");
        prj.setFormat(LanguageFileFormat.PROPERTIES);
        prj.setType(LanguageFileType.ISO8859_1);
        prj.setId(projectId);

        // and Work
        long workId = 1L;
        WorkDto work = new WorkDto();
        work.setProjectId(prj.getId());
        work.setId(workId);

        // WHEN expect UTF-8 and WHEN file has no key/value pairs
        Path uploadedLngFile = Paths.get("d:\\empty-test_fi-utf8.properties");
        when(projectService.getWorkDtoById(1L)).thenReturn(work);
        when(projectService.getProjectDtoById(666L)).thenReturn(prj);

        // THEN throw exception if the upload file contains only empty or
        // comment lines
        try {
            iso8859validator.checkEmptyFile(uploadedLngFile, 1L);
            fail("No exception thrown");
        } catch (StorageException e) {
            assertThat(e.getMessage().contains(messages.getPart("FileStorageService.empty_properties_file")),
                    is(equalTo(true)));
        }
    }

    @Test
    public void failFileName_IfEncodingNotSameAsInTranve() {
        // given project ...
        ProjectDto prj = null;
        WorkDto work = null;
        final long projectId = 666L;
        prj = new ProjectDto();
        prj.setName("Translate IT 2");
        prj.setFormat(LanguageFileFormat.PROPERTIES);
        prj.setType(LanguageFileType.ISO8859_1);
        prj.setId(projectId);

        // and Work
        long workId = 1L;
        work = new WorkDto();
        work.setProjectId(prj.getId());
        work.setId(workId);

        // WHEN expect ISO8859
        when(projectService.getWorkDtoById(1L)).thenReturn(work);
        when(projectService.getProjectDtoById(666L)).thenReturn(prj);
        ;

        // THEN throw exception if the upload file is UTF-8
        Path uploadedLngFile = Paths.get("d:\\messages_fi-UTF8.properties");
        try {
            iso8859validator.checkFileCharSet(uploadedLngFile, 1L);
            fail("No exception thrown");
        } catch (StorageException e) {
            assertThat(e.getMessage().contains(messages.getPart("FileStorageService.false_ISO8859_encoding"))
            // It should be ISO8859
                    , is(equalTo(true)));
        }

        // WHEN expect UTF-8
        prj = new ProjectDto();
        prj.setName("Translate IT 2");
        prj.setFormat(LanguageFileFormat.PROPERTIES);
        prj.setType(LanguageFileType.UTF_8);
        prj.setId(projectId);

        // and Work
        workId = 2L;
        work = new WorkDto();
        work.setProjectId(prj.getId());
        work.setId(workId);

        when(projectService.getWorkDtoById(2L)).thenReturn(work);
        when(projectService.getProjectDtoById(666L)).thenReturn(prj);
        ;
        // THEN throw exception if the upload file is ISO8859
        uploadedLngFile = Paths.get("d:\\messages_fi.properties");
        try {
            iso8859validator.checkFileCharSet(uploadedLngFile, 2L);
            fail("No exception thrown");
        } catch (StorageException e) {
            assertThat(e.getMessage().contains(messages.getPart("FileStorageService.false_UTF8_encoding"))
            // It should be UTF-8
                    , is(equalTo(true)));
        }

        // WHEN expect UTF-8
        prj = new ProjectDto();
        prj.setName("Translate IT 2");
        prj.setFormat(LanguageFileFormat.PROPERTIES);
        prj.setType(LanguageFileType.UTF_8);
        prj.setId(projectId);

        // and Work
        workId = 3L;
        work = new WorkDto();
        work.setProjectId(prj.getId());
        work.setId(workId);

        when(projectService.getWorkDtoById(3L)).thenReturn(work);
        when(projectService.getProjectDtoById(666L)).thenReturn(prj);

        // THEN don't throw any exception if the upload file is UTF-8
        uploadedLngFile = Paths.get("d:\\messages_fi-UTF8.properties");
        try {
            iso8859validator.checkFileCharSet(uploadedLngFile, 3L);
        } catch (StorageException e) {
            fail("An exception was thrown");
        }

        // WHEN expect ISO8859
        prj = new ProjectDto();
        prj.setName("Translate IT 2");
        prj.setFormat(LanguageFileFormat.PROPERTIES);
        prj.setType(LanguageFileType.ISO8859_1);
        prj.setId(projectId);

        // and Work
        workId = 3L;
        work = new WorkDto();
        work.setProjectId(prj.getId());
        work.setId(workId);

        when(projectService.getWorkDtoById(4L)).thenReturn(work);
        when(projectService.getProjectDtoById(666L)).thenReturn(prj);

        // THEN don't throw any exception if the upload file is ISO8859
        uploadedLngFile = Paths.get("d:\\messages_fi.properties");
        try {
            iso8859validator.checkFileCharSet(uploadedLngFile, 4L);
        } catch (StorageException e) {
            fail("An exception was thrown");
        }
    }
}
