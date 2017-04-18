package translateit2.persistence.dao;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.NoRepositoryBean;
import org.springframework.data.repository.Repository;
import org.springframework.data.repository.query.Param;
import org.springframework.transaction.annotation.Transactional;

import translateit2.persistence.model2.Project;
import translateit2.persistence.model2.Transu2;
import translateit2.persistence.model2.Work;

// https://docs.spring.io/spring-data/jpa/docs/current/reference/html/#repositories.query-methods
@NoRepositoryBean
public interface WorkBaseRepository<T extends Work> extends CrudRepository<T, Long> {	
// TODO: make read only repository and extends loco and trance repositories respectively

	// MAP and JPA
	// https://docs.jboss.org/hibernate/orm/4.1/devguide/en-US/html/ch11.html
	// => Example 11.13. Qualified collection references example
	// http://stackoverflow.com/questions/4371384/can-jpa-return-results-as-a-map
	
	@Transactional(readOnly = true)
	@Query(" SELECT a.segment, b.segment FROM Transu2 a, Transu2 b WHERE a.segmentId=b.segmentId and a.work.id = :sourceCode and b.work.id = :targetCode ORDER BY a.rowId") 
    public Iterable <Object[]> listTranslatableTuples(@Param("sourceCode") Long sourceCode,
    		@Param("targetCode") Long targetCode );

}

