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
	public PersonDto createPersonDto(@Valid final PersonDto entity);
	public PersonDto getPersonDtoByPersonName(String personName);
	public TranslatorGroupDto createGroupDto(@Valid final TranslatorGroupDto entity);
	public TranslatorGroupDto getGroupDtoByName(String name);
	public InfoDto createInfoDto(@Valid final InfoDto entity);	
	/**
	 * Project 
	 */	
	public ProjectDto createProjectDto(@Valid final ProjectDto entity);
	public ProjectDto updateProjectDto(@Valid final ProjectDto entity);
	public void removeProjectDto(final long projectId); 	
	public void removeProjectDtos(List<ProjectDto> entities);
	public ProjectDto getProjectDtoById(long projectId);
	public ProjectDto getProjectDtoByProjectName(String projectName);
	public List<ProjectDto> listAllProjectDtos();
	public long getProjectDtoCount(final long personId);
	public List<ProjectDto> listProjectDtos(long personId);
	
	/**
	 * Work 
	 */	
	public WorkDto createWorkDto(@Valid final WorkDto entity);
	public WorkDto updateWorkDto(@Valid final WorkDto entity);
	public void removeWorkDto(final long workId); 	
	public void removeWorkDtos(List<WorkDto> entities);
	public WorkDto getWorkDtoById(long workId);
	public long getWorkDtoCount(final long groupId);
	public List<WorkDto> listWorkDtos(long groupId);
	public List<WorkDto> listProjectWorkDtos(final long projectId);
	
	/**
	 * Unit 
	 */
	public void createUnitDtos(@Valid List<UnitDto> unitDtos, final long workId);
	public void updateUnitDtos(@Valid List<UnitDto> unitDtos, final long workId);
	public void removeUnitDtos(final long workId);
	public long getUnitDtoCount(final long workId);
	public UnitDto getUnitDtoById(long unitId);
	public List<UnitDto> getPage(final long workId, int pageIndex, int pageSize);
	public List<UnitDto> listUnitDtos(final long workId);	
}
