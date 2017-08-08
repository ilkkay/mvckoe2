package translateit2.fileLocator;

import static org.assertj.core.api.Assertions.assertThatCode;
import static org.hamcrest.CoreMatchers.equalTo;
import static org.junit.Assert.*;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.util.ArrayList;
import java.util.List;

import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;
import org.springframework.util.FileSystemUtils;

import translateit2.fileloader.FileLoaderException;
import translateit2.filelocator.FileLocator;
import translateit2.filelocator.FileLocatorImpl;
import translateit2.lngfileservice.LanguageFileFormat;
import translateit2.lngfileservice.LanguageFileType;

public class FileLocatorUnitTests {
    List<Path> newLocation = null;

    @BeforeClass
    public static void setUpBeforeClass() throws Exception {
    }

    @AfterClass
    public static void tearDownAfterClass() throws Exception {
    }

    @Before
    public void setUp() throws Exception {
        newLocation = new ArrayList<Path> ();
    }

    @After
    public void tearDown() throws Exception {

    }

    @Test
    public void moveFile_failIfFileMissing() {
        
        // initialize
        Path uploadedPath = Paths.get("D:\\sw-tools\\STS\\translateit2testi\\TranslateIT2v4\\upload-dir4");
        Path uploadedFilePath = uploadedPath.resolve("messages_fi.properties");
        
        // WHEN upload file has no path
                
        // THEN
        assertThatCode(() -> filelocator().moveUploadedFileIntoFilesystem(
                uploadedFilePath, LanguageFileFormat.PROPERTIES))
        .isExactlyInstanceOf(FileLoaderException.class);                
    }
    
    @Test
    public void moveFile_assertFileExists() {
        
        // WHEN
        Path uploadedPath = Paths.get("D:\\sw-tools\\STS\\translateit2testi\\TranslateIT2v4\\upload-dir4");
        if (Files.notExists(uploadedPath))
            try {
                Files.createDirectory(uploadedPath);
            } catch (IOException e1) {
                fail ("Could not copy test directory.");
            }
        
        Path srcFilePath = Paths.get("D:\\messages_fi.properties");
        Path uploadedFilePath = uploadedPath.resolve("messages_fi.properties");        
        try {
            Files.copy(srcFilePath, uploadedFilePath, StandardCopyOption.REPLACE_EXISTING);
        } catch (IOException e) {
            fail ("Could not copy test file.");
        }
                
        // THEN
        List<Path> newLocation = new ArrayList<Path> ();
        assertThatCode(() -> { newLocation.add(filelocator().moveUploadedFileIntoFilesystem(
                uploadedFilePath, LanguageFileFormat.PROPERTIES)); } )
        .doesNotThrowAnyException();        
        
        // the permanent file exists
        assertThat(Files.exists(newLocation.get(0)), equalTo(true));
        
        // and the size is expected size
        try {
            int expectedSize = (int) Files.size(srcFilePath);
            assertTrue(Files.size(newLocation.get(0)) ==  expectedSize);
        } catch (IOException e) {
            fail ("Could not read file sizes.");
        }
        
        // remove the new moved file
        FileSystemUtils.deleteRecursively(newLocation.get(0).getParent().toFile());               
    }
    
    private FileLocator filelocator() {
        return new FileLocatorImpl(); 
    }
}