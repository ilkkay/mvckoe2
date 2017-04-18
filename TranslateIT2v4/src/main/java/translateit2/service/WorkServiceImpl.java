package translateit2.service;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;

import javax.validation.Valid;
import javax.validation.Validator;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.EnableTransactionManagement;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.Assert;

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

@EnableTransactionManagement
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

	/*
	 * PROJECT services start 
	 */
	@Override
	public ProjectDto createProjectDto(@Valid final ProjectDto entity) {
		Project perProject = projectRepo.save(convertToEntity(entity));
		return convertToDto(perProject); 
	}

	@Transactional
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
	
	@Transactional
	@Override
	public ProjectDto removeProjectDto(final long projectId) {
		if (projectRepo.exists(projectId)){
			List <Tranve> allTranves = (List<Tranve>) tranveRepo.findByProjectId(projectId);
			allTranves.stream().forEach(t->removeAllLocoDtos(t.getId()));
			tranveRepo.delete(allTranves);
			projectRepo.delete(getProjectById(projectId));
			return null; // not like this, but what else ??
		}
		else
			throw new IllegalArgumentException("Could not remove project. Id = " + projectId); 
	}
	
	@Transactional
	@Override
	public void removeAllProjectDtos() {
		List <Project> projects = (List<Project>) projectRepo.findAll();
		projects.stream().forEach(prj ->removeProjectDto(prj.getId()));
		// or should we delete using a list ??
	}
	
	@Override
	public List<ProjectDto> listAllProjectDtos(final long personId) {
		List<ProjectDto> projectDtos = new ArrayList<ProjectDto>(); 		
		projectRepo.findAll().forEach(l->projectDtos.add(convertToDto(l)));		
		return projectDtos.stream()
				.collect(Collectors.toList());		
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

	/**
	 * TRANVE starts
	 */
	@Transactional
	@Override
	public TranveDto createTranveDto(@Valid final TranveDto entity) {
		Tranve perTranve = convertToEntity(entity);
		perTranve.setProject(getProjectById(entity.getProjectId()));		
		perTranve = tranveRepo.save(perTranve);
		return convertToDto(perTranve); 
	}

	@Transactional
	@Override
	public TranveDto updateTranveDto(@Valid final TranveDto tranveDto) {
		Tranve perTranve = getTranveById(tranveDto.getId());    	
    	convertToEntity(tranveDto,perTranve);
    	perTranve.getTransus().forEach(t->t.setWork (perTranve));
    	return convertToDto(tranveRepo.save(perTranve));
    }

	@Transactional
	@Override
	public TranveDto createTranveTransuDtos(@Valid List<TransuDto2> transuDtos, final long tranveId){
		Tranve perTranve = getTranveById(tranveId);	
		for (TransuDto2 transuDto2 : transuDtos) {
			Transu2 curTransu = convertToEntity(transuDto2);
			curTransu.setWork (perTranve);
		}
    	perTranve = tranveRepo.save(perTranve);
    	return convertToDto(perTranve);
    }
	
	@Transactional
	@Override
	public TranveDto removeTranveDto(final long tranveId) {
		if (tranveRepo.exists(tranveId)) {
			removeAllLocoDtos(tranveId);
			tranveRepo.delete(tranveId);
			return null;
		}
		else
			throw new IllegalArgumentException("Could not remove translatable version. Id = " + tranveId); 			
	}
	
	@Override
	public TranveDto getTranveDtoById(final long id) {
        return convertToDto(tranveRepo.findOne(id));
    }
	
	@SuppressWarnings("unchecked")
	@Override
	public List<TransuDto2> listAllTranveTransuDtos(final long tranveId ) {
		if (!tranveRepo.exists(tranveId))
			throw new IllegalArgumentException("Could not remove translatable version. Id = " + tranveId); 
		else {
			Tranve perTranve = getTranveById(tranveId);
			Set <Transu2> transus = perTranve.getTransus();
			List<TransuDto2> transuDtos = new ArrayList<TransuDto2>(); 
			for (Transu2 t : transus)transuDtos.add(convertToDto(t));
			return transuDtos.stream().sorted((t1, t2) 
					-> Integer.compare(t1.getRowId(),t2.getRowId()))
					.collect(Collectors.toList());
		}
	}
    
    @SuppressWarnings("unchecked")
	@Override
	public List<TranveDto> listAllTranveDtos(final long projectId) {
		List<TranveDto> tranveDtos = new ArrayList<TranveDto>(); 		
		tranveRepo.findByProjectId(projectId).forEach(l->tranveDtos.add(convertToDto(l)));		
		return tranveDtos.stream().sorted((t1, t2) 
				-> t1.getVersion().compareTo(t2.getVersion()))
				.collect(Collectors.toList());
		
    }
	/*
	 * TRANVE ends
	 */

	/*
	 * LOCO starts
	 */
	@Transactional
	@Override
	public Loco2Dto createLocoDto(@Valid final Loco2Dto entity) {
		Loco2 perLoco = convertToEntity(entity);
		perLoco.setTranve(getTranveById(entity.getTranveId()));
		perLoco = loco2Repo.save(perLoco);
		return convertToDto(perLoco); 
	}
	
	@Transactional
	@Override
	public Loco2Dto updateLocoDto(@Valid final Loco2Dto loco2Dto) {
		Loco2 perLoco = getLocoById(loco2Dto.getId());    	
    	convertToEntity(loco2Dto,perLoco);
    	perLoco.getTransus().forEach(t->t.setWork (perLoco));
    	return convertToDto(loco2Repo.save(perLoco));
    }
	
	@Override
	public Loco2Dto getLocoDtoById(final long id) {
        return convertToDto(loco2Repo.findOne(id));
    }
	
	@Override
	public Loco2Dto removeLocoDto(final long workId) {
		if (loco2Repo.exists(workId)) {
			loco2Repo.delete(workId);
			return null;
		}
		else
			throw new IllegalArgumentException("Could not remove localized object. Id = " + workId); 			

	}
	
	@SuppressWarnings("unchecked")
	@Transactional
	@Override
	public void removeAllLocoDtos(final long tranveId) {
		List <Loco2> locos = new ArrayList<Loco2>();
		loco2Repo.findByTranveId(tranveId).forEach(l->((List)locos).add(l));
		loco2Repo.delete(locos);		
		// OR like this ??
		// loco2Repo.findByTranveId(tranveId).forEach(l->loco2Repo.delete(l));		
	}

    @SuppressWarnings("unchecked")
	@Override
	public List<Loco2Dto> listAllLocoDtos(final long tranveId) {
		List<Loco2Dto> loco2Dtos = new ArrayList<Loco2Dto>(); 				
		loco2Repo.findByTranveId(tranveId).forEach(l->loco2Dtos.add(convertToDto(l)));		
		
		// this is strange but there no other to use for sorting
		return loco2Dtos.stream().sorted((l1, l2) 
				//->Long.compare(t1.getTranveId(),t2.getTranveId()))
				-> l1.getVersion().compareTo(l2.getVersion()))
				.collect(Collectors.toList());
    }

    @Override
	public List<Loco2Dto> listAllLocoDtosOrderByTranslator(final long tranveId) {
    	List<Loco2Dto> locoDtos = new ArrayList<Loco2Dto>();
    	loco2Repo.findByTranveId(tranveId).forEach(l->locoDtos.add(convertToDto(l)));
    	return locoDtos.stream().sorted((l1, l2) 
				-> l1.getTranslator().compareTo(l2.getTranslator()))
				.collect(Collectors.toList());
    }
    
	@Transactional
	@Override
	public Loco2Dto createLocoTransuDtos(@Valid List<TransuDto2> transuDtos, final long locoId){
		Loco2 perLoco = getLocoById(locoId);	
		for (TransuDto2 transuDto2 : transuDtos) {
			Transu2 curTransu = convertToEntity(transuDto2);
			curTransu.setWork (perLoco);
		}
    	perLoco = loco2Repo.save(perLoco);
    	return convertToDto(perLoco);
    }
	
	
    @SuppressWarnings("unchecked")
	@Override
	public List<TransuDto2> listAllLocoTransuDtos(final long locoId) {
		Loco2 perLoco = getLocoById(locoId);
	    Set <Transu2> transus = perLoco.getTransus();
		List<TransuDto2> transuDtos = new ArrayList<TransuDto2>(); 
		for (Transu2 t : transus){ transuDtos.add(convertToDto(t)); }		
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
   
	@Transactional
	@Override
	public Loco2Dto updateLocoTransuDtos(@Valid List<TransuDto2> transuDtos) {
		if (transuDtos.size()>0) {
			Loco2 perLoco = getLocoById(transuDtos.get(0).getWorkId());	
			for (TransuDto2 transuDto2 : transuDtos) {
		    	Transu2 curTransu = perLoco.getTransuByRowId(transuDto2.getRowId()); // Id() ??
		    	convertToEntity(transuDto2,curTransu);
			}
			perLoco = loco2Repo.save(perLoco);
			return convertToDto(perLoco);
		}
		else
			throw new IllegalArgumentException("No row(s) found for update");		
    }
	
    @Transactional 
    @Override
	public Loco2Dto removeTransuDto(TransuDto2 transuDto2){
    	Loco2 perLoco = getLocoById(transuDto2.getWorkId()); //
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
		
    private Loco2Dto convertToDto(Loco2 loco2) {
    	if (loco2 == null) return null;
        Loco2Dto loco2Dto = modelMapper.map(loco2, Loco2Dto.class);
        return loco2Dto;
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
    
    /*
     * make translatable pairs to be used in the view layer
     */
    public HashMap <String,String> listTranslatebleTuples(long sourceCode, long targetCode) {
    	HashMap <String,String> translatableTuples = new LinkedHashMap<String, String>();
  
      	List <Object[]> tuples =  (List<Object[]>) loco2Repo.listTranslatableTuples(     
    			new Long(sourceCode), new Long(targetCode));    	    
    	for (int i=0; i <tuples.size(); i++) {
          	Object[] pair = tuples.get(i);
    		translatableTuples.put((String)pair[0], (String)pair[1]);
    	}
 	
    	return translatableTuples;
    }
}
