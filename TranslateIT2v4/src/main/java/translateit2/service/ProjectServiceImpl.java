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
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.EnableTransactionManagement;
import org.springframework.validation.annotation.Validated;

import translateit2.persistence.dao.FileInfoRepository;
import translateit2.persistence.dao.InfoRepository;
import translateit2.persistence.dao.PersonRepository;
import translateit2.persistence.dao.ProjectRepository;
import translateit2.persistence.dao.TranslatorGroupRepository;
import translateit2.persistence.dao.UnitRepository;
import translateit2.persistence.dao.WorkRepository;
import translateit2.persistence.dto.FileInfoDto;
import translateit2.persistence.dto.InfoDto;
import translateit2.persistence.dto.PersonDto;
import translateit2.persistence.dto.ProjectDto;
import translateit2.persistence.dto.ProjectMapper;
import translateit2.persistence.dto.TranslatorGroupDto;
import translateit2.persistence.model.FileInfo;
import translateit2.persistence.model.Info;
import translateit2.persistence.model.Person;
import translateit2.persistence.model.Project;
import translateit2.persistence.model.TranslatorGroup;
import translateit2.persistence.model.Unit;
import translateit2.persistence.model.Work;

@Validated
@EnableTransactionManagement
@Service
public class ProjectServiceImpl implements ProjectService {
    static final Logger logger = LogManager.getLogger(ProjectServiceImpl.class);

    @Autowired
    private FileInfoRepository fileInfoRepo;

    @Autowired
    private TranslatorGroupRepository groupRepo;

    @Autowired
    private InfoRepository infoRepo;

    @Autowired
    private ProjectMapper modelMapper;

    @Autowired
    private PersonRepository personRepo;

    @Autowired
    private ProjectRepository projectRepo;

    @Autowired
    private UnitRepository unitRepo;

    @Autowired
    private WorkRepository workRepo;

    @Transactional
    @Override
    public FileInfoDto createFileInfoDto(@Valid final FileInfoDto entity) {
        logger.log(getLoggerLevel(), "Entering createFileInfoDto with {}", entity.toString());
        FileInfo perInfo = fileInfoRepo.save(convertToEntity(entity));
        FileInfoDto infoDto = convertToDto(perInfo);
        logger.log(getLoggerLevel(), "Leaving createInfoDto with {}", infoDto.toString());
        return infoDto;
    }
    /*
     * FILE INFO services end
     */

    @Transactional
    @Override
    public TranslatorGroupDto createGroupDto(@Valid final TranslatorGroupDto entity) {
        logger.log(getLoggerLevel(), "Entering createGroupDto with {}", entity.toString());
        TranslatorGroup perGroup = groupRepo.save(convertToEntity(entity));
        TranslatorGroupDto groupDto = convertToDto(perGroup);
        logger.log(getLoggerLevel(), "Leaving createGroupDto with {}", groupDto.toString());
        return groupDto;
    }

    @Transactional
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

