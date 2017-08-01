package translateit2.configuration;

import java.time.LocalDate;
import java.util.Locale;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import translateit2.lngfileservice.LanguageFileFormat;
import translateit2.lngfileservice.LanguageFileType;
import translateit2.persistence.dto.InfoDto;
import translateit2.persistence.dto.PersonDto;
import translateit2.persistence.dto.ProjectDto;
import translateit2.persistence.dto.TranslatorGroupDto;
import translateit2.persistence.dto.WorkDto;
import translateit2.persistence.model.Priority;
import translateit2.service.ProjectService;

@Component
public class DemoDatabaseInitializer implements DatabaseInitializer {

    @Autowired
    ProjectService projectService;
    
    public void loadDemo() {
        PersonDto personDto = new PersonDto();
        personDto.setFullName("Ilkka");
        personDto = projectService.createPersonDto(personDto);

        InfoDto infoDto = new InfoDto();
        infoDto.setText("This is info");
        infoDto = projectService.createInfoDto(infoDto);

        TranslatorGroupDto groupDto = new TranslatorGroupDto();
        groupDto.setName("Group name");
        groupDto = projectService.createGroupDto(groupDto);

        long startCount = projectService.getProjectDtoCount(0);

        ProjectDto prj = new ProjectDto();
        prj.setName("Translate IT 2");
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
        // work.setOriginalFile("dotcms");
        work.setSkeletonFile("skeleton file");
        // work.setStatus(Status.NEW); not yet!!
        work.setPriority(Priority.HIGH);
        work.setStarted(LocalDate.now());
        // LocalDate finishedDate = LocalDate.parse("2017-05-22");
        // work.setFinished(finishedDate); not yet!!
        LocalDate currentDate = LocalDate.now();
        LocalDate deadLine = currentDate.plusMonths(2L);
        deadLine = LocalDate.parse("2017-05-22");
        work.setDeadLine(deadLine);
        work.setProgress(0.0);
        work.setGroupId(groupDto.getId());
        work = projectService.createWorkDto(work);
    }

}
