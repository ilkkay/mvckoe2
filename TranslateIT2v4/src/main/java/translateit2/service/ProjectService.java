package translateit2.service;

import java.util.List;

import javax.validation.Valid;

import org.springframework.validation.annotation.Validated;

import translateit2.persistence.dto.FileInfoDto;
import translateit2.persistence.dto.InfoDto;
import translateit2.persistence.dto.PersonDto;
import translateit2.persistence.dto.ProjectDto;
import translateit2.persistence.dto.TranslatorGroupDto;
import translateit2.persistence.dto.UnitDto;
import translateit2.persistence.dto.WorkDto;

@Validated
public interface ProjectService {
    /**
     * Person + Group + Info are needed for project creation
     */
    PersonDto createPersonDto(@Valid final PersonDto entity);

    PersonDto getPersonDtoByPersonName(String personName);

    TranslatorGroupDto createGroupDto(@Valid final TranslatorGroupDto entity);

    TranslatorGroupDto getGroupDtoByName(String name);

    InfoDto createInfoDto(@Valid final InfoDto entity);
    
    FileInfoDto createFileInfoDto(@Valid final FileInfoDto entity);

    /**
     * Project
     */
    ProjectDto createProjectDto(@Valid final ProjectDto entity);

    ProjectDto updateProjectDto(@Valid final ProjectDto entity);

    void removeProjectDto(final long projectId);

    void removeProjectDtos(List<ProjectDto> entities);

    ProjectDto getProjectDtoById(long projectId);

    ProjectDto getProjectDtoByProjectName(String projectName);

    List<ProjectDto> listAllProjectDtos();

    long getProjectDtoCount(final long personId);

    List<ProjectDto> listProjectDtos(long personId);
}
