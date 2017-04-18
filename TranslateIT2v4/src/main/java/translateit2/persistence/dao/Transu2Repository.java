package translateit2.persistence.dao;

import java.util.HashMap;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.transaction.annotation.Transactional;

import translateit2.persistence.model2.Transu2;
// http://docs.spring.io/spring-data/jpa/docs/1.4.1.RELEASE/reference/html/jpa.repositories.html

@Transactional(readOnly=true)
public interface Transu2Repository extends PagingAndSortingRepository<Transu2,Long> {
	Iterable<Transu2> findByWorkId(Long workId);
	Iterable<Transu2> findByOrderByWorkIdAsc(Long workId);
	Iterable<Transu2> findByOrderByIdAsc();
	Transu2 findByWorkIdAndRowId(final long workId,final Integer rowId);
}

/*
public interface Transu2Repository extends CrudRepository<Transu2, Long> {
	Iterable<Transu2> findByWorkId(Long workId);
	Iterable<Transu2> findByOrderByWorkIdAsc(Long workId);
	Iterable<Transu2> findByOrderByIdAsc();
	Transu2 findByWorkIdAndRowId(final long workId,final Integer rowId);
}
*/
