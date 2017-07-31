package translateit2.service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import javax.transaction.Transactional;
import javax.validation.Valid;

import org.apache.logging.log4j.Level;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.EnableTransactionManagement;
import org.springframework.validation.annotation.Validated;

import translateit2.LogTest;
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
import translateit2.persistence.model.State;
import translateit2.persistence.model.TranslatorGroup;
import translateit2.persistence.model.Unit;
import translateit2.persistence.model.Work;

@Validated
@EnableTransactionManagement
@Service
public class ProjectServiceImpl implements ProjectService{
	static final Logger logger = LogManager.getLogger(ProjectServiceImpl.class);
	/*
	 * TODO: currently for testing
	 */
	private Level getLoggerLevel() {
        return Level.forName("NOTICE", 450);
	}
	
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
        logger.log(getLoggerLevel(), "Entering createPersonDto with {}", entity.toString());
		Person perPerson = personRepo.save(convertToEntity(entity));
		PersonDto perPersonDto = convertToDto(perPerson);
		logger.log(getLoggerLevel(), "Leaving createPersonDto with {}", perPersonDto.toString());
		return perPersonDto; 
	}

	@Override
	public PersonDto getPersonDtoByPersonName(String name) {
		logger.log(getLoggerLevel(), "Entering getPersonDtoByPersonName with {}", name.toString());
		Optional <Person> perPerson = personRepo.findByFullName(name);
		PersonDto personDto = convertToDto(perPerson.get());
		logger.log(getLoggerLevel(), "Leaving getPersonDtoByPersonName with {}", personDto.toString());
		return personDto;	
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
		logger.log(getLoggerLevel(), "Entering createInfoDto with {}", entity.toString());
		Info perInfo = infoRepo.save(convertToEntity(entity));
		InfoDto infoDto = convertToDto(perInfo);
		logger.log(getLoggerLevel(), "Leaving createInfoDto with {}", infoDto.toString());
		return infoDto; 
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
		logger.log(getLoggerLevel(), "Entering getGroupDtoByName with {}", name.toString());
		Optional <TranslatorGroup> perGroup = groupRepo.findByName(name);
		TranslatorGroupDto groupDto = convertToDto(perGroup.get());
		logger.log(getLoggerLevel(), "Leaving getGroupDtoByName with {}", groupDto.toString());
		return groupDto;	
	}
	
	private TranslatorGroup convertToEntity(TranslatorGroupDto groupDto) {
		TranslatorGroup group = modelMapper.map(groupDto, TranslatorGroup.class);      
		return group;
	}

	@Override
	public TranslatorGroupDto createGroupDto(@Valid final TranslatorGroupDto entity) {
		logger.log(getLoggerLevel(), "Entering createGroupDto with {}", entity.toString());
		TranslatorGroup perGroup = groupRepo.save(convertToEntity(entity));
		TranslatorGroupDto groupDto = convertToDto(perGroup);
		logger.log(getLoggerLevel(), "Leaving createGroupDto with {}", groupDto.toString());
		return groupDto; 
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
		logger.log(getLoggerLevel(), "Entering createProjectDto with {}", entity.toString());
		Project perProject = convertToEntity(entity);
		perProject.setPerson(personRepo.findOne(entity.getPersonId()));
		perProject.setInfo(infoRepo.findOne(entity.getInfoId()));
		perProject = projectRepo.save(perProject);
		ProjectDto projectDto = convertToDto(perProject);
		logger.log(getLoggerLevel(), "Leaving createProjectDto with {}", projectDto.toString());
		return projectDto; 
	}

	@Transactional
	@Override
	public ProjectDto updateProjectDto(@Valid final ProjectDto project) {
		logger.log(getLoggerLevel(), "Entering updateProjectDto with {}", project.toString());
		Project perProject = projectRepo.findOne(project.getId());  
		convertToEntity(project,perProject);
		ProjectDto projectDto = convertToDto(projectRepo.save(perProject));
		logger.log(getLoggerLevel(), "Leaving updateProjectDto with {}", projectDto.toString());
		return projectDto;
	}

	@Transactional
	@Override
	public void removeProjectDto(long projectId) {
		logger.log(getLoggerLevel(), "Entering removeProjectDto with id: {}", projectId);
		if (projectRepo.exists(projectId)){
			List <Work> allWorks = workRepo.findByProjectId(projectId);
			allWorks.stream().forEach(wrk->removeUnitDtos(wrk.getId()));
			workRepo.delete(allWorks);
			projectRepo.delete(projectId);
			logger.log(getLoggerLevel(), "Leaving removeProjectDto()");
		}
		else
			throw new IllegalArgumentException("Could not remove project. No such project having id = " + projectId); 
	}

	@Transactional
	@Override
	public void removeProjectDtos(List<ProjectDto> entities) {
		logger.log(getLoggerLevel(), "Entering removeProjectDtos with list size: {}", entities.size());
		entities.stream().forEach(prj ->removeProjectDto(prj.getId()));	
		logger.log(getLoggerLevel(), "Leaving removeProjectDtos()");
	}

	@Override
	public ProjectDto getProjectDtoById(long projectId) {
		logger.log(getLoggerLevel(), "Entering getProjectDtoById with id: {}", projectId);
		ProjectDto projectDto = convertToDto(projectRepo.findOne(projectId)); 
		logger.log(getLoggerLevel(), "Leaving getProjectDtoById with {}", projectDto);
		return projectDto;
	}

	@Override
	public ProjectDto getProjectDtoByProjectName(String projectName) {
		logger.log(getLoggerLevel(), "Entering getProjectDtoByProjectName with {}", projectName);
		Optional <Project> project = projectRepo.findByName(projectName);
		ProjectDto projectDto = convertToDto(project.get());
		logger.log(getLoggerLevel(), "Leaving getProjectDtoByProjectName with {}", projectDto.toString());
		return projectDto;	
	}

	@Override
	public List<ProjectDto> listProjectDtos(long personId) {
		logger.log(getLoggerLevel(), "Entering listProjectDtos with id: {}", personId);
		List<ProjectDto> projectDtos = new ArrayList<ProjectDto>(); 		
		List<Project> projects=projectRepo.findAll().stream()
				.filter(prj->personId == prj.getPerson().getId())
				.collect(Collectors.toList());
		projects.forEach(prj->projectDtos.add(convertToDto(prj)));
		logger.log(getLoggerLevel(), "Leaving listProjectDtos with list size: {}", projectDtos.size());

		return projectDtos;
	}

	@Override
	public List<ProjectDto> listAllProjectDtos() {
		logger.log(getLoggerLevel(), "Entering listAllProjectDtos()");
		List<ProjectDto> projectDtos = new ArrayList<ProjectDto>(); 		
		projectRepo.findAll().forEach(l->projectDtos.add(convertToDto(l)));	
		logger.log(getLoggerLevel(), "Leaving listProjectDtos() with list size: {}", projectDtos.size());
		return projectDtos;
	}

	@Override
	public long getProjectDtoCount(final long personId){
		logger.log(getLoggerLevel(), "Entering getProjectDtoCount with id: {}",personId);

		long dtoCount = 0;
		
		if (personId != 0)
			dtoCount = projectRepo.countByPersonId(personId);
		else
			dtoCount = projectRepo.count();
		
		logger.log(getLoggerLevel(), "Leaving getProjectDtoCount with dtoCount {}",dtoCount);
		return dtoCount;
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
		logger.log(getLoggerLevel(), "Entering createWorkDto with {} ",entity.toString());

		Work perWork = convertToEntity(entity);
		//perWork.setGroup(groupRepo.findOne(entity.getGroupId()));
		perWork.setProject(projectRepo.findOne(entity.getProjectId()));
		perWork = workRepo.save(perWork);
		WorkDto workDto = convertToDto(perWork); 
		
		logger.log(getLoggerLevel(), "Leaving createWorkDto with {} ",workDto.toString());
		return workDto; 
	}

	@Transactional
	@Override
	public WorkDto updateWorkDto(@Valid final WorkDto work) {
		logger.log(getLoggerLevel(), "Entering updateWorkDto with {} ",work.toString());
		Work perWork = workRepo.findOne(work.getId());
		convertToEntity(work,perWork);
		WorkDto workDto = convertToDto(workRepo.save(perWork));
		logger.log(getLoggerLevel(), "Leaving updateWorkDto with {} ",workDto.toString());
		return workDto;
	}

	@Transactional
	@Override
	public void removeWorkDto(final long workId) {
		logger.log(getLoggerLevel(), "Entering removeWorkDto with id: {} ",workId);

		if (workRepo.exists(workId)){
			removeUnitDtos(workId);
			workRepo.delete(workId);
			logger.log(getLoggerLevel(), "Leaving removeWorkDto()");
		}
		else
			throw new IllegalArgumentException("Could not remove work. No such work having id = " + workId); 
	}

	@Transactional
	@Override
	public void removeWorkDtos(List<WorkDto> entities) {
		logger.log(getLoggerLevel(), "Entering removeWorkDtos with list size {} ",entities.size());
		entities.stream().forEach(wrk ->removeWorkDto(wrk.getId()));	
		logger.log(getLoggerLevel(), "Leaving removeWorkDtos()");
	}

	@Override
	public WorkDto getWorkDtoById(long workId) {
		logger.log(getLoggerLevel(), "Entering getWorkDtoById with id: {} ",workId);
		WorkDto workDto = convertToDto(workRepo.findOne(workId));
		logger.log(getLoggerLevel(), "Leaving getWorkDtoById with {} ",workDto.toString());
		return workDto;
	}

	@Override
	public long getWorkDtoCount(final long groupId){
		logger.log(getLoggerLevel(), "Entering getWorkDtoCount with id: {} ",groupId);
		long cnt = workRepo.countByGroupId(groupId);
		logger.log(getLoggerLevel(), "Leaving getWorkDtoCount with count {} ",cnt);
		return cnt ;		
	}
	
	@Override
	public List<WorkDto> listWorkDtos(long groupId) {
		logger.log(getLoggerLevel(), "Entering listWorkDtos with id: {} ",groupId);
		List<WorkDto> workDtos = new ArrayList<WorkDto>(); 		
		List<Work> works=workRepo.findAll().stream()
				.filter(wrk->groupId == wrk.getGroup().getId())
				.collect(Collectors.toList());
		works.forEach(prj->workDtos.add(convertToDto(prj)));

		logger.log(getLoggerLevel(), "Leaving listWorkDtos with list size: {} ",workDtos.size());
		return workDtos;
	}

	@Override
	public List<WorkDto> listProjectWorkDtos(long projectId) {
		logger.log(getLoggerLevel(), "Entering listProjectWorkDtos with id: {} ",projectId);
		List<WorkDto> workDtos = new ArrayList<WorkDto>(); 		
		List<Work> works=workRepo.findAll().stream()
				.filter(wrk->projectId == wrk.getProject().getId())
				.collect(Collectors.toList());
		works.forEach(prj->workDtos.add(convertToDto(prj)));

		logger.log(getLoggerLevel(), "Leaving listWorkDtos with list size: {} ",workDtos.size());
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
		logger.log(getLoggerLevel(), "Entering createUnitDtos with unit list size {} and  id: {} ",unitDtos.size(),workId);
		List <Unit> units = new ArrayList<Unit>();
		Work work=workRepo.findOne(workId);
		unitDtos.stream().forEach(dto->units.add(convertToEntity(dto)));
		units.stream().forEach(unit->unit.setWork(work));
		unitRepo.save(units);
		//logger.log(getLoggerLevel(), "Leaving createUnitDtos() saved for {}", work.toString());
		logger.log(getLoggerLevel(), "Leaving createUnitDtos()");
	}

	@Transactional
	@Override
	public void updateUnitDtos(List<UnitDto> unitDtos, long workId) {
		logger.log(getLoggerLevel(), "Entering updateUnitDtos with unit list size {} and  id: {} ",unitDtos.size(),workId);
		List <Unit> units = new ArrayList<Unit>();
	
		for (UnitDto dto : unitDtos) {
			Unit perUnit = unitRepo.findOne(dto.getId());  
			convertToEntity(dto,perUnit);
			units.add(perUnit);
		}
		unitRepo.save(units);
		logger.log(getLoggerLevel(), "Leaving updateUnitDtos()");
	}
	
	@Override
	public long getUnitDtoCount(final long workId){
		logger.log(getLoggerLevel(), "Entering getUnitDtoCount with id: {} ",workId);
		long cnt = unitRepo.countByWorkId(workId); 
		logger.log(getLoggerLevel(), "Leaving getUnitDtoCount with count: {} ",cnt);
		return cnt;
	}
	
	@Override
	public UnitDto getUnitDtoById(long unitId) {
		logger.log(getLoggerLevel(), "Entering getUnitDtoById with id: {} ",unitId);
		UnitDto unitDto = convertToDto(unitRepo.findOne(unitId)); 
		logger.log(getLoggerLevel(), "Leaving getUnitDtoById with id: {} ",unitDto.toString());
		return unitDto;
	}
	
	@Override
	public List<UnitDto> listUnitDtos(final long workId) {
		logger.log(getLoggerLevel(), "Entering listUnitDtos with id: {} ",workId);
		List <UnitDto> unitDtos = new ArrayList<UnitDto>();
		List<Unit> units=unitRepo.findAll().stream()
				.filter(unit->workId == unit.getWork().getId())
				.collect(Collectors.toList());
		units.forEach(unit->unitDtos.add(convertToDto(unit)));
		
		logger.log(getLoggerLevel(), "Leaving listUnitDtos with unit list size: {} ",unitDtos.size());
		return unitDtos;
	}
	
	@Transactional
	@Override
	public void removeUnitDtos(final long workId) {
		logger.log(getLoggerLevel(), "Entering removeUnitDtos with id: {} ",workId);
		List<Unit> units=unitRepo.findAll().stream()
				.filter(unit->workId == unit.getWork().getId())
				.collect(Collectors.toList());
		unitRepo.delete(units);
		logger.log(getLoggerLevel(), "Leaving removeUnitDtos()");
	}
	
	// https://www.javacodegeeks.com/2013/05/spring-data-solr-tutorial-pagination.html
	@Override 
	public List <UnitDto> getPage(final long workId, int pageIndex, int pageSize) {
		//TODO: not working Page<Unit> p = unitRepo.getUnitsByWorkId(Long.valueOf(workId), req);
		 
		logger.log(getLoggerLevel(), "Entering getPage with  id: {}, pageIndex {} and pageSize {}",workId, pageIndex, pageSize);
		PageRequest req = new PageRequest(pageIndex, pageSize, 
				Sort.Direction.ASC, "serialNumber");
		Page<Unit> p = unitRepo.findByWorkId(workId, req);
		
		List <UnitDto> unitDtos = new ArrayList<UnitDto>();
		p.forEach(unit->unitDtos.add(convertToDto(unit)));

		logger.log(getLoggerLevel(), "Leaving getPage with unit list size {}",unitDtos.size());
		return unitDtos;
	}

	@Override
	public long getStatistics(long workId) {
		logger.log(getLoggerLevel(), "Entering getStatistics with  id: {} ",workId);

		long translated = unitRepo.countStates(Long.valueOf(workId), State.TRANSLATED);
		translated = unitRepo.countByWorkIdAndTargetState(workId,State.TRANSLATED);
		
		logger.log(getLoggerLevel(), "Leaving getStatistics with {} ",translated);

		return translated;
	}
}
