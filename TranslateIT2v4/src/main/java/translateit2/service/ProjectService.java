package translateit2.service;

import java.util.List;

import javax.validation.Valid;

import org.springframework.validation.annotation.Validated;

import translateit2.persistence.dto.FileInfoDto;
import translateit2.persistence.dto.InfoDto;
import translateit2.persistence.dto.PersonDto;
import translateit2.persistence.dto.ProjectDto;
import translateit2.persistence.dto.TranslatorGroupDto;

@Validated
public interface ProjectService {
    FileInfoDto createFileInfoDto(@Valid final FileInfoDto entity);

    TranslatorGroupDto createGroupDto(@Valid final TranslatorGroupDto entity);

    InfoDto createInfoDto(@Valid final InfoDto entity);

    /**
     * Person + Group + Info are needed for project creation
     */
    PersonDto createPersonDto(@Valid final PersonDto entity);

    /**
     * Project
     */
    ProjectDto createProjectDto(@Valid final ProjectDto entity);
    
    TranslatorGroupDto getGroupDtoByName(String name);

    PersonDto getPersonDtoByPersonName(String personName);

    ProjectDto getProjectDtoById(long projectId);

    ProjectDto getProjectDtoByProjectName(String projectName);

    long getProjectDtoCount();

    long getProjectDtoCountByPerson( long personId);

    List<ProjectDto> listAllProjectDtos();

    List<ProjectDto> listProjectDtos(long personId);

    void removeProjectDto(final long projectId);
    
    void removeProjectDtos(List<ProjectDto> entities);

    ProjectDto updateProjectDto(@Valid final ProjectDto entity);
}
