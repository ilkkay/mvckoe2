package translateit2.service;

import java.util.List;

import translateit2.persistence.dto.LocoDto;
import translateit2.persistence.dto.TransuDto;
import translateit2.persistence.model.Loco;
import translateit2.persistence.model.Transu;

public interface LocoService {
    public LocoDto createLocoDto(final LocoDto entity);
    public LocoDto updateLocoDto();
    public List<LocoDto> listAllLocoDtos();
    public LocoDto getLocoDtoById(final long id);
    public LocoDto getLocoDtoByProjectName(String projectName);
    
    public void createTransuDto(TransuDto transuDto);
    public void updateTransuDto(TransuDto transuDto);
    public List<TransuDto> listAllTransuDtos();
    public TransuDto getTransuDtoByRowId(int rowId);
    public void removeTransuDto(TransuDto transuDto);    
}
