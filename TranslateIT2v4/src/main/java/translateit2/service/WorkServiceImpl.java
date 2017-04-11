package translateit2.service;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;

import javax.validation.Valid;
import javax.validation.Validator;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import translateit2.persistence.dao.Loco2Repository;
import translateit2.persistence.dao.LocoRepository;
import translateit2.persistence.dao.ProjectRepository;
import translateit2.persistence.dao.TranveRepository;
import translateit2.persistence.dto.Loco2Dto;
import translateit2.persistence.dto.LocoDto;
import translateit2.persistence.dto.LocoMapper;
import translateit2.persistence.dto.ProjectDto;
import translateit2.persistence.dto.TransuDto;
import translateit2.persistence.dto.TransuDto2;
import translateit2.persistence.dto.TranveDto;
import translateit2.persistence.dto.WorkMapper;
import translateit2.persistence.model.Loco;
import translateit2.persistence.model.Transu;
import translateit2.persistence.model2.Loco2;
import translateit2.persistence.model2.Project;
import translateit2.persistence.model2.Transu2;
import translateit2.persistence.model2.Tranve;

@Service
public class WorkServiceImpl implements WorkService{
	@Autowired
    private Validator validator;
		
	@Autowired
    private WorkMapper modelMapper;
	
	@Autowired
    private Loco2Repository loco2Repo;
	
	@Autowired
    private TranveRepository tranveRepo;

	@Autowired
    private ProjectRepository projectRepo;
	
	@Override
	public ProjectDto createProjectDto(@Valid final ProjectDto entity) {
		Project perProject = projectRepo.save(convertToEntity(entity));
		return convertToDto(perProject); 
	}
	
    @Override
	public ProjectDto updateProjectDto(@Valid final ProjectDto projectDto) {
    	Project perProject = getProjectById(projectDto.getId());    	
    	convertToEntity(projectDto,perProject);
    	return convertToDto(projectRepo.save(perProject));
    }
	
	@Override
	public ProjectDto getProjectDtoById(final long id) {
        return convertToDto(projectRepo.findOne(id));
    }
    
	@Override
	public ProjectDto getProjectDtoByProjectName(String projectName) {
	    	Optional <Project> project = projectRepo.findByProjectName(projectName);
			return convertToDto(project.get());	
	}
	
	@Override
	public ProjectDto removeProjectDto(final ProjectDto entity) {
    	projectRepo.delete(convertToEntity(entity));
    	return null;
	}
	
	@Override
	public List<ProjectDto> listAllProjectDtos() {
		List<ProjectDto> projectDtos = new ArrayList<ProjectDto>(); 		
		projectRepo.findAll().forEach(l->projectDtos.add(convertToDto(l)));		
		
		return projectDtos.stream()
				.collect(Collectors.toList());
		
    }
/**
 *     PROJECT ends
 */

	@Override
	public Loco2Dto getLocoDtoById(final long id) {
        return convertToDto(loco2Repo.findOne(id));
    }
    @Override
	public Loco2Dto updateLocoDto(@Valid final Loco2Dto loco2Dto) {
		Loco2 perLoco = getLocoById(loco2Dto.getId());    	
    	convertToEntity(loco2Dto,perLoco);
    	perLoco.getTransus().forEach(t->t.setWork (perLoco));
    	return convertToDto(loco2Repo.save(perLoco));
    }
        
	@Override
	public Loco2Dto createLocoDto(@Valid final Loco2Dto entity) {
		Loco2 perLoco = convertToEntity(entity);
		perLoco.setTranve(getTranveById(entity.getTranve()));
		perLoco = loco2Repo.save(perLoco);
		return convertToDto(perLoco); 
	}

	/**
	 * TRANVE starts
	 */
	@Override
	public TranveDto createTranveDto(@Valid final TranveDto entity) {
		Tranve perTranve = convertToEntity(entity);
		perTranve.setProject(getProjectById(entity.getProject()));		
		perTranve = tranveRepo.save(perTranve);
		return convertToDto(perTranve); 
	}

