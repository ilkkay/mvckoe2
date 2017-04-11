package translateit2.persistence.dao;

import org.springframework.data.repository.CrudRepository;

import translateit2.persistence.model2.Transu2;
// http://docs.spring.io/spring-data/jpa/docs/1.4.1.RELEASE/reference/html/jpa.repositories.html
public interface Transu2Repository extends CrudRepository<Transu2, Long> {
	Iterable<Transu2> findByWorkId(Long idUser);
	Iterable<Transu2> findByOrderByWorkIdAsc(Long idUser);
	Iterable<Transu2> findByOrderByIdAsc();
	Transu2 findByWorkIdAndRowId(final long locoId,final Integer rowId);
}

