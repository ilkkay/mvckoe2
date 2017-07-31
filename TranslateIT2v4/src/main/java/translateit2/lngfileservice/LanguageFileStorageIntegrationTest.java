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
import translateit2.languagefileservice.factory.LanguageFileServiceFactory;
import translateit2.languagefileservice.factory.LanguageFileServiceFactoryImpl;

@RunWith(SpringRunner.class)
@SpringBootTest(classes = TranslateIt2v4Application.class)
@WebAppConfiguration
public class LanguageFileStorageIntegrationTest {

    private LanguageFileServiceFactory languageFileServiceFactory;

    @Autowired
    public void setLngFileServiceFactory(LanguageFileServiceFactoryImpl lngFileServiceFactory) {
        this.languageFileServiceFactory = lngFileServiceFactory;
    }

    @Test
    public void getFactoryService_cached() {
        LanguageFileStorage service = null;

        service = languageFileServiceFactory.getService(LanguageFileFormat.XLIFF).get();
        assertThat(service.getFileFormat(), is(equalTo(LanguageFileFormat.XLIFF)));

        service = languageFileServiceFactory.getService(LanguageFileFormat.PROPERTIES).get();
        assertThat(service.getFileFormat(), is(equalTo(LanguageFileFormat.PROPERTIES)));

        service = languageFileServiceFactory.getService(LanguageFileFormat.DEFAULT).get();
        assertThat(service.getFileFormat(), is(equalTo(LanguageFileFormat.DEFAULT)));

        try {
            service = languageFileServiceFactory.getService(LanguageFileFormat.PO).get();
            fail("No exception was thrown");
        } catch (Exception e) {
            assertThat(e).hasMessageContaining("No value present");
        }

        Path p = service.getPath("dotcms_en.properties");
        assertThat(p.getParent().toString(), is(equalTo("upload-dir3")));

        languageFileServiceFactory.listFormatsSupported().stream().forEach(System.out::println);
    }

    // https://blog.goyello.com/2015/10/01/different-ways-of-testing-exceptions-in-java-and-junit/
    @Test
    public void failToUpLoad_ifFormatNotSupported() {

        try {
            languageFileServiceFactory.getService(LanguageFileFormat.PO).get();
            fail("No exception was thrown");
        } catch (Exception e) {
            assertThat(e).isInstanceOf(RuntimeException.class);
        }

    }

}
