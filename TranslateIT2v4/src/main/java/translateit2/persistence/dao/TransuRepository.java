package translateit2.persistence.dao;

import org.springframework.data.repository.CrudRepository;
import translateit2.persistence.model.Transu;

public interface TransuRepository extends CrudRepository<Transu, Long> {
	Iterable<Transu> findByLocoId(Long idUser);
	Iterable<Transu> findByOrderByLocoIdAsc(Long idUser);
	Iterable<Transu> findByOrderByIdAsc();
}
