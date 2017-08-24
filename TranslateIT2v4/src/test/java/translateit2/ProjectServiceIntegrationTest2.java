package translateit2;

import static org.assertj.core.api.Assertions.assertThatCode;
import static org.hamcrest.CoreMatchers.equalTo;
import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertThat;
import static org.junit.Assert.fail;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Iterator;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

import javax.validation.ConstraintViolation;
import javax.validation.ConstraintViolationException;
import javax.validation.Path;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.hamcrest.collection.IsIterableContainingInOrder;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.mapping.PropertyPath;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.context.junit4.SpringRunner;

import translateit2.exception.TranslateIt2Exception;
import translateit2.languagefile.LanguageFileFormat;
import translateit2.languagefile.LanguageFileType;
import translateit2.persistence.dto.PersonDto;
import translateit2.persistence.dto.ProjectDto;
import translateit2.persistence.dto.TranslatorGroupDto;
import translateit2.persistence.dto.UnitDto;
import translateit2.persistence.dto.WorkDto;
import translateit2.persistence.model.Priority;
import translateit2.persistence.model.Project;
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
@RunWith(SpringRunner.class)
@SpringBootTest(classes = TranslateIt2v4Application.class)
public class ProjectServiceIntegrationTest2 {
    static final Logger logger = LogManager.getLogger(ProjectServiceIntegrationTest2.class.getName());

    private long testPersonId;

    private long testGroupId;

    @Autowired
    private ProjectService projectService;

    @Autowired
    private WorkService workService;

    @Before
    public void setup() {

        PersonDto personDto = new PersonDto();
        personDto.setFullName("James Bond");
        personDto = projectService.createPersonDto(personDto);

        testPersonId = personDto.getId();

        TranslatorGroupDto groupDto = new TranslatorGroupDto();
        groupDto.setName("Group name 2");
        groupDto = projectService.createGroupDto(groupDto);

        testGroupId = groupDto.getId();

        ProjectDto prj = new ProjectDto();
        prj.setName("Translate IT 22");
        prj.setSourceLocale(new Locale("fi_FI"));
        prj.setFormat(LanguageFileFormat.PROPERTIES);
        prj.setType(LanguageFileType.UTF_8);
        prj = projectService.createProjectDto(prj,"James Bond");
    }

    @After
    public void reset() {
        // remove all for a person
        List<ProjectDto> personPrjs = projectService.getProjectDtos(testPersonId);
        projectService.removeProjectDtos(personPrjs);

        projectService.removePersonDto(testPersonId);

        projectService.removeGroupDto(testGroupId);
    }

    @Test
    public void AddProject_assertAllFields() {
        ProjectDto prj = new ProjectDto();
        prj.setName("Translate IT 333");
        prj.setSourceLocale(new Locale("fi_FI"));
        prj.setFormat(LanguageFileFormat.PROPERTIES);
        prj.setType(LanguageFileType.UTF_8);
        prj = projectService.createProjectDto(prj,"James Bond");

        assertThat("Translate IT 333",equalTo(prj.getName()));
        assertThat("fi_FI".toLowerCase(),equalTo(prj.getSourceLocale().toString()));
        assertThat(LanguageFileFormat.PROPERTIES,equalTo(prj.getFormat()));
        assertThat(LanguageFileType.UTF_8,equalTo(prj.getType()));

        projectService.removeProjectDto(prj.getId());

    }

    @Test
    public void AddProjectHavingShortProjectName_assertViolation__ProjectNameSize() {
        ProjectDto prj = new ProjectDto();
        prj.setName("Tr.");
        prj.setSourceLocale(new Locale("fi_FI"));
        prj.setFormat(LanguageFileFormat.PROPERTIES);
        prj.setType(LanguageFileType.UTF_8);

        try {
            prj = projectService.createProjectDto(prj,"James Bond");
            projectService.removeProjectDto(prj.getId());
            fail("No Constraint Violation Exception thrown");
        } catch (ConstraintViolationException e) {                        
            ConstraintViolation<ProjectDto> constraintViolation = (ConstraintViolation<ProjectDto>) e.getConstraintViolations().stream().findFirst().get();
            String messageTemplate = constraintViolation.getMessageTemplate();
            assert("ProjectDto.projectName.size".equals(messageTemplate));                        
        }
    }
    
