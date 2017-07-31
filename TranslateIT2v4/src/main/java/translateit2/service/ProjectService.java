package translateit2.service;

import java.util.List;

import javax.validation.Valid;

import org.springframework.validation.annotation.Validated;

import translateit2.persistence.dto.InfoDto;
import translateit2.persistence.dto.PersonDto;
import translateit2.persistence.dto.ProjectDto;
import translateit2.persistence.dto.TranslatorGroupDto;
import translateit2.persistence.dto.UnitDto;
import translateit2.persistence.dto.WorkDto;

@Validated
public interface ProjectService {
	/**
	 * Person + Group + Info for testing => TODO: move to another service
	 */	
	PersonDto createPersonDto(@Valid final PersonDto entity);
	PersonDto getPersonDtoByPersonName(String personName);
	TranslatorGroupDto createGroupDto(@Valid final TranslatorGroupDto entity);
	TranslatorGroupDto getGroupDtoByName(String name);
	InfoDto createInfoDto(@Valid final InfoDto entity);	
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
	
	/**
	 * Work 
	 */	
	WorkDto createWorkDto(@Valid final WorkDto entity);
	WorkDto updateWorkDto(@Valid final WorkDto entity);
	void removeWorkDto(final long workId); 	
	void removeWorkDtos(List<WorkDto> entities);
	WorkDto getWorkDtoById(long workId);
	long getWorkDtoCount(final long groupId);
	List<WorkDto> listWorkDtos(long groupId);
	List<WorkDto> listProjectWorkDtos(final long projectId);
	
	/**
	 * Unit 
	 */
	void createUnitDtos(@Valid List<UnitDto> unitDtos, final long workId);
	void updateUnitDtos(@Valid List<UnitDto> unitDtos, final long workId);
	void removeUnitDtos(final long workId);
	long getUnitDtoCount(final long workId);
	UnitDto getUnitDtoById(long unitId);
	List<UnitDto> getPage(final long workId, int pageIndex, int pageSize);
	List<UnitDto> listUnitDtos(final long workId);	
	long getStatistics(final long workId);
}