    @Transactional
    @Override
    public PersonDto createPersonDto(@Valid final PersonDto entity) {
        logger.log(getLoggerLevel(), "Entering createPersonDto with {}", entity.toString());
        Person perPerson = personRepo.save(convertToEntity(entity));
        PersonDto perPersonDto = convertToDto(perPerson);
        logger.log(getLoggerLevel(), "Leaving createPersonDto with {}", perPersonDto.toString());
        return perPersonDto;
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

    /*
     * PERSON services end
     */

    @Override
    public TranslatorGroupDto getGroupDtoByName(String name) {
        logger.log(getLoggerLevel(), "Entering getGroupDtoByName with {}", name.toString());
        Optional<TranslatorGroup> perGroup = groupRepo.findByName(name);
        TranslatorGroupDto groupDto = convertToDto(perGroup.get());
        logger.log(getLoggerLevel(), "Leaving getGroupDtoByName with {}", groupDto.toString());
        return groupDto;
    }

    @Override
    public PersonDto getPersonDtoByPersonName(String name) {
        logger.log(getLoggerLevel(), "Entering getPersonDtoByPersonName with {}", name.toString());
        Optional<Person> perPerson = personRepo.findByFullName(name);
        PersonDto personDto = convertToDto(perPerson.get());
        logger.log(getLoggerLevel(), "Leaving getPersonDtoByPersonName with {}", personDto.toString());
        return personDto;
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
        Optional<Project> project = projectRepo.findByName(projectName);
        ProjectDto projectDto = convertToDto(project.get());
        logger.log(getLoggerLevel(), "Leaving getProjectDtoByProjectName with {}", projectDto.toString());
        return projectDto;
    }

    @Override
    public long getProjectDtoCount() {
        long dtoCount =  projectRepo.count();
        logger.log(getLoggerLevel(), "Leaving getProjectDtoCount with dtoCount {}", dtoCount);
        return dtoCount;
    }

    @Override
    public long getProjectDtoCountByPerson(final long personId) {
        logger.log(getLoggerLevel(), "Entering getProjectDtoCount with id: {}", personId);
        long dtoCount = projectRepo.countByPersonId(personId);
        logger.log(getLoggerLevel(), "Leaving getProjectDtoCount with dtoCount {}", dtoCount);
        return dtoCount;
    }
    
    @Override
    public List<ProjectDto> listAllProjectDtos() {
        logger.log(getLoggerLevel(), "Entering listAllProjectDtos()");
        List<ProjectDto> projectDtos = new ArrayList<ProjectDto>();
        projectRepo.findAll().forEach(l -> projectDtos.add(convertToDto(l)));
        logger.log(getLoggerLevel(), "Leaving listProjectDtos() with list size: {}", projectDtos.size());
        return projectDtos;
    }

    @Override
    public List<ProjectDto> listProjectDtos(long personId) {
        logger.log(getLoggerLevel(), "Entering listProjectDtos with id: {}", personId);
        List<ProjectDto> projectDtos = new ArrayList<ProjectDto>();
        List<Project> projects = projectRepo.findAll().stream().filter(prj -> personId == prj.getPerson().getId())
                .collect(Collectors.toList());
        projects.forEach(prj -> projectDtos.add(convertToDto(prj)));
        logger.log(getLoggerLevel(), "Leaving listProjectDtos with list size: {}", projectDtos.size());

        return projectDtos;
    }

    @Transactional
    @Override
    public void removeProjectDto(long projectId) {
        logger.log(getLoggerLevel(), "Entering removeProjectDto with id: {}", projectId);
        if (projectRepo.exists(projectId)) {
            List<Work> allWorks = workRepo.findByProjectId(projectId);
            allWorks.stream().forEach(wrk -> removeUnitDtos(wrk.getId()));
            workRepo.delete(allWorks);
            projectRepo.delete(projectId);
            logger.log(getLoggerLevel(), "Leaving removeProjectDto()");
        } else
            throw new IllegalArgumentException("Could not remove project. No such project having id = " + projectId);
    }

    @Transactional
    @Override
    public void removeProjectDtos(List<ProjectDto> entities) {
        logger.log(getLoggerLevel(), "Entering removeProjectDtos with list size: {}", entities.size());
        entities.stream().forEach(prj -> removeProjectDto(prj.getId()));
        logger.log(getLoggerLevel(), "Leaving removeProjectDtos()");
    }

    @Transactional
    @Override
    public ProjectDto updateProjectDto(@Valid final ProjectDto project) {
        logger.log(getLoggerLevel(), "Entering updateProjectDto with {}", project.toString());
        Project perProject = projectRepo.findOne(project.getId());
        convertToEntity(project, perProject);
        ProjectDto projectDto = convertToDto(projectRepo.save(perProject));
        logger.log(getLoggerLevel(), "Leaving updateProjectDto with {}", projectDto.toString());
        return projectDto;
    }

    /*
     * FILE INFO services start
     */
    private FileInfoDto convertToDto(FileInfo info) {
        if (info == null)
            return null;
        FileInfoDto infoDto = modelMapper.map(info, FileInfoDto.class);
        return infoDto;
    }

    /*
     * INFO services start
     */
    private InfoDto convertToDto(Info info) {
        if (info == null)
            return null;
        InfoDto infoDto = modelMapper.map(info, InfoDto.class);
        return infoDto;
    }

    /*
     * PERSON services start
     */
    private PersonDto convertToDto(Person person) {
        if (person == null)
            return null;
        PersonDto personDto = modelMapper.map(person, PersonDto.class);
        return personDto;
    }

    /*
     * GROUP services end
     */
    /*
     * PROJECT services start
     */
    private ProjectDto convertToDto(Project project) {
        if (project == null)
            return null;
        ProjectDto projectDto = modelMapper.map(project, ProjectDto.class);
        return projectDto;
    }

    /*
     * GROUP services start
     */
    private TranslatorGroupDto convertToDto(TranslatorGroup group) {
        if (group == null)
            return null;
        TranslatorGroupDto groupDto = modelMapper.map(group, TranslatorGroupDto.class);
        return groupDto;
    }

    private FileInfo convertToEntity(FileInfoDto fileInfoDto) {
        FileInfo info = modelMapper.map(fileInfoDto, FileInfo.class);
        return info;
    }
    
    private Info convertToEntity(InfoDto infoDto) {
        Info info = modelMapper.map(infoDto, Info.class);
        return info;
    }

    private Person convertToEntity(PersonDto personDto) {
        Person person = modelMapper.map(personDto, Person.class);
        return person;
    }

    private Project convertToEntity(ProjectDto projectDto) {
        Project project = modelMapper.map(projectDto, Project.class);
        return project;
    }

    private void convertToEntity(ProjectDto projectDto, Project project) {
        modelMapper.map(projectDto, project);
    }

    private TranslatorGroup convertToEntity(TranslatorGroupDto groupDto) {
        TranslatorGroup group = modelMapper.map(groupDto, TranslatorGroup.class);
        return group;
    }

    /*
     * TODO: currently for testing
     */
    private Level getLoggerLevel() {
        return Level.forName("NOTICE", 450);
    }

    @Transactional
    private void removeUnitDtos(final long workId) {
        logger.log(getLoggerLevel(), "Entering removeUnitDtos with id: {} ", workId);
        List<Unit> units = unitRepo.findAll().stream().filter(unit -> workId == unit.getWork().getId())
                .collect(Collectors.toList());
        unitRepo.delete(units);
        logger.log(getLoggerLevel(), "Leaving removeUnitDtos()");
    }

    /**
     * PROJECT ends
     */    
}
