package translateit2;

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
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

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

import translateit2.fileloader.FileLoader;
import translateit2.fileloader.FileLoaderException;
import translateit2.persistence.dao.FileInfoRepository;
import translateit2.persistence.dao.UnitRepository;
import translateit2.persistence.dao.WorkRepository;
import translateit2.persistence.model.FileInfo;
import translateit2.persistence.model.Unit;
import translateit2.service.LoadingContractor;
import translateit2.service.ProjectService;

import org.springframework.mock.web.MockMultipartFile;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.util.FileSystemUtils;
import org.apache.commons.io.IOUtils;;

@RunWith(SpringRunner.class)
@SpringBootTest(classes = TranslateIt2v4Application.class)
public class LoadingContractorIntegrationTests {
    static final Logger logger = LogManager.getLogger(ProjectServiceIntegrationTest.class.getName());

    @Autowired
    private FileInfoRepository fileInfoRepo;

    @Autowired
    private FileLoader fileloader;

    @Autowired
    private WorkRepository workRepo;

    @Autowired
    private UnitRepository unitRepo;

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
    public void uploadSourceFile_assertFileInfoHasOriginalFilename_and_BackupDirectory() throws IOException {
        long workId=1;

        // WHEN multipart file exists
        File file = new File("d:\\dotcms_en-utf8.properties");
        FileInputStream input = new FileInputStream(file);
        MultipartFile multiPartFile = new MockMultipartFile("file",
                file.getName(), "text/plain", IOUtils.toByteArray(input));

        // THEN load to permanent system and to database
        try {
            loadingContractor.uploadSource(multiPartFile, workId);
        }
        catch (Exception ex) {
            fail("Unexcepted exception");
        }

        // assert fileinfo contains orginal filename and backup directory
        FileInfo info = fileInfoRepo.findByWorkId(workId).get();

        String dateStr = Paths.get(info.getBackup_file()).getParent().getFileName().toString();
        String dateNowStr = LocalDate.now().toString();

        assertThat(dateStr, equalTo(dateNowStr));        
        assertThat("dotcms_en-utf8.properties",equalTo(info.getOriginal_file()));

        // remove file from disk and units from database
        FileSystemUtils.deleteRecursively(Paths.get(info.getBackup_file()).getParent().toFile());
        fileInfoRepo.delete(info);
        List<Unit> units = unitRepo.findAll().stream().filter(unit -> workId == unit.getWork().getId())
                .collect(Collectors.toList());
        unitRepo.delete(units);        

        return;
    }

    @Test
    public void reloadingSourceFile_assertCannotUploadException() throws IOException {
        long workId=1;

        // initialize multipart file 
        File file = new File("d:\\dotcms_en-utf8.properties");
        FileInputStream input = new FileInputStream(file);
        MultipartFile multiPartFile = new MockMultipartFile("file",
                file.getName(), "text/plain", IOUtils.toByteArray(input));

        // and WHEN we have loaded the file
        try {
            loadingContractor.uploadSource(multiPartFile, workId);
        }
        catch (Exception ex) {
            fail("Unexcepted exception");
        }

        // THEN reload to the work ID
        // and ASSERT FileLoader exception
        assertThatCode(() -> { loadingContractor.uploadSource(multiPartFile, workId); } )
        .isExactlyInstanceOf(FileLoaderException.class);

        // remove file from disk, units and file info from database
        FileInfo info = fileInfoRepo.findByWorkId(workId).get();
        fileInfoRepo.delete(info);

        FileSystemUtils.deleteRecursively(Paths.get(info.getBackup_file()).getParent().toFile());

        List<Unit> units = unitRepo.findAll().stream().filter(unit -> workId == unit.getWork().getId())
                .collect(Collectors.toList());
        unitRepo.delete(units);
    }

