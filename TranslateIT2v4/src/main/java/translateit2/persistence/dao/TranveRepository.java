package translateit2.persistence.dao;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.Modifying;
import org.springframework.transaction.annotation.Transactional;

import translateit2.persistence.model.Loco;
import translateit2.persistence.model2.Loco2;
import translateit2.persistence.model2.Tranve;

@Transactional
public interface TranveRepository extends WorkBaseRepository<Tranve> {
	@Transactional(readOnly=true) 
	Iterable<Tranve> findByProjectId(long projectId);
}
