package translateit2.languagefileservice;

import static org.hamcrest.CoreMatchers.equalTo;
import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.assertThat;
import static org.junit.Assert.fail;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.time.LocalDate;
import java.util.List;
import java.util.Locale;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.context.web.WebAppConfiguration;
import org.springframework.web.multipart.MultipartFile;

import translateit2.TranslateIt2v4Application;
import translateit2.lngfileservice.LanguageFileFormat;
import translateit2.lngfileservice.LanguageFileType;
import translateit2.lngfileservice.iso8859.Iso8859Storage;
import translateit2.lngfileservice.iso8859.Iso8859StorageImpl;
import translateit2.persistence.dto.InfoDto;
import translateit2.persistence.dto.PersonDto;
import translateit2.persistence.dto.ProjectDto;
import translateit2.persistence.dto.TranslatorGroupDto;
import translateit2.persistence.dto.WorkDto;
import translateit2.persistence.model.Priority;
import translateit2.persistence.model.Status;
import translateit2.service.ProjectService;
import translateit2.service.WorkService;

@RunWith(SpringRunner.class)
@SpringBootTest(classes = TranslateIt2v4Application.class)
@WebAppConfiguration
public class Iso8859StorageIntegrationTest {
    private Iso8859Storage iso8859Storage;

    @Autowired
    private ProjectService projectService;

    @Autowired
    private WorkService workService;

    @Test
    public void downloadTargetLngFileToDataBase_assertFileExists() throws IOException {
        // given loaded source file
        ProjectDto prj = projectService.getProjectDtoByProjectName("Translate IT 111");
        List<WorkDto> works = workService.listProjectWorkDtos(prj.getId());
        WorkDto work = works.get(0);
        Path uploadedLngFile = Paths.get("upload-dir3/dotcms_en.properties");
        iso8859Storage.uploadSourceToDb(uploadedLngFile, work.getId());

        // and uploaded target file
        uploadedLngFile = Paths.get("upload-dir3/dotcms_fi-utf8.properties");
        iso8859Storage.uploadTargetToDb(uploadedLngFile, work.getId());

        // download file
        Path dstDir = Paths.get("upload-dir3");
        Path downloadedPath = null;
        try {
            downloadedPath = iso8859Storage.downloadTargetLngFile(dstDir, work.getId());
        } catch (IOException e) {
            fail("IO Exception: " + e.getMessage());
        }
        assertThat(Files.exists(downloadedPath), is(equalTo(true)));

    }

    @Autowired
    public void setLngFileServiceFactory(Iso8859StorageImpl iso8859Storage) {
        this.iso8859Storage = iso8859Storage;
    }

    @Before
    public void setup() {
        // create Project
        PersonDto personDto = new PersonDto();
        personDto.setFullName("James Bond");
        personDto = projectService.createPersonDto(personDto);

        InfoDto infoDto = new InfoDto();
        infoDto.setText("This is info");
        infoDto = projectService.createInfoDto(infoDto);

        TranslatorGroupDto groupDto = new TranslatorGroupDto();
        groupDto = projectService.createGroupDto(groupDto);

        ProjectDto prj = new ProjectDto();
        prj.setName("Translate IT 111");
        prj.setSourceLocale(new Locale("en_EN"));
        prj.setFormat(LanguageFileFormat.PROPERTIES);
        prj.setType(LanguageFileType.UTF_8);
        prj.setPersonId(personDto.getId());
        prj.setInfoId(infoDto.getId());
        prj = projectService.createProjectDto(prj);

        // create Work
        WorkDto work = new WorkDto();
        work.setProjectId(prj.getId());
        work.setGroupId(666L);
        work.setLocale(new Locale("fi_FI"));
        work.setVersion("0.07");
        work.setOriginalFile("dotcms");
        work.setSkeletonFile("skeleton_file");
        work.setStatus(Status.NEW);
        work.setPriority(Priority.HIGH);
        work.setStarted(LocalDate.now());
        LocalDate currentDate = LocalDate.now();
        LocalDate deadLine = currentDate.plusMonths(2L);
        deadLine = deadLine.plusDays(5L);
        work.setDeadLine(deadLine);
        work.setProgress(0.666);
        work.setGroupId(groupDto.getId());
        work = workService.createWorkDto(work);
    }

    // @Test
    public void storeUploadedMultiPartFile_assertExists() throws IOException {
        Path propPath = Paths.get("upload-dir3/dotcms_en.properties");
        byte[] bytes = Files.readAllBytes(propPath);
        MultipartFile multipartFile = new MockMultipartFile("file", "uploaded.properties", "text/plain", bytes);

        Path uploadedPath = Paths.get("upload-dir3/uploaded.properties");
        if (Files.exists(uploadedPath))
            Files.delete(uploadedPath);

        iso8859Storage.storeFile(multipartFile);
        assertThat(Files.exists(uploadedPath), is(equalTo(true)));
    }

    // @Test
    public void uploadSourceLngFileToDataBase_assertUnitCount() throws IOException {
        // given
        ProjectDto prj = projectService.getProjectDtoByProjectName("Translate IT 111");
        List<WorkDto> works = workService.listProjectWorkDtos(prj.getId());
        WorkDto work = works.get(0);
        // upload file
        Path uploadedLngFile = Paths.get("upload-dir3/dotcms_en.properties");
        iso8859Storage.uploadSourceToDb(uploadedLngFile, work.getId());

        long receivedCount = workService.getUnitDtoCount(work.getId());
        assertThat(receivedCount, is(equalTo(4140L)));
    }

    // @Test
    public void uploadTargetLngFileToDataBase_assertUnitCount() throws IOException {
        // given loaded source file
        ProjectDto prj = projectService.getProjectDtoByProjectName("Translate IT 111");
        List<WorkDto> works = workService.listProjectWorkDtos(prj.getId());
        WorkDto work = works.get(0);
        Path uploadedLngFile = Paths.get("upload-dir3/dotcms_en.properties");
        iso8859Storage.uploadSourceToDb(uploadedLngFile, work.getId());

        // upload target file
        uploadedLngFile = Paths.get("upload-dir3/dotcms_fi-utf8.properties");
        iso8859Storage.uploadTargetToDb(uploadedLngFile, work.getId());
    }
}
