package translateit2.persistence.dao;

import java.util.List;

import org.springframework.data.repository.CrudRepository;
import org.springframework.data.rest.core.annotation.RepositoryRestResource;

import translateit2.persistence.model.Work;

@RepositoryRestResource(collectionResourceRel = "work", path = "work")
public interface WorkRepository extends CrudRepository<Work, Long> {
	List <Work> findAll();
	List <Work> findByProjectId(final long projectId);
	Long countByGroupId(final long groupId);
}
