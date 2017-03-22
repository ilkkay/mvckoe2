package translateit2.persistence.dao;

import org.springframework.data.repository.CrudRepository;
import translateit2.persistence.model.Transu;
// http://docs.spring.io/spring-data/jpa/docs/1.4.1.RELEASE/reference/html/jpa.repositories.html
public interface TransuRepository extends CrudRepository<Transu, Long> {
	Iterable<Transu> findByLocoId(Long idUser);
	Iterable<Transu> findByOrderByLocoIdAsc(Long idUser);
	Iterable<Transu> findByOrderByIdAsc();
	Transu findByLocoIdAndRowId(final long locoId,final Integer rowId);
}
