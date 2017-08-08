package translateit2;

import static org.hamcrest.CoreMatchers.equalTo;
import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertThat;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.context.junit4.SpringRunner;

import translateit2.lngfileservice.LanguageFileFormat;
import translateit2.lngfileservice.LanguageFileType;
import translateit2.persistence.dto.FileInfoDto;
import translateit2.persistence.dto.InfoDto;
import translateit2.persistence.dto.PersonDto;
import translateit2.persistence.dto.ProjectDto;
import translateit2.persistence.dto.TranslatorGroupDto;
import translateit2.persistence.dto.UnitDto;
import translateit2.persistence.dto.WorkDto;
import translateit2.persistence.model.Priority;
import translateit2.persistence.model.Source;
import translateit2.persistence.model.Status;
import translateit2.persistence.model.Target;
import translateit2.service.ProjectService;
import translateit2.service.WorkService;
import translateit2.util.Messages;

//https://springframework.guru/integration-testing-with-spring-and-junit/
//https://springframework.guru/mockito-mock-vs-spy-in-spring-boot-tests/
//https://dzone.com/articles/mockito-mock-vs-spy-in-spring-boot-tests
//https://www.tutorialspoint.com/design_pattern/null_object_pattern.htm
// http://www.journaldev.com/2668/spring-validation-example-mvc-validator
// https://www.petrikainulainen.net/programming/spring-framework/spring-from-the-trenches-adding-validation-to-a-rest-api/

@ConfigurationProperties(prefix = "test.translateit2")
@TestPropertySource("test.properties")
// @TestPropertySource(properties = {"ProjectNameMaxSize =
// 666","ProjectNameMinSize = 3"})
@RunWith(SpringRunner.class)
@SpringBootTest(classes = TranslateIt2v4Application.class)
public class ProjectServiceIntegrationTest {
    static final Logger logger = LogManager.getLogger(ProjectServiceIntegrationTest.class.getName());

    @Autowired
    private ProjectService projectService;

    @Autowired
    private WorkService workService;

    @Autowired
    Messages messages;

    @Test
    public void createUnit_assertUnitCount_returnEmptyDB() {

        PersonDto personDto = new PersonDto();
        personDto.setFullName("James Bond");
        personDto = projectService.createPersonDto(personDto);

        InfoDto infoDto = new InfoDto();
        infoDto.setText("This is info");
        infoDto = projectService.createInfoDto(infoDto);

        TranslatorGroupDto groupDto = new TranslatorGroupDto();
        groupDto = projectService.createGroupDto(groupDto);

        long startCount = projectService.getProjectDtoCount();

        ProjectDto prj = new ProjectDto();
        prj.setName("Translate IT 333");
        prj.setSourceLocale(new Locale("fi_FI"));
        prj.setFormat(LanguageFileFormat.PROPERTIES);
        prj.setType(LanguageFileType.UTF_8);
        prj.setPersonId(personDto.getId());
        prj.setInfoId(infoDto.getId());
        prj = projectService.createProjectDto(prj);

        ProjectDto prj1 = projectService.getProjectDtoById(prj.getId());
        assertEquals("Translate IT 333", prj.getName());
        ProjectDto prj2 = projectService.getProjectDtoByProjectName("Translate IT 333");
        assertEquals("Translate IT 333", prj2.getName());

        prj.setName("Translate IT 4");
        prj = projectService.updateProjectDto(prj);
        prj = projectService.getProjectDtoById(prj.getId());
        assertEquals("Translate IT 4", prj.getName());

        prj = new ProjectDto();
        prj.setName("Translate IT 5");
        prj.setPersonId(personDto.getId());
        prj.setInfoId(infoDto.getId());
        prj.setFormat(LanguageFileFormat.PROPERTIES);
        prj.setType(LanguageFileType.UTF_8);
        prj.setSourceLocale(new Locale("en_EN"));
        prj = projectService.createProjectDto(prj);

        long curCount = projectService.getProjectDtoCount();
        assertThat(curCount, is(equalTo(startCount + 2)));

        // create empty file info for work entity
        FileInfoDto fileInfoDto = new FileInfoDto();
        fileInfoDto = projectService.createFileInfoDto(fileInfoDto);

        WorkDto work = new WorkDto();
        work.setProjectId(prj.getId());
        work.setGroupId(666L);
        work.setFileInfoId(fileInfoDto.getId());
        work.setLocale(new Locale("en_EN"));
        work.setVersion("0.07");
        work.setOriginalFile("dotcms");
        work.setSkeletonFile("skeleton file");
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

        /*
         * initializng finished
         */

        // given
        final UnitDto unit = new UnitDto();
        unit.setSegmentKey("segmentKey");
        unit.setSerialNumber(666);
        final Source s = new Source();
        s.setText("source text");
        final Target t = new Target();
        t.setText("target text");
        unit.setSource(s);
        unit.setTarget(t);

        List<UnitDto> unitDtos = new ArrayList<UnitDto>();
        unitDtos.add(unit);

        // when
        workService.createUnitDtos(unitDtos, work.getId());

        // TODO: then
        long unitCount = workService.getUnitDtoCount(work.getId());
        assertThat(1L, is(equalTo(unitCount)));
        unitDtos.clear();

        // giwen
        List<UnitDto> newUnitDtos = workService.listUnitDtos(work.getId());
        newUnitDtos.forEach(dto -> dto.setSegmentKey("new " + dto.getSegmentKey()));
        newUnitDtos.forEach(dto -> dto.getSource().setText("new " + dto.getSource().getText()));

        // TODO: this will throw exception
        newUnitDtos.forEach(dto -> dto.getTarget().setText("new " + dto.getTarget().getText()));

        // when
        workService.updateUnitDtos(newUnitDtos, work.getId());

        // TODO: then
        assertThat(1, is(equalTo(1)));
        newUnitDtos.clear();

        int pageIndex = 0;
        int pageSize = 10;
        List<UnitDto> unitPage = workService.getPage(work.getId(), pageIndex, pageSize);
        assertThat(1, is(equalTo(unitPage.size())));

        // when remove units
        workService.removeUnitDtos(work.getId());

        // then no units more for this work
        unitCount = workService.getUnitDtoCount(work.getId());
        assertThat(0L, is(equalTo(unitCount)));

        // remove all
        projectService.removeProjectDtos(projectService.listAllProjectDtos());
        curCount = projectService.getProjectDtoCount();
        assertThat(curCount, is(equalTo(0L)));
    }

