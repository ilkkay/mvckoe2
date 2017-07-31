package translateit2.persistence.dao;

import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.data.rest.core.annotation.RepositoryRestResource;
import org.springframework.data.web.PageableDefault;

import translateit2.persistence.model.State;
import translateit2.persistence.model.Unit;

@RepositoryRestResource(collectionResourceRel = "unit", path = "unit")
public interface UnitRepository extends PagingAndSortingRepository<Unit, Long> {
    @Override
    List<Unit> findAll();

    Long countByWorkId(final long workId);

    @Query("select u from trUnit u where u.work.id = :workId")
    Page<Unit> getUnitsByWorkId(@Param("workId") Long workId, @PageableDefault(size = 10) Pageable pageabled);

    Page<Unit> findByWorkId(final long workId, @PageableDefault(size = 10) Pageable pageabled);

    @Query("select count(u.id) from trUnit u where u.work.id = :workId and u.target.state = :state")
    Long countStates(@Param("workId") Long workId, @Param("state") State state);

    Long countByWorkIdAndTargetState(final long workId, final State state);

}
