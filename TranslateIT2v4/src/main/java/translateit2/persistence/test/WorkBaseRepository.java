package translateit2.persistence.test;

import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.NoRepositoryBean;

@NoRepositoryBean
public interface WorkBaseRepository<T extends Work> extends CrudRepository<T, Long> {
}

