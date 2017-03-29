package translateit2.service;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;
import javax.transaction.Transactional;

import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import translateit2.persistence.dao.LocoRepository;
import translateit2.persistence.dto.LocoDto;
import translateit2.persistence.dto.TransuDto;
import translateit2.persistence.model.Loco;
import translateit2.persistence.model.Transu;

@Service
@Transactional // we don't need this annotation <= CrudRepository
public class LocoServiceImpl implements LocoService{	
	private Loco perLoco;
	
    public Loco getPerLoco() {
		return perLoco;
	}
	public void setPerLoco(Loco perLoco) {
		this.perLoco = perLoco;
	}
	
	@Autowired
    private LocoRepository locoRepo;
    // private PagingLocoRepository locoRepo;
    
    public LocoRepository getLocoRepo() {
		return locoRepo;
	}
	public void setLocoRepo(LocoRepository locoRepo) {
		this.locoRepo = locoRepo;
	}
	
	@Autowired
    private ModelMapper modelMapper;
    
    
	public LocoServiceImpl() {
        super();
    }

	public Loco updateLoco(final Loco entity) {
    	perLoco = locoRepo.save(entity);
    	return perLoco ;
    }
    @Override
	public LocoDto updateLocoDto() {
    	perLoco =  locoRepo.save(perLoco);
    	return convertToDto(perLoco);
    }
    
    
	public Loco createLoco(final Loco entity) {
    	perLoco = locoRepo.save(entity);
        return perLoco;
    }
	@Override
	public LocoDto createLocoDto(LocoDto entity) {
		// TODO Auto-generated method stub
    	perLoco = locoRepo.save(convertToEntity(entity));
		return convertToDto(perLoco); 
	}
	
	
	public Loco getLocoById(final long id) {
        return locoRepo.findOne(id);
    }
    @Override
	public LocoDto getLocoDtoById(final long id) {
        return convertToDto(locoRepo.findOne(id));
    }
    
	public Iterable<Loco> listAllLocos() {
        return locoRepo.findAll();
    }
	@Override
	public List<LocoDto> listAllLocoDtos() {
		List<LocoDto> locoDtos = new ArrayList<LocoDto>(); 
		for (Loco loco : locoRepo.findAll()){
			LocoDto dto = convertToDto(loco);
			locoDtos.add(dto);
		}	
		return locoDtos;
    }
        
	public Loco getLocoByProjectName(String projectName) {
        return locoRepo.findByProjectName(projectName).get();
    }
    
    @Override
	public LocoDto getLocoDtoByProjectName(String projectName){
    	Loco loco = locoRepo.findByProjectName(projectName).get();
    	//if (loco==null) return new NullLocoDto();
		return convertToDto(loco);
	
    }
    
    public Transu getTransuById(long Id){
		return perLoco.getTransus().
				stream().filter(t->Id==t.getId())
				.findAny()									
				.orElse(null);								
	}
    
    @Override
	public TransuDto getTransuDtoByRowId(int rowId){
    		return copyTransuToDto(perLoco.getTransus().
    				stream().filter(t->rowId==t.getRowId())
    				.findAny()									
    				.orElse(null));								
    	}
    
	public List<Transu> listAllTransus() {
	    Set <Transu> transus = perLoco.getTransus();
		List<Transu> transuList = new ArrayList<Transu>(); 
		for (Transu transu : transus){
			transuList.add(transu);
		}	
		return transuList;
    }
	
	private TransuDto copyTransuToDto(Transu t){
		TransuDto dto = new TransuDto();
		dto.setId(t.getId());
		dto.setLoco(0);
		dto.setRowId(t.getRowId());
		dto.setSourceSegm(t.getSourceSegm());
		dto.setTargetSegm(t.getTargetSegm());
		return dto;
	}
	
	@Override
	public List<TransuDto> listAllTransuDtos() {
	    Set <Transu> transus = perLoco.getTransus();
		List<TransuDto> transuDtos = new ArrayList<TransuDto>(); 
		for (Transu t : transus){
			transuDtos.add(copyTransuToDto(t));
		}	
		return transuDtos;
    }

	/*
    private AbstractLocoDto convertToDto(Loco loco) {
    	if (loco == null) return new NullLocoDto();
        LocoDto locoDto = modelMapper.map(loco, LocoDto.class);
        return locoDto;
    }
    */
	
    private LocoDto convertToDto(Loco loco) {
    	if (loco == null) return null;
        LocoDto locoDto = modelMapper.map(loco, LocoDto.class);
        return locoDto;
    }
    
    private Loco convertToEntity(LocoDto locoDto) {
        Loco loco = modelMapper.map(locoDto, Loco.class);
      
        if (locoDto.getId() != null) {
            Loco oldLoco = getLocoById(locoDto.getId());
        }
        return loco;
    }
    
    
    @Override
	public void updateTransuDto(TransuDto transuDto){
    	Transu curTransu = perLoco.getTransuByRowId(transuDto.getRowId());
    	curTransu.setRowId(transuDto.getRowId());
    	curTransu.setSourceSegm(transuDto.getSourceSegm());
    	curTransu.setTargetSegm(transuDto.getTargetSegm());
    	curTransu.setLoco (perLoco);
    }
    
    @Override
	public void createTransuDto(TransuDto transuDto){
    	Transu curTransu = new Transu(); //perLoco.getTransuByRowId(transuDto.getRowId());
    	curTransu.setRowId(transuDto.getRowId());
    	curTransu.setSourceSegm(transuDto.getSourceSegm());
    	curTransu.setTargetSegm(transuDto.getTargetSegm());
    	curTransu.setLoco (perLoco);
    }
    
    @Override
	public void removeTransuDto(TransuDto transuDto){
    	Transu curTransu = perLoco.getTransuByRowId(transuDto.getRowId());
    	curTransu.setLoco(null);
    	curTransu = null;
    }    
}