    @Test
    public void createUpdateAddProjectRemoveAll_returnEmptyDB() {
        /* from different service */
        PersonDto personDto = new PersonDto();
        personDto.setFullName("James Bond");
        personDto = projectService.createPersonDto(personDto);

        InfoDto infoDto = new InfoDto();
        infoDto.setText("This is info");
        infoDto = projectService.createInfoDto(infoDto);

        long startCount = projectService.getProjectDtoCount();

        ProjectDto prj = new ProjectDto();
        prj.setName("Translate IT 22");
        prj.setSourceLocale(new Locale("fi_FI"));
        prj.setFormat(LanguageFileFormat.PROPERTIES);
        prj.setType(LanguageFileType.UTF_8);
        prj.setPersonId(personDto.getId());
        prj.setInfoId(infoDto.getId());
        prj = projectService.createProjectDto(prj);

        ProjectDto prj1 = projectService.getProjectDtoById(prj.getId());
        assertEquals("Translate IT 22", prj.getName());
        ProjectDto prj2 = projectService.getProjectDtoByProjectName("Translate IT 22");
        assertEquals("Translate IT 22", prj2.getName());

        prj.setName("Translate IT 4");
        prj = projectService.updateProjectDto(prj);
        prj = projectService.getProjectDtoById(prj.getId());
        assertEquals("Translate IT 4", prj.getName());

        prj = new ProjectDto();
        prj.setName("Translate IT 5");
        prj.setPersonId(personDto.getId());
        prj.setInfoId(infoDto.getId());
        prj.setSourceLocale(new Locale("fi_FI"));
        prj.setFormat(LanguageFileFormat.PROPERTIES);
        prj.setType(LanguageFileType.UTF_8);
        prj = projectService.createProjectDto(prj);

        long curCount = projectService.getProjectDtoCount();
        assertThat(curCount, is(equalTo(startCount + 2)));

        // remove one
        projectService.removeProjectDto(prj.getId());
        curCount = projectService.getProjectDtoCount();
        assertThat(curCount, is(equalTo(startCount + 1)));

        // TODO test remove all for a person
        long personId = prj.getPersonId();
        List<ProjectDto> personPrjs = projectService.listProjectDtos(personId);
        projectService.removeProjectDtos(personPrjs);
        curCount = projectService.getProjectDtoCountByPerson(personId);
        assertThat(curCount, is(equalTo(0L)));

        // remove all
        projectService.removeProjectDtos(projectService.listAllProjectDtos());
        curCount = projectService.getProjectDtoCount();
        assertThat(curCount, is(equalTo(0L)));
    }

