package translateit2.fileloader;

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
public class LanguageFileLoaderIntegrationTest {
    private LanguageFileLoaderService storage;

    @Autowired
    public void setLngFileServiceFactory(LanguageFileLoaderService storage) {
        this.storage = storage;
    }

    @Test
    public void setUniQueFilePAht() {
        Path p = storage.getUniquePath(".properties");
        p = storage.getUniquePath(null);
    }
}
