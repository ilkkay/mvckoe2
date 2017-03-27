package translateit2.service;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

import javax.annotation.PostConstruct;
import javax.validation.ConstraintViolation;
import javax.validation.ConstraintViolationException;
import javax.validation.Validator;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import translateit2.persistence.dao.LocoRepository;
import translateit2.persistence.dto.LocoDto;
import translateit2.persistence.dto.LocoMapper;
import translateit2.persistence.dto.TransuDto;
import translateit2.persistence.model.Loco;
import translateit2.persistence.model.Transu;
import translateit2.validator.LocoValidator;

@Service
public class Loco2ServiceImpl implements Loco2Service{	

	@Autowired
    private Validator validator;
	
	//@Autowired
    //private LocoValidator locoValidator;
	
	@Autowired
    private LocoMapper modelMapper;
	
	@Autowired
    private LocoRepository locoRepo;
    // private PagingLocoRepository locoRepo;
    
    public LocoRepository getLocoRepo() {
		return locoRepo;
	}
	public void setLocoRepo(LocoRepository locoRepo) {
		this.locoRepo = locoRepo;
	}	

	public Loco2ServiceImpl() {
        super();        
    }
	
    @Override
	public LocoDto updateLocoDto(final LocoDto locoDto) {
    	Loco perLoco = locoRepo.save(convertToEntity(locoDto));
    	return convertToDto(perLoco);
    }
    
	@Override
	public LocoDto createLocoDto(final LocoDto entity) {		
		Set<ConstraintViolation<LocoDto>> violations = validator.validate(entity);
		if (!violations.isEmpty()) {
			throw new ConstraintViolationException(
				    new HashSet<ConstraintViolation<?>>(violations));
		}
		
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
		
		return (List<LocoDto>) locoDtos.stream().sorted((t1, t2) 
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
    	Loco loco = locoRepo.findByProjectName(projectName);
		return convertToDto(loco);	
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
		
		return (List<TransuDto>) transuDtos.stream().sorted((t1, t2) 
				-> Integer.compare(t1.getRowId(),t2.getRowId()))
				.collect(Collectors.toList());		
    }
    
    @Override
	public LocoDto updateTransuDto(TransuDto transuDto){
    	Loco perLoco = getLocoById(transuDto.getLoco());
    	
    	// destination null
    	Transu curTransu = perLoco.getTransuByRowId(transuDto.getRowId()); // Id() ??
    	convertToEntity(transuDto,curTransu);
    	
    	perLoco=locoRepo.save(perLoco);
    	return convertToDto(perLoco);
    }
    
	@Override
	public LocoDto createTransuDto(TransuDto transuDto, final long locoId){
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
}
