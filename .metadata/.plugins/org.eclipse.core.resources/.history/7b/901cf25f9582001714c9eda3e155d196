package translateit2;

import static org.hamcrest.CoreMatchers.equalTo;
import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.assertThat;
import static org.junit.Assert.fail;

import java.util.Locale;

import javax.validation.ConstraintViolationException;

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

import translateit2.languagefile.LanguageFileFormat;
import translateit2.languagefile.LanguageFileType;
import translateit2.persistence.dto.ProjectDto;
import translateit2.service.ProjectService;
import translateit2.util.Messages;

// http://www.journaldev.com/2668/spring-validation-example-mvc-validator
// https://www.petrikainulainen.net/programming/spring-framework/spring-from-the-trenches-adding-validation-to-a-rest-api/

@ConfigurationProperties(prefix = "test.translateit2")
@TestPropertySource("test.properties")
// @TestPropertySource(properties = {"ProjectNameMaxSize =
// 666","ProjectNameMinSize = 3"})
@RunWith(SpringRunner.class)
@SpringBootTest(classes = TranslateIt2v4Application.class)
public class ProjectExceptionIntegrationTests {
    static final Logger logger = LogManager.getLogger(ProjectExceptionIntegrationTests.class.getName());

    private Integer projectNameMaxSize;

    private Integer projectNameMinSize;

    @Autowired
    private ProjectService projectService;

    @Autowired
    Messages messages;

    public void setProjectNameMaxSize(Integer projectNameMaxSize) {
        this.projectNameMaxSize = projectNameMaxSize;
    }

    public void setProjectNameMinSize(Integer projectNameMinSize) {
        this.projectNameMinSize = projectNameMinSize;
    }

    @Before
    public void setup() {
        Locale.setDefault(Locale.ENGLISH); // for javax validation
        messages.resetLocale(Locale.ENGLISH); // for custom validation
    }

    @Test
    public void parameterTest() {
        System.out.println("ProjectNameMaxSize: " + projectNameMaxSize);
        System.out.println("ProjectNameMinSize: " + projectNameMinSize);
    }

    // @Test
    public void failCreateProject_ifLocaleNull() throws Exception {
        ProjectDto projectDto = new ProjectDto();
        projectDto.setName("dotCMS");
        projectDto.setFormat(LanguageFileFormat.PROPERTIES);
        projectDto.setType(LanguageFileType.UTF_8);
        projectDto.setSourceLocale(null);
        try {
            projectDto = projectService.createProjectDto(projectDto);
            fail("No Constraint Violation Exception thrown");
        } catch (ConstraintViolationException e) {
            assertThat(e.getConstraintViolations().stream()
                    .filter(v -> v.getMessageTemplate().equals("{javax.validation.constraints.NotNull.message}"))
                    .count(), is(equalTo(1L)));
        }
    }

    // @Test
    public void failCreateProject_ifProjectnameEmpty() throws Exception {
        ProjectDto projectDto = new ProjectDto();
        projectDto.setName(null);
        projectDto.setFormat(LanguageFileFormat.PROPERTIES);
        projectDto.setType(LanguageFileType.UTF_8);
        projectDto.setSourceLocale(new Locale("en_EN"));
        try {
            projectDto = projectService.createProjectDto(projectDto);
            fail("No Constraint Violation Exception thrown");
        } catch (ConstraintViolationException e) {
            assertThat(
                    e.getConstraintViolations().stream()
                            .filter(v -> v.getMessageTemplate()
                                    .equals("{org.hibernate.validator.constraints.NotBlank.message}"))
                            .count(),
                    is(equalTo(1L)));
        }
    }

    @Test
    public void failCreateProject_ifProjectNameExists() throws Exception {
        ProjectDto projectDto = new ProjectDto();
        projectDto.setName("Translate IT 2");
        projectDto.setFormat(LanguageFileFormat.PROPERTIES);
        projectDto.setType(LanguageFileType.UTF_8);
        projectDto.setSourceLocale(new Locale("en_EN"));

        logger.info("Given projectdto {}", projectDto.toString());

        try {
            projectDto = projectService.createProjectDto(projectDto);
            fail("No Constraint Violation Exception thrown");
        } catch (ConstraintViolationException e) {
            assertThat(e.getConstraintViolations().stream()
                    .filter(v -> v.getMessage().contains(messages.getPart("ProjectValidator.project_exists_already")))
                    .count(), is(equalTo(1L)));
        }
    }

    // @Test
    public void failCreateProject_ifShortProjectname() throws Exception {
        ProjectDto projectDto = new ProjectDto();
        projectDto.setName("prj");
        projectDto.setFormat(LanguageFileFormat.PROPERTIES);
        projectDto.setType(LanguageFileType.UTF_8);
        projectDto.setSourceLocale(new Locale("en_EN"));
        try {
            projectDto = projectService.createProjectDto(projectDto);
            fail("No Constraint Violation Exception thrown");
        } catch (ConstraintViolationException e) {
            assertThat(e.getConstraintViolations().stream()
                    .filter(v -> v.getMessage().contains(messages.getPart("ProjectDto.projectName.size"))).count(),
                    is(equalTo(1L)));
        }
    }


    /*
     * @Test public void create_locodto_with_empty_and_null_segment() throws
     * Exception{ TransuDto transuDto = new TransuDto();
     * transuDto.setSourceSegm("1"); // must contain atlest ... and not empty
     * transuDto.setTargetSegm(null); transuDto.setRowId(4);
     * 
     * try{ fail("No Constraint Violation Exception thrown"); }
     * catch(ConstraintViolationException e){
     * assertThat(e.getConstraintViolations().stream()
     * .filter(v->v.getMessage().contains( "may not be null"))
     * //messages.getPart("javax.validation.constraints.NotNull"))) .count() ,
     * is(equalTo(1L)));
     * 
     * assertThat(e.getConstraintViolations().stream()
     * .filter(v->v.getMessage().contains( "must be between")) .count() ,
     * is(equalTo(1L))); } }
     */
}
