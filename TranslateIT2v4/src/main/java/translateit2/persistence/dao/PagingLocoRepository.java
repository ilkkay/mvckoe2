package translateit2.persistence.dao;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.data.web.PageableDefault;

import translateit2.persistence.model.Loco;
import translateit2.persistence.model.Transu;

public interface PagingLocoRepository extends PagingAndSortingRepository<Loco, Long> {
	Loco findByProjectName(String projectName);
	
	@Query("select t from Transu t where t.loco = :locoId")
	Page<Transu> getTransusByLocoId(@Param("locoId") long id, 
			@PageableDefault(size = 10) Pageable pageabled);
}
