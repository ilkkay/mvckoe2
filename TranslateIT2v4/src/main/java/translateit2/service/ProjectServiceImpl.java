package translateit2.service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import javax.transaction.Transactional;
import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.EnableTransactionManagement;
import org.springframework.validation.annotation.Validated;

import translateit2.persistence.dao.InfoRepository;
import translateit2.persistence.dao.PersonRepository;
import translateit2.persistence.dao.ProjectRepository;
import translateit2.persistence.dao.TranslatorGroupRepository;
import translateit2.persistence.dao.UnitRepository;
import translateit2.persistence.dao.WorkRepository;
import translateit2.persistence.dto.InfoDto;
import translateit2.persistence.dto.PersonDto;
import translateit2.persistence.dto.ProjectDto;
import translateit2.persistence.dto.TranslatorGroupDto;
import translateit2.persistence.dto.UnitDto;
import translateit2.persistence.dto.WorkDto;
import translateit2.persistence.dto.ProjectMapper;
import translateit2.persistence.model.Info;
import translateit2.persistence.model.Person;
import translateit2.persistence.model.Project;
import translateit2.persistence.model.TranslatorGroup;
import translateit2.persistence.model.Unit;
import translateit2.persistence.model.Work;

@Validated
@EnableTransactionManagement
@Service
public class ProjectServiceImpl implements ProjectService{
	@Autowired
	private ProjectMapper modelMapper;

	@Autowired
	private ProjectRepository projectRepo;

	@Autowired
	private PersonRepository personRepo;
	
	@Autowired
	private TranslatorGroupRepository groupRepo;

	@Autowired
	private InfoRepository infoRepo;

	@Autowired
	private WorkRepository workRepo;
	
	@Autowired
	private UnitRepository unitRepo;

	/*
	 * PERSON services start 
	 */	
	private PersonDto convertToDto(Person person) {
		if (person == null) return null;
		PersonDto personDto = modelMapper.map(person, PersonDto.class);
		return personDto;
	}

	private Person convertToEntity(PersonDto personDto) {
		Person person = modelMapper.map(personDto, Person.class);      
		return person;
	}

	@Override
	public PersonDto createPersonDto(@Valid final PersonDto entity) {

		Person perPerson = personRepo.save(convertToEntity(entity));
		return convertToDto(perPerson); 
	}

	@Override
	public PersonDto getPersonDtoByPersonName(String name) {
		Optional <Person> perPerson = personRepo.findByFullName(name);
		return convertToDto(perPerson.get());	
	}
	
	/*
	 * PERSON services end 
	 */	

	/*
	 * INFO services start 
	 */
	private InfoDto convertToDto(Info info) {
		if (info == null) return null;
		InfoDto infoDto = modelMapper.map(info, InfoDto.class);
		return infoDto;
	}

	private Info convertToEntity(InfoDto infoDto) {
		Info info = modelMapper.map(infoDto, Info.class);      
		return info;
	}

	@Override
	public InfoDto createInfoDto(@Valid final InfoDto entity) {
		Info perInfo = infoRepo.save(convertToEntity(entity));
		return convertToDto(perInfo); 
	}
	/*
	 * INFO services end 
	 */	
	/*
	 * GROUP services start 
	 */	
	private TranslatorGroupDto convertToDto(TranslatorGroup group) {
		if (group == null) return null;
		TranslatorGroupDto groupDto = modelMapper.map(group, TranslatorGroupDto.class);
		return groupDto;
	}

	@Override
	public TranslatorGroupDto getGroupDtoByName(String name) {
		Optional <TranslatorGroup> perGroup = groupRepo.findByName(name);
		return convertToDto(perGroup.get());	
	}
	
	private TranslatorGroup convertToEntity(TranslatorGroupDto groupDto) {
		TranslatorGroup group = modelMapper.map(groupDto, TranslatorGroup.class);      
		return group;
	}

	@Override
	public TranslatorGroupDto createGroupDto(@Valid final TranslatorGroupDto entity) {
		TranslatorGroup perGroup = groupRepo.save(convertToEntity(entity));
		return convertToDto(perGroup); 
	}

	/*
	 * GROUP services end 
	 */
	/*
	 * PROJECT services start 
	 */	
	private ProjectDto convertToDto(Project project) {
		if (project == null) return null;
		ProjectDto projectDto = modelMapper.map(project, ProjectDto.class);
		return projectDto;
	}

