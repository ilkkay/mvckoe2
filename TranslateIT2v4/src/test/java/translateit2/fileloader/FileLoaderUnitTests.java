package translateit2.fileloader;

import static org.assertj.core.api.Assertions.assertThatCode;
import static org.hamcrest.CoreMatchers.equalTo;
import static org.junit.Assert.*;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;

import org.apache.commons.io.IOUtils;
import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.web.multipart.MultipartFile;

import translateit2.filelocator.FileLocator;
import translateit2.filelocator.FileLocatorImpl;
import translateit2.lngfileservice.LanguageFileFormat;

public class FileLoaderUnitTests {

    @Test
    public void storeMultipartFile_assertUploadedFile() throws IOException {
        Path filePath = Paths.get("d:\\dotcms_fi-utf8.properties");
        File file = new File("d:\\dotcms_fi-utf8.properties");
        FileInputStream input = new FileInputStream(file);
        MultipartFile multipartFile = new MockMultipartFile("file",
                file.getName(), "text/plain", IOUtils.toByteArray(input));
        
        List<Path> paths = new ArrayList<Path>();

        assertThatCode(() -> { paths.add(fileloader().storeToUploadDirectory(multipartFile)); } )
        .doesNotThrowAnyException(); 
        
        // the permanent file exists
        assertThat(Files.exists(paths.get(0)), equalTo(true));
        
        // and the size is expected size
        try {
            int expectedSize = (int) Files.size(filePath);
            assertTrue(Files.size(paths.get(0)) ==  expectedSize);
        } catch (IOException e) {
            fail ("Could not read file sizes.");
        }
    }

    
    private FileLoader fileloader() {
        FileLoaderProperties props = new FileLoaderProperties();
        props.setLocation("upload-dir4");
        return new FileLoaderImpl(props); 
    }
}
