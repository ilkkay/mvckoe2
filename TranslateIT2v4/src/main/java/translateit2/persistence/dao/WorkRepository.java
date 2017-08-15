package translateit2.persistence.dao;

import java.util.List;

import org.springframework.data.repository.CrudRepository;

import translateit2.persistence.model.Work;

//@RepositoryRestResource(collectionResourceRel = "work", path = "work")
public interface WorkRepository extends CrudRepository<Work, Long> {
    Long countByGroupId(final long groupId);

    List<Work> findByProjectId(final long projectId);
    
    @Override
    List<Work> findAll();
}