	private Project convertToEntity(ProjectDto projectDto) {
		Project project = modelMapper.map(projectDto, Project.class);      
		return project;
	} 

	private void convertToEntity(ProjectDto projectDto,Project project) {
		modelMapper.map(projectDto, project);              
	}
	
	@Transactional
	@Override
	public ProjectDto createProjectDto(ProjectDto entity) {
		Project perProject = convertToEntity(entity);
		perProject.setPerson(personRepo.findOne(entity.getPersonId()));
		perProject.setInfo(infoRepo.findOne(entity.getInfoId()));
		perProject = projectRepo.save(perProject);

		return convertToDto(perProject); 
	}

	@Transactional
	@Override
	public ProjectDto updateProjectDto(@Valid final ProjectDto project) {
		Project perProject = projectRepo.findOne(project.getId());  
		convertToEntity(project,perProject);
		return convertToDto(projectRepo.save(perProject));
	}

	@Transactional
	@Override
	public void removeProjectDto(long projectId) {
		if (projectRepo.exists(projectId)){
			List <Work> allWorks = workRepo.findByProjectId(projectId);
			allWorks.stream().forEach(wrk->removeUnitDtos(wrk.getId()));
			workRepo.delete(allWorks);
			projectRepo.delete(projectId);
		}
		else
			throw new IllegalArgumentException("Could not remove project. No such project having id = " + projectId); 
	}

	@Transactional
	@Override
	public void removeProjectDtos(List<ProjectDto> entities) {
		entities.stream().forEach(prj ->removeProjectDto(prj.getId()));	
	}

	@Override
	public ProjectDto getProjectDtoById(long projectId) {
		return convertToDto(projectRepo.findOne(projectId));
	}

	@Override
	public ProjectDto getProjectDtoByProjectName(String projectName) {
		Optional <Project> project = projectRepo.findByName(projectName);
		return convertToDto(project.get());	
	}

	@Override
	public List<ProjectDto> listProjectDtos(long personId) {
		List<ProjectDto> projectDtos = new ArrayList<ProjectDto>(); 		
		List<Project> projects=projectRepo.findAll().stream()
				.filter(prj->personId == prj.getPerson().getId())
				.collect(Collectors.toList());
		projects.forEach(prj->projectDtos.add(convertToDto(prj)));

		return projectDtos;
	}

	@Override
	public List<ProjectDto> listAllProjectDtos() {
		List<ProjectDto> projectDtos = new ArrayList<ProjectDto>(); 		
		projectRepo.findAll().forEach(l->projectDtos.add(convertToDto(l)));	
		return projectDtos;
	}

	@Override
	public long getProjectDtoCount(final long personId){
		if (personId != 0)
			return projectRepo.countByPersonId(personId);
		else
			return projectRepo.count();
	}
	/**
	 *     PROJECT ends
	 */

	/*
	 * WORK services start 
	 */	
	private WorkDto convertToDto(Work work) {
		if (work == null) return null;
		WorkDto workDto = modelMapper.map(work, WorkDto.class);
		return workDto;
	}

	private Work convertToEntity(WorkDto workDto) {
		Work work = modelMapper.map(workDto, Work.class);      
		return work;
	} 

	private void convertToEntity(WorkDto workDto,Work work) {
		modelMapper.map(workDto, work);              
	}
	@Transactional
	@Override
	public WorkDto createWorkDto(@Valid final WorkDto entity) {
		Work perWork = convertToEntity(entity);
		//perWork.setGroup(groupRepo.findOne(entity.getGroupId()));
		perWork.setProject(projectRepo.findOne(entity.getProjectId()));
		perWork = workRepo.save(perWork);

		return convertToDto(perWork); 
	}

	@Transactional
	@Override
	public WorkDto updateWorkDto(@Valid final WorkDto work) {
		Work perWork = workRepo.findOne(work.getId());  
		convertToEntity(work,perWork);
		return convertToDto(workRepo.save(perWork));
	}

	@Transactional
	@Override
	public void removeWorkDto(final long workId) {
		if (workRepo.exists(workId)){
			removeUnitDtos(workId);
			workRepo.delete(workId);
		}
		else
			throw new IllegalArgumentException("Could not remove work. No such work having id = " + workId); 
	}

