package translateit2.service;

import java.util.List;

import javax.validation.Valid;

import org.springframework.validation.annotation.Validated;

import translateit2.persistence.dto.UnitDto;
import translateit2.persistence.dto.WorkDto;

@Validated
public interface WorkService {
    /**
     * Unit
     */
    void createUnitDtos(@Valid List<UnitDto> unitDtos, final long workId);

    /**
     * Work
     */
    WorkDto createWorkDto(@Valid final WorkDto entity, String groupName);

    List<UnitDto> getPage(final long workId, int pageIndex, int pageSize);

    long getTranslatedLinesCount(final long workId);

    UnitDto getUnitDtoById(long unitId);

    long getUnitDtoCount(final long workId);

    WorkDto getWorkDtoById(long workId);

    long getWorkDtoCount(final long groupId);

    List<WorkDto> listProjectWorkDtos(final long projectId);

    List<UnitDto> listUnitDtos(final long workId);

    List<WorkDto> listWorkDtos(long groupId);

    void removeUnitDtos(final long workId);

    void removeWorkDto(final long workId);

    void removeWorkDtos(List<WorkDto> entities);

    void updateUnitDtos(@Valid List<UnitDto> unitDtos, final long workId);

    WorkDto updateWorkDto(@Valid final WorkDto entity);
}