    @Test
    public void AddProjectLongProjectName_assertViolation_ProjectNameSize() {
        ProjectDto prj = new ProjectDto();
        prj.setName("Tr..........................................................");
        prj.setSourceLocale(new Locale("fi_FI"));
        prj.setFormat(LanguageFileFormat.PROPERTIES);
        prj.setType(LanguageFileType.UTF_8);

        try {
            prj = projectService.createProjectDto(prj,"James Bond");
            projectService.removeProjectDto(prj.getId());
            fail("No Constraint Violation Exception thrown");
        } catch (ConstraintViolationException e) {                        
            ConstraintViolation<ProjectDto> constraintViolation = (ConstraintViolation<ProjectDto>) e.getConstraintViolations().stream().findFirst().get();
            String messageTemplate = constraintViolation.getMessageTemplate();
            assert("ProjectDto.projectName.size".equals(messageTemplate));                        
        }
    }
    
    @Test
    public void AddEmptyProject_assertViolation_Name_SourceLocale_Format_and_Type() {

        try {
            ProjectDto prj = new ProjectDto();
            prj = projectService.createProjectDto(prj,"James Bond");
            projectService.removeProjectDto(prj.getId());
            fail("No Constraint Violation Exception thrown");
        } catch (ConstraintViolationException e) {

            List <Path> returnedPropertyPaths = new ArrayList<Path>();
            List <String> returnedFields = new ArrayList<String>();

            e.getConstraintViolations().stream()
            .forEach(v -> returnedPropertyPaths.add(v.getPropertyPath()));

            for(Path p : returnedPropertyPaths) {
                Iterator<Path.Node> nodeIterator = p.iterator();
                String lastNode = "";
                while (nodeIterator.hasNext()) {
                    Path.Node node = nodeIterator.next();
                    lastNode = node.toString();
                }
                returnedFields.add(lastNode);
            }

            List <String> expectedFields = Arrays.asList("format","name","sourceLocale","charset");

            Collections.sort(returnedFields);
            Collections.sort(expectedFields);
            assertThat(returnedFields, 
                    IsIterableContainingInOrder.contains(expectedFields.toArray()));
        }

    }

    @Test
    public void RemoveProject_assertTranslateIt2Exception() {
        ProjectDto prj = new ProjectDto();
        prj.setName("Translate IT 333");
        prj.setSourceLocale(new Locale("fi_FI"));
        prj.setFormat(LanguageFileFormat.PROPERTIES);
        prj.setType(LanguageFileType.UTF_8);
        prj = projectService.createProjectDto(prj,"James Bond");      

        projectService.removeProjectDto(prj.getId());

        assertThatCode(() -> projectService.getProjectDtoByProjectName("Translate IT 333"))
        .isExactlyInstanceOf(TranslateIt2Exception.class);        

    }

    @Test
    public void RemoveProjectHavingWorks_assertWorkCount() {
        ProjectDto prj = projectService.getProjectDtoByProjectName("Translate IT 22");
        WorkDto work = new WorkDto();
        work.setProjectId(prj.getId());
        work.setLocale(new Locale("en_EN"));
        work.setVersion("0.073");
        work.setPriority(Priority.HIGH);
        LocalDate currentDate = LocalDate.now();
        LocalDate deadLine = currentDate.plusMonths(2L);
        deadLine = deadLine.plusDays(5L);
        work.setDeadLine(deadLine);
        work = workService.createWorkDto(work,"Group name 2");   

        projectService.removeProjectDto(prj.getId());

        // assert that no works left
        assertThat(0L, is(equalTo(workService.getWorkDtoCount(testGroupId))));        
    }

    @Test
    public void UpdateProject_assertAllFields() {
        ProjectDto prj = projectService.getProjectDtoByProjectName("Translate IT 22");
        prj.setName("Translate IT 333");
        prj.setSourceLocale(new Locale("en_EN"));
        prj.setFormat(LanguageFileFormat.XLIFF);
        prj.setType(LanguageFileType.ISO8859_1);
        prj = projectService.updateProjectDto(prj);

        assertThat("Translate IT 333",equalTo(prj.getName()));
        assertThat("en_EN".toLowerCase(),equalTo(prj.getSourceLocale().toString()));
        assertThat(LanguageFileFormat.XLIFF,equalTo(prj.getFormat()));
        assertThat(LanguageFileType.ISO8859_1,equalTo(prj.getType()));

        projectService.removeProjectDto(prj.getId());

    }
}