	@Transactional
	@Override
	public void removeWorkDtos(List<WorkDto> entities) {
		entities.stream().forEach(wrk ->removeWorkDto(wrk.getId()));	
	}

	@Override
	public WorkDto getWorkDtoById(long workId) {
		return convertToDto(workRepo.findOne(workId));
	}

	@Override
	public long getWorkDtoCount(final long groupId){
		return workRepo.countByGroupId(groupId);
	}
	
	@Override
	public List<WorkDto> listWorkDtos(long groupId) {
		List<WorkDto> workDtos = new ArrayList<WorkDto>(); 		
		List<Work> works=workRepo.findAll().stream()
				.filter(wrk->groupId == wrk.getGroup().getId())
				.collect(Collectors.toList());
		works.forEach(prj->workDtos.add(convertToDto(prj)));

		return workDtos;
	}

	@Override
	public List<WorkDto> listProjectWorkDtos(long projectId) {
		List<WorkDto> workDtos = new ArrayList<WorkDto>(); 		
		List<Work> works=workRepo.findAll().stream()
				.filter(wrk->projectId == wrk.getProject().getId())
				.collect(Collectors.toList());
		works.forEach(prj->workDtos.add(convertToDto(prj)));

		return workDtos;
	}
	
	/*
	 * UNIT services start 
	 */
	private UnitDto convertToDto(Unit unit) {
		if (unit == null) return null;
		UnitDto unitDto = modelMapper.map(unit, UnitDto.class);
		return unitDto;
	}

	private Unit convertToEntity(UnitDto unitDto) {
		Unit unit = modelMapper.map(unitDto, Unit.class);      
		return unit;
	} 

	private void convertToEntity(UnitDto unitDto,Unit unit) {
		modelMapper.map(unitDto, unit);              
	}

	@Transactional
	@Override
	public void createUnitDtos(List<UnitDto> unitDtos, long workId) {
		List <Unit> units = new ArrayList<Unit>();
		Work work=workRepo.findOne(workId);
		unitDtos.stream().forEach(dto->units.add(convertToEntity(dto)));
		units.stream().forEach(unit->unit.setWork(work));
		unitRepo.save(units);
	}

	@Transactional
	@Override
	public void updateUnitDtos(List<UnitDto> unitDtos, long workId) {
		List <Unit> units = new ArrayList<Unit>();
	
		for (UnitDto dto : unitDtos) {
			Unit perUnit = unitRepo.findOne(dto.getId());  
			convertToEntity(dto,perUnit);
			units.add(perUnit);
		}
		unitRepo.save(units);
	}
	
	@Override
	public long getUnitDtoCount(final long workId){
		return unitRepo.countByWorkId(workId);
	}
	
	@Override
	public UnitDto getUnitDtoById(long unitId) {
		return convertToDto(unitRepo.findOne(unitId));
	}
	
	@Override
	public List<UnitDto> listUnitDtos(final long workId) {
		List <UnitDto> unitDtos = new ArrayList<UnitDto>();
		List<Unit> units=unitRepo.findAll().stream()
				.filter(unit->workId == unit.getWork().getId())
				.collect(Collectors.toList());
		units.forEach(unit->unitDtos.add(convertToDto(unit)));
		
		return unitDtos;
	}
	
	@Transactional
	@Override
	public void removeUnitDtos(final long workId) {
		List<Unit> units=unitRepo.findAll().stream()
				.filter(unit->workId == unit.getWork().getId())
				.collect(Collectors.toList());
		unitRepo.delete(units);
	}
	
	// https://www.javacodegeeks.com/2013/05/spring-data-solr-tutorial-pagination.html
	@Override 
	public List <UnitDto> getPage(final long workId, int pageIndex, int pageSize) {
		//TODO: not working Page<Unit> p = unitRepo.getUnitsByWorkId(Long.valueOf(workId), req);
		 
		PageRequest req = new PageRequest(pageIndex, pageSize, 
				Sort.Direction.ASC, "serialNumber");
		Page<Unit> p = unitRepo.findByWorkId(workId, req);
		
		List <UnitDto> unitDtos = new ArrayList<UnitDto>();
		p.forEach(unit->unitDtos.add(convertToDto(unit)));
		return unitDtos;
	}
}