	@Override
	public TranveDto createTransuDto(@Valid TransuDto2 transuDto2, final TranveDto tranveDto){
		long tranveId= tranveDto.getId();
		Tranve perTranve = getTranveById(tranveId);
		
		Transu2 curTransu = convertToEntity(transuDto2);
    	curTransu.setWork (perTranve);
    	perTranve = tranveRepo.save(perTranve);
    	return convertToDto(perTranve);
    }

	@Override
	public TranveDto removeTranveDto(final TranveDto entity) {
    	tranveRepo.delete(convertToEntity(entity));
    	return null;
	}
	
    @SuppressWarnings("unchecked")
	@Override
	public List<TransuDto2> listAllTransuDtos(final TranveDto tranveDto) {
    	if (tranveDto == null)
    		return Collections.EMPTY_LIST;
    	Tranve perTranve = getTranveById(tranveDto.getId());
	    Set <Transu2> transus = perTranve.getTransus();
		List<TransuDto2> transuDtos = new ArrayList<TransuDto2>(); 
		for (Transu2 t : transus){
			transuDtos.add(convertToDto(t));
		}
		
		return transuDtos.stream().sorted((t1, t2) 
				-> Integer.compare(t1.getRowId(),t2.getRowId()))
				.collect(Collectors.toList());		
    }
    
    @SuppressWarnings("unchecked")
	@Override
	public List<TranveDto> listAllTranveDtos() {
		List<TranveDto> tranveDtos = new ArrayList<TranveDto>(); 		
		tranveRepo.findAll().forEach(l->tranveDtos.add(convertToDto(l)));		
		
		return tranveDtos.stream()
				.collect(Collectors.toList());
		
		/*return tranveDtos.stream().sorted((t1, t2) 
				-> t1.getProjectName().compareTo(t2.getProjectName()))
				.collect(Collectors.toList());*/
		
    }
	/*
	 * TRANVE ends
	 */
	
    private Loco2Dto convertToDto(Loco2 loco2) {
    	if (loco2 == null) return null;
        Loco2Dto loco2Dto = modelMapper.map(loco2, Loco2Dto.class);
        return loco2Dto;
    }
    
    /*
	@Override
	public Loco2Dto getLocoDtoByProjectName(String projectName) {
	    	Optional <Loco2> loco2 = loco2Repo.findByProjectName(projectName);
			return convertToDto(loco2.get());	
	}
	*/
	
	@Override
	public Loco2Dto removeLocoDto(final Loco2Dto entity) {
    	loco2Repo.delete(convertToEntity(entity));
    	return null;
	}
	

    @SuppressWarnings("unchecked")
	@Override
	public List<Loco2Dto> listAllLocoDtos() {
		List<Loco2Dto> loco2Dtos = new ArrayList<Loco2Dto>(); 		
		loco2Repo.findAll().forEach(l->loco2Dtos.add(convertToDto(l)));		
		
		return loco2Dtos.stream()
				.collect(Collectors.toList());		
		/*
		return loco2Dtos.stream().sorted((t1, t2) 
				-> t1.getProjectName().compareTo(t2.getProjectName()))
				.collect(Collectors.toList());
		*/
    }

    @Override
	public List<Loco2Dto> listInOrderAllLocoDtos() {
    	List<Loco2Dto> locoDtos = new ArrayList<Loco2Dto>();
    	loco2Repo.findByOrderByIdAsc().forEach(l->locoDtos.add(convertToDto(l)));
    	return locoDtos;
    }
    
	@Override
	public Loco2Dto createTransuDto(@Valid TransuDto2 transuDto2, final long locoId){
		Loco2 perLoco = getLocoById(locoId);
		
		Transu2 curTransu = convertToEntity(transuDto2);
    	curTransu.setWork (perLoco);
    	perLoco = loco2Repo.save(perLoco);
    	return convertToDto(perLoco);
    }
	
