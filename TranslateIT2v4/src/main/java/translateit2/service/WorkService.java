package translateit2.service;

import java.util.HashMap;
import java.util.List;

import javax.validation.Valid;

import org.springframework.validation.annotation.Validated;

import translateit2.persistence.dto.Loco2Dto;
import translateit2.persistence.dto.ProjectDto;
import translateit2.persistence.dto.TransuDto2;
import translateit2.persistence.dto.TranveDto;

@Validated
public interface WorkService {
	/**
	 * Project 
	 */	
	public ProjectDto createProjectDto(@Valid final ProjectDto entity);
	public ProjectDto updateProjectDto(@Valid final ProjectDto projectDto);
	public ProjectDto removeProjectDto(final long projectId); 
	public void removeAllProjectDtos(); // TODO: what is the correct way to remove?
	public ProjectDto getProjectDtoById(long projectId);
	public List<ProjectDto> listAllProjectDtos(final long personId); 
	// TODO: personId not yet implemented
	
	/**
	 * Translatable version = tranve
	 */
	// TODO: test updateTranveDto(@Valid final TranveDto entity)
	public TranveDto createTranveDto(@Valid final TranveDto entity);
	public TranveDto updateTranveDto(@Valid final TranveDto entity);
	public TranveDto removeTranveDto(final long tranveId); 
	public TranveDto getTranveDtoById(final long tranveId);
	public List<TranveDto> listAllTranveDtos(final long projectId);
	
	 /* Translation units in tranve */  
	public TranveDto createTranveTransuDtos(@Valid List<TransuDto2> transuDto2, final long tranveId);
	public List<TransuDto2> listAllTranveTransuDtos(final long tranveId);
	//TODO: public List<TransuDto2> getTranveTransuDtoPage(final long start, final long end, final long tranveId);
	
	/**
	 * Localized object = loco
	 */
	public Loco2Dto createLocoDto(@Valid final Loco2Dto entity);
    public Loco2Dto updateLocoDto(@Valid final Loco2Dto entity);
    public Loco2Dto removeLocoDto(final long locoId);
    public Loco2Dto getLocoDtoById(final long locoId);
    public List<Loco2Dto> listAllLocoDtos(final long tranveId); 
    public List<Loco2Dto> listAllLocoDtosOrderByTranslator(final long tranveId); 
      
    /* Translation units in loco */
	public Loco2Dto createLocoTransuDtos(@Valid List<TransuDto2> transuDto2, final long locoId);
	public Loco2Dto updateLocoTransuDtos(@Valid List<TransuDto2> transuDto2);
	public List<TransuDto2> listAllLocoTransuDtos(final long locoId);
	//TODO: public List<TransuDto2> getLocoTransuDtoPage(final long start, final long end, final long tranveId);
	
	/* TODO: probably only for internal use? */
    public void removeAllLocoDtos(final long tranveId);
	public long getProjectDtoCount(final long userId); 

	/* TODO: probably only for testing ? */
    public HashMap <String,String> listTranslatebleTuples(long locoId, long tranveId); // TODO: do we need this
    public Loco2Dto removeTransuDto(TransuDto2 transuDto2); // TODO: do we need this
	public TransuDto2 getTransuDtoByRowId(int rowId, final long locoId); // TODO: do we need this
	public ProjectDto getProjectDtoByProjectName(String projectName); // TODO: do we need this
}
