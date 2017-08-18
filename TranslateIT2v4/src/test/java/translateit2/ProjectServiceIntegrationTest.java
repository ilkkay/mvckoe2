package translateit2;

import static org.hamcrest.CoreMatchers.equalTo;
import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertThat;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.context.junit4.SpringRunner;

import translateit2.languagefile.LanguageFileFormat;
import translateit2.languagefile.LanguageFileType;
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

    private long testPersonId;
    
    private long testGroupId;
    
    @Autowired
    private ProjectService projectService;

    @Autowired
    private WorkService workService;

    @Autowired
    Messages messages;

    @Test
    public void createUnit_assertUnitCount_returnEmptyDB() {

        //TranslatorGroupDto groupDto = new TranslatorGroupDto();
        //groupDto = projectService.createGroupDto(groupDto);

        long startCount = projectService.getProjectDtoCount();

        ProjectDto prj = new ProjectDto();
        prj.setName("Translate IT 333");
        prj.setSourceLocale(new Locale("fi_FI"));
        prj.setFormat(LanguageFileFormat.PROPERTIES);
        prj.setType(LanguageFileType.UTF_8);
        prj = projectService.createProjectDto(prj,"James Bond");

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
        prj.setFormat(LanguageFileFormat.PROPERTIES);
        prj.setType(LanguageFileType.UTF_8);
        prj.setSourceLocale(new Locale("en_EN"));
        prj = projectService.createProjectDto(prj,"James Bond");

        long curCount = projectService.getProjectDtoCount();
        assertThat(curCount, is(equalTo(startCount + 2)));

        WorkDto work = new WorkDto();
        work.setProjectId(prj.getId());
        work.setLocale(new Locale("en_EN"));
        work.setVersion("0.073");
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
        work = workService.createWorkDto(work,"Group name 2");

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

        int pageNumber = 1;
        int pageSize = 10;
        List<UnitDto> unitPage = workService.getPage(work.getId(), pageNumber, pageSize);
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
        long startCount = projectService.getProjectDtoCount();

        ProjectDto prj = new ProjectDto();
        prj.setName("Translate IT 22");
        prj.setSourceLocale(new Locale("fi_FI"));
        prj.setFormat(LanguageFileFormat.PROPERTIES);
        prj.setType(LanguageFileType.UTF_8);

        prj = projectService.createProjectDto(prj,"James Bond");

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
        prj.setSourceLocale(new Locale("fi_FI"));
        prj.setFormat(LanguageFileFormat.PROPERTIES);
        prj.setType(LanguageFileType.UTF_8);
        
        prj = projectService.createProjectDto(prj,"James Bond");

        long curCount = projectService.getProjectDtoCount();
        assertThat(curCount, is(equalTo(startCount + 2)));

        // remove one
        projectService.removeProjectDto(prj.getId());
        curCount = projectService.getProjectDtoCount();
        assertThat(curCount, is(equalTo(startCount + 1)));

        // remove all for a person
        List<ProjectDto> personPrjs = projectService.listProjectDtos(testPersonId);
        projectService.removeProjectDtos(personPrjs);
        curCount = projectService.getProjectDtoCountByPerson(testPersonId);
        assertThat(curCount, is(equalTo(0L)));

        // assert that we same number of projects than we started
        long returnedProjectCount = projectService.getProjectDtoCount();
        assertThat(startCount, is(equalTo(returnedProjectCount)));
        
        // remove all
        projectService.removeProjectDtos(projectService.listAllProjectDtos());
        curCount = projectService.getProjectDtoCount();
        assertThat(curCount, is(equalTo(0L)));
        
    }

    @Test
    public void createWork_assertProjectId_WorkId_and_WorkCountPerProject() {

        //TranslatorGroupDto groupDto = new TranslatorGroupDto();
        //groupDto = projectService.createGroupDto(groupDto);

        long startCount = projectService.getProjectDtoCount();

        ProjectDto prj = new ProjectDto();
        prj.setName("Translate IT 22");
        prj.setSourceLocale(new Locale("fi_FI"));
        prj.setFormat(LanguageFileFormat.PROPERTIES);
        prj.setType(LanguageFileType.UTF_8);

        prj = projectService.createProjectDto(prj,"James Bond");

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
        prj.setSourceLocale(new Locale("fi_FI"));
        prj.setFormat(LanguageFileFormat.PROPERTIES);
        prj.setType(LanguageFileType.UTF_8);
        prj = projectService.createProjectDto(prj,"James Bond");

        long curCount = projectService.getProjectDtoCount();
        assertThat(curCount, is(equalTo(startCount + 2)));

        /*
         * initializng finished
         */

        WorkDto work = new WorkDto();
        work.setProjectId(prj.getId());
        work.setLocale(new Locale("en_EN"));
        work.setVersion("0.071");
        work.setOriginalFile("dotcms");
        work.setSkeletonFile("skeleton file");
        work.setStatus(Status.NEW);
        work.setPriority(Priority.HIGH);
        work.setStarted(LocalDate.now());
        work.setDeadLine(LocalDate.parse("2017-10-10"));

        work.setProgress(0.666);
        work = workService.createWorkDto(work,"Group name 2");

        WorkDto wrk1 = workService.getWorkDtoById(work.getId());
        assertEquals("dotcms", wrk1.getOriginalFile());

        work = workService.updateWorkDto(work);
        LocalDate expected = LocalDate.parse("2017-10-10");
        wrk1 = workService.getWorkDtoById(work.getId());
        assertThat(expected, is(equalTo(wrk1.getDeadLine())));
        assertThat(1L, is(equalTo(workService.getWorkDtoCount(testGroupId))));

        List<WorkDto> works = workService.listWorkDtos(testGroupId);
        assertThat(1, is(equalTo(works.size())));

        // assert the work count of the two projects
        Map<Long, Integer> workMap = projectService.getWorkCountPerProject("James Bond");
        List <ProjectDto> dtos = projectService.listProjectDtos(testPersonId);
        assertThat(workMap.get(dtos.get(0).getId()), equalTo(0));
        assertThat(workMap.get(dtos.get(1).getId()), equalTo(1));
        
        // remove created work and remove all works of its parent project
        projectService.removeProjectDto(wrk1.getProjectId());
        // get count of all projects
        assertThat(1L, is(equalTo(projectService.getProjectDtoCount())));
        assertThat(0L, is(equalTo(workService.getWorkDtoCount(testGroupId))));

        // remove all
        projectService.removeProjectDtos(projectService.listAllProjectDtos());
        curCount = projectService.getProjectDtoCount();
        assertThat(curCount, is(equalTo(0L)));
        
    }

    @Before
    public void setup() {
        Locale.setDefault(Locale.ENGLISH); // for javax validation
        messages.resetLocale(Locale.ENGLISH); // for custom validation
        
        PersonDto personDto = new PersonDto();
        personDto.setFullName("James Bond");
        personDto = projectService.createPersonDto(personDto);
        
        testPersonId = personDto.getId();

        TranslatorGroupDto groupDto = new TranslatorGroupDto();
        groupDto.setName("Group name 2");
        groupDto = projectService.createGroupDto(groupDto);
        
        testGroupId = groupDto.getId();

    }
    
    @After
    public void reset() {
        projectService.removePersonDto(testPersonId);
        
        projectService.removeGroupDto(testGroupId);
    }
}
