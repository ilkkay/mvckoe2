package translateit2.service;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import translateit2.persistence.dao.LocoRepository;
import translateit2.persistence.dto.LocoDto;
import translateit2.persistence.dto.LocoMapper;
import translateit2.persistence.dto.TransuDto;
import translateit2.persistence.model.Loco;
import translateit2.persistence.model.Transu;

@Service
public class Loco2ServiceImpl implements Loco2Service{	

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
    
    @Override
	public List<LocoDto> listAllLocoDtos() {
		List<LocoDto> locoDtos = new ArrayList<LocoDto>(); 		
		locoRepo.findAll().forEach(l->locoDtos.add(convertToDto(l)));		
		return locoDtos;
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
    public TransuDto getTransuDtoByRowId(int rowId, final LocoDto locoDto) {
		Loco perLoco = getLocoById(locoDto.getId());
	    Set <Transu> transus = perLoco.getTransus();
	    return convertToDto(transus.stream().filter(t->rowId == t.getRowId())
	    		.findAny().orElse(null));
    }
	
    @Override
	public List<TransuDto> listAllTransuDtos(final LocoDto locoDto) {
		Loco perLoco = getLocoById(locoDto.getId());
	    Set <Transu> transus = perLoco.getTransus();
		List<TransuDto> transuDtos = new ArrayList<TransuDto>(); 
		for (Transu t : transus){
			transuDtos.add(convertToDto(t));
		}	
		return transuDtos;
    }
    
    @Override
	public LocoDto updateTransuDto(TransuDto transuDto){
    	Loco perLoco = getLocoById(transuDto.getLoco());
    	
    	Transu curTransu = perLoco.getTransuByRowId(transuDto.getRowId()); // Id() ??
    	// multiple representation of the same entity =>
    	curTransu.setLoco(null); curTransu = null;
    	Transu newTransu = convertToEntity(transuDto);
    	newTransu.setLoco(perLoco); 
    	
    	perLoco=locoRepo.save(perLoco);
    	return convertToDto(perLoco);
    }
    
	@Override
	public LocoDto createTransuDto(TransuDto transuDto, LocoDto locoDto){
		Loco perLoco = getLocoById(locoDto.getId());
		
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
}
