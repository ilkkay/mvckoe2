package translateit2.service;

import java.util.List;

import translateit2.persistence.dto.LocoDto;
import translateit2.persistence.dto.TransuDto;

public interface LocoService {
    public LocoDto createLocoDto(final LocoDto entity);
    public LocoDto updateLocoDto();
    public List<LocoDto> listAllLocoDtos();
    public LocoDto getLocoDtoById(final long id);
    public LocoDto getLocoDtoByProjectName(String projectName);
    
    public void createTransuDto(TransuDto transuDto);
    public void updateTransuDto(TransuDto transuDto);
    public void removeTransuDto(TransuDto transuDto);    
    public List<TransuDto> listAllTransuDtos();
    public TransuDto getTransuDtoByRowId(int rowId);
}