    @SuppressWarnings("unchecked")
	@Override
	public List<TransuDto2> listAllTransuDtos(final Loco2Dto loco2Dto) {
		Loco2 perLoco = getLocoById(loco2Dto.getId());
	    Set <Transu2> transus = perLoco.getTransus();
		List<TransuDto2> transuDtos = new ArrayList<TransuDto2>(); 
		for (Transu2 t : transus){
			transuDtos.add(convertToDto(t));
		}
		
		return transuDtos.stream().sorted((t1, t2) 
				-> Integer.compare(t1.getRowId(),t2.getRowId()))
				.collect(Collectors.toList());		
    }
    
    @Override
    public TransuDto2 getTransuDtoByRowId(int rowId, final long locoId) {
		Loco2 perLoco = getLocoById(locoId);
	    Set <Transu2> transus = perLoco.getTransus();
	    return convertToDto(transus.stream().filter(t->rowId == t.getRowId())
	    		.findAny().orElse(null));
    }
    
    @Override
	public Loco2Dto updateTransuDto(@Valid TransuDto2 transuDto2){
    	Loco2 perLoco = getLocoById(transuDto2.getWork());
    	
    	// destination null
    	Transu2 curTransu = perLoco.getTransuByRowId(transuDto2.getRowId()); // Id() ??
    	convertToEntity(transuDto2,curTransu);
    	
    	perLoco=loco2Repo.save(perLoco);
    	return convertToDto(perLoco);
    }
    
    @Override
	public Loco2Dto removeTransuDto(TransuDto2 transuDto2){
    	Loco2 perLoco = getLocoById(transuDto2.getWork()); //
    	Transu2 curTransu = perLoco.getTransuByRowId(transuDto2.getRowId());
    	curTransu.setWork(null); curTransu = null;
    	perLoco=loco2Repo.save(perLoco);
    	return convertToDto(perLoco);
    }
    /*
     * 	Private methods
     */
    
	private Project getProjectById(final long id) {
        return projectRepo.findOne(id);
    }
	
	private Tranve getTranveById(final long id) {
        return tranveRepo.findOne(id);
    }
	
	private Loco2 getLocoById(final long id) {
        return loco2Repo.findOne(id);
    }
	
	private Loco2 convertToEntity(Loco2Dto loco2Dto) {
        Loco2 loco2 = modelMapper.map(loco2Dto, Loco2.class);      
        return loco2;
    }
	
    private void convertToEntity(Loco2Dto loco2Dto,Loco2 loco2) {
        modelMapper.map(loco2Dto, loco2);              
    }
    
    private TransuDto2 convertToDto(Transu2 transu2) {
    	if (transu2 == null) return null;
        TransuDto2 transuDto2 = modelMapper.map(transu2, TransuDto2.class);
        return transuDto2;
    }
    
    private Transu2 convertToEntity(TransuDto2 transuDto2) {
        Transu2 transu2 = modelMapper.map(transuDto2, Transu2.class);      
        return transu2;
    } 
    
    private void convertToEntity(TransuDto2 transuDto2,Transu2 transu2) {
        modelMapper.map(transuDto2, transu2);              
    }
    
    private Tranve convertToEntity(TranveDto tranveDto) {
        Tranve tranve = modelMapper.map(tranveDto, Tranve.class);      
        return tranve;
    } 

    private TranveDto convertToDto(Tranve tranve) {
    	if (tranve == null) return null;
        TranveDto tranveDto = modelMapper.map(tranve, TranveDto.class);
        return tranveDto;
    }

    private void convertToEntity(TranveDto tranveDto,Tranve tranve) {
        modelMapper.map(tranveDto, tranve);              
    }
    
    
    
    private Project convertToEntity(ProjectDto projectDto) {
    	Project project = modelMapper.map(projectDto, Project.class);      
        return project;
    } 

    private ProjectDto convertToDto(Project project) {
    	if (project == null) return null;
    	ProjectDto projectDto = modelMapper.map(project, ProjectDto.class);
        return projectDto;
    }

    private void convertToEntity(ProjectDto projectDto,Project project) {
        modelMapper.map(projectDto, project);              
    }
}
