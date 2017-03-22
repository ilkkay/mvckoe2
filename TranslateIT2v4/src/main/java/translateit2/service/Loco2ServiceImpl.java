package translateit2.service;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

import javax.transaction.Transactional;

import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import translateit2.persistence.dao.LocoRepository;
import translateit2.persistence.dto.AbstractLocoDto;
import translateit2.persistence.dto.LocoDto;
import translateit2.persistence.dto.NullLocoDto;
import translateit2.persistence.dto.TransuDto;
import translateit2.persistence.model.Loco;
import translateit2.persistence.model.Transu;

@Service
@Transactional // we don't need this annotation <= CrudRepository
public class Loco2ServiceImpl implements Loco2Service{	

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
    
    
	public Loco2ServiceImpl() {
        super();
    }

	public Loco updateLoco(final Loco entity) {
    	return locoRepo.save(entity);
    }
    @Override
	public LocoDto updateLocoDto(final LocoDto locoDto) {
    	Loco perLoco = getLocoByProjectName(locoDto.getProjectName());
    	perLoco =  locoRepo.save(perLoco);
    	return convertToDto(perLoco);
    }
    
    
	public Loco createLoco(final Loco entity) {
        return locoRepo.save(entity);
    }
	@Override
	public LocoDto createLocoDto(final LocoDto entity) {
		// TODO Auto-generated method stub
    	Loco perLoco = locoRepo.save(convertToEntity(entity));
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
	public List<LocoDto> listAllLocoDtos() {
		List<LocoDto> locoDtos = new ArrayList<LocoDto>(); 
		for (Loco loco : locoRepo.findAll()){
			LocoDto dto = convertToDto(loco);
			locoDtos.add(dto);
		}	
		return locoDtos;
    }
        
	public Loco getLocoByProjectName(String projectName) {
        return locoRepo.findByProjectName(projectName);
    }
    
    public LocoDto getLocoDtoByProjectName(String projectName){
    	Loco loco = locoRepo.findByProjectName(projectName);
		return convertToDto(loco);
	
    }
    
    public Transu getTransuById(long Id, final LocoDto locoDto) {
		Loco perLoco = getLocoById(locoDto.getId());
		return perLoco.getTransus().
				stream().filter(t->Id==t.getId())
				.findAny()									
				.orElse(null);								
	}
    
    public TransuDto getTransuDtoByRowId(int rowIdLocoDto, final LocoDto locoDto) {
		Loco perLoco = getLocoById(locoDto.getId());
    		return copyTransuToDto(perLoco.getTransus().
    				stream().filter(t->rowIdLocoDto==t.getRowId())
    				.findAny()									
    				.orElse(null));								
    	}
    
	public List<Transu> listAllTransus(final LocoDto locoDto) {
		Loco perLoco = getLocoById(locoDto.getId());
	    Set <Transu> transus = perLoco.getTransus();
		List<Transu> transuList = new ArrayList<Transu>(); 
		for (Transu transu : transus){
			transuList.add(transu);
		}	
		return transuList;
    }
	
	
	public List<TransuDto> listAllTransuDtos(final LocoDto locoDto) {
		Loco perLoco = getLocoById(locoDto.getId());
	    Set <Transu> transus = perLoco.getTransus();
		List<TransuDto> transuDtos = new ArrayList<TransuDto>(); 
		for (Transu t : transus){
			transuDtos.add(copyTransuToDto(t));
		}	
		return transuDtos;
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
    
    private Transu convertToEntity(TransuDto transuDto) {
        Transu transu = modelMapper.map(transuDto, Transu.class);
      
        return transu;
    }
    
    @Override
	public void updateTransuDto(TransuDto transuDto){
    	Loco perLoco = getLocoById(transuDto.getLoco());
    	/*
    	Transu curTransu = perLoco.getTransuByRowId(transuDto.getRowId());
    	curTransu.setRowId(transuDto.getRowId());
    	curTransu.setSourceSegm(transuDto.getSourceSegm());
    	curTransu.setTargetSegm(transuDto.getTargetSegm());
    	*/
    	Transu curTransu = convertToEntity(transuDto);
    	
    	curTransu.setLoco (perLoco);
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

	/*
	private TransuDto copyDtoToTransu(TransuDto dto){
		Transu t = new Transu();
		t.setId(dto.getId());
		t.setRowId(dto.getRowId());
		t.setSourceSegm(dto.getSourceSegm());
		t.setTargetSegm(t.getTargetSegm());
		return dto;
	}
*/
	@Override
	public void createTransuDto(TransuDto transuDto, LocoDto locoDto){
		/*
    	Transu curTransu = new Transu(); 
    	curTransu.setRowId(transuDto.getRowId());
    	curTransu.setSourceSegm(transuDto.getSourceSegm());
    	curTransu.setTargetSegm(transuDto.getTargetSegm());
    	*/
		Loco perLoco = getLocoById(locoDto.getId());
    	Transu curTransu = convertToEntity(transuDto);
    	curTransu.setLoco (perLoco);
    }
    
    @Override
	public void removeTransuDto(TransuDto transuDto){
    	Loco perLoco = getLocoById(transuDto.getLoco()); //
    	Transu curTransu = perLoco.getTransuByRowId(transuDto.getRowId());
    	curTransu.setLoco(null);
    	curTransu = null;
    }    
}
