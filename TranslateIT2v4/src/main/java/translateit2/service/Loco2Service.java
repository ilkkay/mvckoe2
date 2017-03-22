package translateit2.service;

import java.util.List;

import translateit2.persistence.dto.LocoDto;
import translateit2.persistence.dto.TransuDto;

public interface Loco2Service {
    public LocoDto createLocoDto(final LocoDto entity);
    public LocoDto updateLocoDto(final LocoDto entity);
    public List<LocoDto> listAllLocoDtos();
    public LocoDto getLocoDtoById(final long id);
    public LocoDto getLocoDtoByProjectName(String projectName);
    
    public void createTransuDto(TransuDto transuDto,LocoDto locoDto);
    public void updateTransuDto(TransuDto transuDto);
    public List<TransuDto> listAllTransuDtos(final LocoDto entity);
    public TransuDto getTransuDtoByRowId(int rowId, final LocoDto locoDto);
    public void removeTransuDto(TransuDto transuDto);    
}
