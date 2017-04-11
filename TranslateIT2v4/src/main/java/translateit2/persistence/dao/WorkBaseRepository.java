package translateit2.persistence.dao;

import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.NoRepositoryBean;

import translateit2.persistence.model2.Work;

@NoRepositoryBean
public interface WorkBaseRepository<T extends Work> 
	extends CrudRepository<T, Long> {
}

