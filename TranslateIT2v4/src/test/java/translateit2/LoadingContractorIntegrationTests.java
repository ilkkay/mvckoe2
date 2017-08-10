package translateit2;

import static org.junit.Assert.*;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.web.multipart.MultipartFile;

import translateit2.fileloader.FileLoaderException;
import translateit2.service.LoadingContractor;
import translateit2.service.ProjectService;

import org.springframework.mock.web.MockMultipartFile;
import org.springframework.test.context.junit4.SpringRunner;

import org.apache.commons.io.IOUtils;;

@RunWith(SpringRunner.class)
@SpringBootTest(classes = TranslateIt2v4Application.class)
public class LoadingContractorIntegrationTests {
    static final Logger logger = LogManager.getLogger(ProjectServiceIntegrationTest.class.getName());

    @Autowired
    private LoadingContractor loadingContractor;
    
    @BeforeClass
    public static void setUpBeforeClass() throws Exception {
    }

    @AfterClass
    public static void tearDownAfterClass() throws Exception {
    }

    @Before
    public void setUp() throws Exception {
    }

    @After
    public void tearDown() throws Exception {
    }

    @Test
    public void test() throws IOException {
            long workId=1;
            
            File file = new File("d:\\dotcms_fi-utf8.properties");
            FileInputStream input = new FileInputStream(file);
            MultipartFile multiPartFile = new MockMultipartFile("file",
                    file.getName(), "text/plain", IOUtils.toByteArray(input));
            
            loadingContractor.uploadSource(multiPartFile, workId);

    }

}