    @Test
    public void uploadTargetFile_assertUnitTargetTextLength() throws IOException {
        long workId=1;


        // WHEN loaded to source file
        try {
            File fileSource = new File("d:\\dotcms_en-utf8.properties");
            FileInputStream input1 = new FileInputStream(fileSource);
            MultipartFile multiPartFile = new MockMultipartFile("file1",
                    fileSource.getName(), "text/plain", IOUtils.toByteArray(input1));
            loadingContractor.uploadSource(multiPartFile, workId);
        }
        catch (Exception ex) {
            fail("Unexcepted exception");
        }


        // WHEN load to target file
        try {
            File fileTarget = new File("d:\\dotcms_fi-utf8.properties");
            FileInputStream input2 = new FileInputStream(fileTarget);
            MultipartFile multiPartFile = new MockMultipartFile("file2",
                    fileTarget.getName(), "text/plain", IOUtils.toByteArray(input2));
            loadingContractor.uploadTarget(multiPartFile, workId);
        }
        catch (Exception ex) {
            fail("Unexcepted exception");
        }

        List<Unit> units = unitRepo.findAll().stream().filter(unit -> workId == unit.getWork().getId())
                .collect(Collectors.toList());

        Unit unit_1 = units.get(0);
        assert(unit_1.getTarget().getText().length() > 0);

        // remove file from disk and units from database
        FileInfo info = fileInfoRepo.findByWorkId(workId).get();
        fileInfoRepo.delete(info);

        FileSystemUtils.deleteRecursively(Paths.get(info.getBackup_file()).getParent().toFile());

        unitRepo.delete(units);

        return;
    }

    @Test
    public void downloadTarget_assertDownloadDirectoryName() {
        long workId = 1;

        // WHEN source and target files have been uploaded
        try {
            File fileSource = new File("d:\\dotcms_en-utf8.properties");
            FileInputStream input1 = new FileInputStream(fileSource);
            MultipartFile multiPartFile1 = new MockMultipartFile("file1",
                    fileSource.getName(), "text/plain", IOUtils.toByteArray(input1));
            loadingContractor.uploadSource(multiPartFile1, workId);

            File fileTarget = new File("d:\\dotcms_fi-utf8.properties");
            FileInputStream input2 = new FileInputStream(fileTarget);
            MultipartFile multiPartFile2 = new MockMultipartFile("file2",
                    fileTarget.getName(), "text/plain", IOUtils.toByteArray(input2));
            loadingContractor.uploadTarget(multiPartFile2, workId);
        }
        catch (Exception ex) {
            fail("Unexcepted exception");
        }

        // THEN download Target file as stream
        List<Stream<Path>> paths = new ArrayList<Stream<Path>>();        
        assertThatCode(() -> { paths.add(loadingContractor.downloadTarget(workId)); } )
        .doesNotThrowAnyException(); 

        // assert stream path count
        Stream<Path> streamPath = paths.get(0);        
        List<Path> streamPaths = streamPath.map(path -> path.toAbsolutePath()).collect(Collectors.toList());        
        assertThat(streamPaths.size(), equalTo(1));

        // assert download directory name
        String expectedDownloadDir = fileloader.getDownloadPath("test.txt").getParent().getFileName().toString();
        String returnedDownloadDir = streamPaths.get(0).getParent().getFileName().toString();
        assertThat(expectedDownloadDir,equalTo(returnedDownloadDir));

        // remove file from disk and units from database
        FileInfo info = fileInfoRepo.findByWorkId(workId).get();
        fileInfoRepo.delete(info);

        FileSystemUtils.deleteRecursively(Paths.get(info.getBackup_file()).getParent().toFile());

        List<Unit> units = unitRepo.findAll().stream().filter(unit -> workId == unit.getWork().getId())
                .collect(Collectors.toList());
        unitRepo.delete(units);
    }
    
    @Test   
    public void removeUploadedSource_assertFileInfo_and_File_Removed()  {
        long workId=1;

        // and WHEN we have loaded the multipart file
        try {
            File file = new File("d:\\dotcms_en-utf8.properties");
            FileInputStream input = new FileInputStream(file);
            MultipartFile multiPartFile = new MockMultipartFile("file",
                    file.getName(), "text/plain", IOUtils.toByteArray(input));
            loadingContractor.uploadSource(multiPartFile, workId);
        }
        catch (Exception ex) {
            fail("Unexcepted exception");
        }

        FileInfo info = fileInfoRepo.findByWorkId(workId).get();
        String backupFile = info.getBackup_file();
        
        // THEN we remove it
        assertThatCode(() -> { loadingContractor.removeUploadedSource(workId); } )
        .doesNotThrowAnyException(); 
        
        // assert that entity exists more
        assertThat(fileInfoRepo.findByWorkId(workId).isPresent(),equalTo(false));
        
        // and neither does file
        assertThat(Files.exists(Paths.get(backupFile)),equalTo(false));
        
        // remove units from database
        FileSystemUtils.deleteRecursively(Paths.get(info.getBackup_file()).getParent().toFile());

        List<Unit> units = unitRepo.findAll().stream().filter(unit -> workId == unit.getWork().getId())
                .collect(Collectors.toList());
        unitRepo.delete(units);
    }
}
