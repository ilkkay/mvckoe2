package translateit2.service;

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
	// TODO: listAllTranveDtos(final long projectId)
	public ProjectDto createProjectDto(@Valid final ProjectDto entity);
	public ProjectDto updateProjectDto(@Valid final ProjectDto projectDto);
	public ProjectDto removeProjectDto(@Valid final ProjectDto projectDto); // TODO: remove all dependencies
	public ProjectDto getProjectDtoById(long id);
	public List<ProjectDto> listAllProjectDtos(); // TODO: final long personId
	
	/**
	 * Translatable version = tranve
	 */
	// TODO: updateTranveDto(@Valid final TranveDto entity)
	// TODO: listAllTranveDtos(final long tranveId)
	public TranveDto createTranveDto(@Valid final TranveDto entity);
	public TranveDto removeTranveDto(final TranveDto entity); // TODO: exceptions?
	public List<TranveDto> listAllTranveDtos(); // TODO: final long projectId

	 /* Translation units in tranve */  
	public TranveDto createTransuDto(@Valid TransuDto2 transuDto2, final TranveDto tranveDto);
	public List<TransuDto2> listAllTransuDtos(final TranveDto tranveDto);

	/**
	 * Localized object = loco
	 */
	public Loco2Dto createLocoDto(@Valid final Loco2Dto entity);
    public Loco2Dto updateLocoDto(@Valid final Loco2Dto entity);
    public Loco2Dto removeLocoDto(final Loco2Dto entity);
    public Loco2Dto getLocoDtoById(final long id);
    public List<Loco2Dto> listAllLocoDtos(); //  TODO: final long tranveId
    public List<Loco2Dto> listInOrderAllLocoDtos(); //  TODO: final long tranveId
    
    /* Translation units in loco */
	public Loco2Dto createTransuDto(@Valid TransuDto2 transuDto2, final long locoId);
	public Loco2Dto updateTransuDto(@Valid TransuDto2 transuDto2);
	public List<TransuDto2> listAllTransuDtos(final Loco2Dto loco2Dto);
	
	/* TODO: are the next ones used only for testing or internally? */
	public Loco2Dto removeTransuDto(TransuDto2 transuDto2); // TODO: do we need this
	public TransuDto2 getTransuDtoByRowId(int rowId, final long locoId); // TODO: do we need this
	public ProjectDto getProjectDtoByProjectName(String projectName); // TODO: do we need this
}
