package translateit2.languagefactory;

import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.CoreMatchers.equalTo;
import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.assertThat;
import static org.junit.Assert.fail;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.context.web.WebAppConfiguration;

import translateit2.TranslateIt2v4Application;
import translateit2.lngfileservice.LanguageFileFormat;

@RunWith(SpringRunner.class)
@SpringBootTest(classes = TranslateIt2v4Application.class)
@WebAppConfiguration
public class LanguageFileStorageIntegrationTest {
    /*
    @Autowired
    private LanguageFileFactory<LanguageFileWriter, LanguageFileFormat> fileWriterFactory;

    @Autowired
    private LanguageFileFactory<LanguageFileReader, LanguageFileFormat> fileReaderFactory;

    @Autowired
    private LanguageFileFactory<LanguageFileValidator, LanguageFileFormat> fileValidatorFactory;
    */
    
    @Autowired
    private LanguageBeanCache<LanguageFileReader, LanguageFileFormat> fileReaderCache;

    @Autowired
    private LanguageBeanCache<LanguageFileWriter, LanguageFileFormat> fileWriterCache;
    
    @Autowired
    private LanguageBeanCache<LanguageFileValidator, LanguageFileFormat> fileValidatorCache;

    // https://blog.goyello.com/2015/10/01/different-ways-of-testing-exceptions-in-java-and-junit/
    @Test
    public void failToGetService_ifFormatNotSupported() {

        try {
            fileReaderCache.getService(LanguageFileFormat.PO).get();
            fail("No exception was thrown");
        } catch (Exception e) {
            assertThat(e).isInstanceOf(RuntimeException.class);
        }

    }

    /*
    @Test
    public void testFactoryImpl() {
        LanguageFileReader reader = fileReaderFactory.getService(LanguageFileFormat.DEFAULT).get();
        assertThat(reader.getFileFormat(), is(equalTo(LanguageFileFormat.DEFAULT)));
        // reader.read();

        LanguageFileWriter writer = fileWriterFactory.getService(LanguageFileFormat.DEFAULT).get();
        assertThat(writer.getFileFormat(), is(equalTo(LanguageFileFormat.DEFAULT)));
        // writer.write();

        LanguageFileValidator validator = fileValidatorFactory.getService(LanguageFileFormat.DEFAULT).get();
        assertThat(validator.getFileFormat(), is(equalTo(LanguageFileFormat.DEFAULT)));
        // validator.validate();

    }
    */
    
    @Test
    public void testBeanCache() {
        LanguageFileWriter writerBean = fileWriterCache.getService(LanguageFileFormat.DEFAULT).get();
        assertThat(writerBean.getFileFormat(), is(equalTo(LanguageFileFormat.DEFAULT)));

        LanguageFileReader readerBean = fileReaderCache.getService(LanguageFileFormat.DEFAULT).get();
        assertThat(readerBean.getFileFormat(), is(equalTo(LanguageFileFormat.DEFAULT)));
        
        LanguageFileValidator validatorBean = fileValidatorCache.getService(LanguageFileFormat.DEFAULT).get();
        assertThat(validatorBean.getFileFormat(), is(equalTo(LanguageFileFormat.DEFAULT)));
    }

}
