package translateit2.service;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;

import javax.validation.ConstraintViolation;
import javax.validation.ConstraintViolationException;
import javax.validation.Valid;
import javax.validation.Validator;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.validation.annotation.Validated;

import translateit2.persistence.dao.LocoRepository;
import translateit2.persistence.dto.LocoDto;
import translateit2.persistence.dto.LocoMapper;
import translateit2.persistence.dto.TransuDto;
import translateit2.persistence.model.Loco;
import translateit2.persistence.model.Transu;
import translateit2.validator.LocoValidator;

@Validated
@Service
public class LocoServiceImpl implements LocoService{	

	@Autowired
    private Validator validator;
		
	@Autowired
    private LocoMapper modelMapper;
	
	@Autowired
    private LocoRepository locoRepo;
    // private PagingLocoRepository locoRepo;
/*    
    public LocoRepository getLocoRepo() {
		return locoRepo;
	}
	public void setLocoRepo(LocoRepository locoRepo) {
		this.locoRepo = locoRepo;
	}	
*/
	public LocoServiceImpl() {
        super();        
    }
	
    @Override
	public LocoDto updateLocoDto(@Valid final LocoDto locoDto) {
    	/*
		Set<ConstraintViolation<LocoDto>> violations = validator.validate(locoDto);
		if (!violations.isEmpty()) {
			throw new ConstraintViolationException(
				    new HashSet<ConstraintViolation<?>>(violations));
		}
		*/
		// TODO: problem with perloco.transus.loco is null when it should be perloco  	
    	
		Loco perLoco = getLocoById(locoDto.getId());    	
    	convertToEntity(locoDto,perLoco);
    	perLoco.getTransus().forEach(t->t.setLoco (perLoco));
    	return convertToDto(locoRepo.save(perLoco));
    }
    
	@Override
	public LocoDto createLocoDto(@Valid final LocoDto entity) {
		Loco perLoco = locoRepo.save(convertToEntity(entity));
		return convertToDto(perLoco); 
	}
	
	@Override
	public void removeLocoDto(final LocoDto entity) {
    	locoRepo.delete(convertToEntity(entity));
	}
	
    @Override
	public LocoDto getLocoDtoById(final long id) {
        return convertToDto(locoRepo.findOne(id));
    }
    
    @SuppressWarnings("unchecked")
	@Override
	public List<LocoDto> listAllLocoDtos() {
		List<LocoDto> locoDtos = new ArrayList<LocoDto>(); 		
		locoRepo.findAll().forEach(l->locoDtos.add(convertToDto(l)));		
		
		return locoDtos.stream().sorted((t1, t2) 
				-> t1.getProjectName().compareTo(t2.getProjectName()))
				.collect(Collectors.toList());
    }
    @Override
	public List<LocoDto> listInOrderAllLocoDtos() {
    	List<LocoDto> locoDtos = new ArrayList<LocoDto>();
    	locoRepo.findByOrderByIdAsc().forEach(l->locoDtos.add(convertToDto(l)));
    	return locoDtos;
    }
    
    @Override
    public LocoDto getLocoDtoByProjectName(String projectName){
    	Optional <Loco> loco = locoRepo.findByProjectName(projectName);
		return convertToDto(loco.get());	
    }
    
    @Override
    public TransuDto getTransuDtoByRowId(int rowId, final long locoId) {
		//Loco perLoco = getLocoById(locoDto.getId());
		Loco perLoco = getLocoById(locoId);
	    Set <Transu> transus = perLoco.getTransus();
	    return convertToDto(transus.stream().filter(t->rowId == t.getRowId())
	    		.findAny().orElse(null));
    }
	
    @SuppressWarnings("unchecked")
	@Override
	public List<TransuDto> listAllTransuDtos(final LocoDto locoDto) {
		Loco perLoco = getLocoById(locoDto.getId());
	    Set <Transu> transus = perLoco.getTransus();
		List<TransuDto> transuDtos = new ArrayList<TransuDto>(); 
		for (Transu t : transus){
			transuDtos.add(convertToDto(t));
		}
		
		return transuDtos.stream().sorted((t1, t2) 
				-> Integer.compare(t1.getRowId(),t2.getRowId()))
				.collect(Collectors.toList());		
    }
    
    @Override
	public LocoDto updateTransuDto(@Valid TransuDto transuDto){
    	/*
		Set<ConstraintViolation<TransuDto>> violations = validator.validate(transuDto);
		if (!violations.isEmpty()) {
			throw new ConstraintViolationException(
				    new HashSet<ConstraintViolation<?>>(violations));
		}
		*/
    	Loco perLoco = getLocoById(transuDto.getLoco());
    	
    	// destination null
    	Transu curTransu = perLoco.getTransuByRowId(transuDto.getRowId()); // Id() ??
    	convertToEntity(transuDto,curTransu);
    	
    	perLoco=locoRepo.save(perLoco);
    	return convertToDto(perLoco);
    }
    
	@Override
	public LocoDto createTransuDto(@Valid TransuDto transuDto, final long locoId){
		/*
		Set<ConstraintViolation<TransuDto>> violations = validator.validate(transuDto);
		if (!violations.isEmpty()) {
			throw new ConstraintViolationException(
				    new HashSet<ConstraintViolation<?>>(violations));
		}
		*/
		Loco perLoco = getLocoById(locoId);
		
		Transu curTransu = convertToEntity(transuDto);
    	curTransu.setLoco (perLoco);
    	perLoco = locoRepo.save(perLoco);
    	return convertToDto(perLoco);
    }
    
    @Override
	public LocoDto removeTransuDto(TransuDto transuDto){
    	Loco perLoco = getLocoById(transuDto.getLoco()); //
    	Transu curTransu = perLoco.getTransuByRowId(transuDto.getRowId());
    	curTransu.setLoco(null); curTransu = null;
    	perLoco=locoRepo.save(perLoco);
    	return convertToDto(perLoco);
    }
    
    /*
     * 	Private methods
     */
    
	private Loco getLocoById(final long id) {
        return locoRepo.findOne(id);
    }

    private LocoDto convertToDto(Loco loco) {
    	if (loco == null) return null;
        LocoDto locoDto = modelMapper.map(loco, LocoDto.class);
        return locoDto;
    }
    
    private Loco convertToEntity(LocoDto locoDto) {
        Loco loco = modelMapper.map(locoDto, Loco.class);      
        return loco;
    }
    
    private TransuDto convertToDto(Transu transu) {
    	if (transu == null) return null;
        TransuDto transuDto = modelMapper.map(transu, TransuDto.class);
        return transuDto;
    }
    
    private Transu convertToEntity(TransuDto transuDto) {
        Transu transu = modelMapper.map(transuDto, Transu.class);      
        return transu;
    } 
    
    private void convertToEntity(TransuDto transuDto,Transu transu) {
        modelMapper.map(transuDto, transu);              
    }
    
    private void convertToEntity(LocoDto locoDto,Loco loco) {
        modelMapper.map(locoDto, loco);              
    }
}
