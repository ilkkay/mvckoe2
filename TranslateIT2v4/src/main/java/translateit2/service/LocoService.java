package translateit2.service;

import java.util.List;

import javax.validation.Valid;

import org.springframework.validation.annotation.Validated;

import translateit2.persistence.dto.LocoDto;
import translateit2.persistence.dto.TransuDto;

@Validated
public interface LocoService {
    public LocoDto createLocoDto(@Valid final LocoDto entity);
    public LocoDto updateLocoDto(@Valid final LocoDto entity);
    public void removeLocoDto(final LocoDto entity);
    public List<LocoDto> listAllLocoDtos();
    public List<LocoDto> listInOrderAllLocoDtos();
    public LocoDto getLocoDtoById(final long id);
    public LocoDto getLocoDtoByProjectName(String projectName);
    
    public LocoDto createTransuDto(@Valid TransuDto transuDto,final long locoId);
    public LocoDto updateTransuDto(@Valid TransuDto transuDto);
    public LocoDto removeTransuDto(TransuDto transuDto);    
    public List<TransuDto> listAllTransuDtos(final LocoDto entity);
    public TransuDto getTransuDtoByRowId(int rowId, final long locoId);
}