    @Test
    public void createWork_assertProjectIdWorkId_returnEmptyDB() {
        PersonDto personDto = new PersonDto();
        personDto.setFullName("James Bond");
        personDto = projectService.createPersonDto(personDto);

        InfoDto infoDto = new InfoDto();
        infoDto.setText("This is info");
        infoDto = projectService.createInfoDto(infoDto);

        TranslatorGroupDto groupDto = new TranslatorGroupDto();
        groupDto = projectService.createGroupDto(groupDto);

        long startCount = projectService.getProjectDtoCount();

        ProjectDto prj = new ProjectDto();
        prj.setName("Translate IT 22");
        prj.setSourceLocale(new Locale("fi_FI"));
        prj.setFormat(LanguageFileFormat.PROPERTIES);
        prj.setType(LanguageFileType.UTF_8);
        prj.setPersonId(personDto.getId());
        prj.setInfoId(infoDto.getId());
        prj = projectService.createProjectDto(prj);

        ProjectDto prj1 = projectService.getProjectDtoById(prj.getId());
        assertEquals("Translate IT 22", prj.getName());
        ProjectDto prj2 = projectService.getProjectDtoByProjectName("Translate IT 22");
        assertEquals("Translate IT 22", prj2.getName());

        prj.setName("Translate IT 4");
        prj = projectService.updateProjectDto(prj);
        prj = projectService.getProjectDtoById(prj.getId());
        assertEquals("Translate IT 4", prj.getName());

        prj = new ProjectDto();
        prj.setName("Translate IT 5");
        prj.setPersonId(personDto.getId());
        prj.setInfoId(infoDto.getId());
        prj.setSourceLocale(new Locale("fi_FI"));
        prj.setFormat(LanguageFileFormat.PROPERTIES);
        prj.setType(LanguageFileType.UTF_8);
        prj = projectService.createProjectDto(prj);

        long curCount = projectService.getProjectDtoCount();
        assertThat(curCount, is(equalTo(startCount + 2)));

        /*
         * initializng finished
         */

        // create empty file info for work entity
        FileInfoDto fileInfoDto = new FileInfoDto();
        fileInfoDto = projectService.createFileInfoDto(fileInfoDto);

        WorkDto work = new WorkDto();
        work.setProjectId(prj.getId());
        work.setGroupId(666L);
        work.setFileInfoId(fileInfoDto.getId());
        work.setLocale(new Locale("en_EN"));
        work.setVersion("0.07");
        work.setOriginalFile("dotcms");
        work.setSkeletonFile("skeleton file");
        work.setStatus(Status.NEW);
        work.setPriority(Priority.HIGH);
        work.setStarted(LocalDate.now());
        work.setDeadLine(LocalDate.parse("2017-10-10"));

        work.setProgress(0.666);
        work.setGroupId(groupDto.getId());
        work = workService.createWorkDto(work);

        WorkDto wrk1 = workService.getWorkDtoById(work.getId());
        assertEquals("dotcms", wrk1.getOriginalFile());

        work = workService.updateWorkDto(work);
        LocalDate expected = LocalDate.parse("2017-10-10");
        wrk1 = workService.getWorkDtoById(work.getId());
        assertThat(expected, is(equalTo(wrk1.getDeadLine())));
        assertThat(1L, is(equalTo(workService.getWorkDtoCount(wrk1.getGroupId()))));

        List<WorkDto> works = workService.listWorkDtos(wrk1.getGroupId());
        assertThat(1, is(equalTo(works.size())));

        // remove created work and remove all works of its parent project
        projectService.removeProjectDto(wrk1.getProjectId());
        // get count of all projects
        assertThat(1L, is(equalTo(projectService.getProjectDtoCount())));
        assertThat(0L, is(equalTo(workService.getWorkDtoCount(wrk1.getGroupId()))));

        // remove all
        projectService.removeProjectDtos(projectService.listAllProjectDtos());
        curCount = projectService.getProjectDtoCount();
        assertThat(curCount, is(equalTo(0L)));
    }

    @Before
    public void setup() {
        Locale.setDefault(Locale.ENGLISH); // for javax validation
        messages.setLocale(Locale.ENGLISH); // for custom validation
    }
}