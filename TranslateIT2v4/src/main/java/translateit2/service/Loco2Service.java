package translateit2.service;

import java.util.List;

import translateit2.persistence.dto.LocoDto;
import translateit2.persistence.dto.TransuDto;

public interface Loco2Service {
    public LocoDto createLocoDto(final LocoDto entity);
    public LocoDto updateLocoDto(final LocoDto entity);
    public void removeLocoDto(final LocoDto entity);
    public List<LocoDto> listAllLocoDtos();
    public List<LocoDto> listInOrderAllLocoDtos();
    public LocoDto getLocoDtoById(final long id);
    public LocoDto getLocoDtoByProjectName(String projectName);
    
    public LocoDto createTransuDto(TransuDto transuDto,final long locoId);
    public LocoDto updateTransuDto(TransuDto transuDto);
    public List<TransuDto> listAllTransuDtos(final LocoDto entity);
    public TransuDto getTransuDtoByRowId(int rowId, final long locoId);
    public LocoDto removeTransuDto(TransuDto transuDto);    
}
