package translateit2.persistence.dao;

import java.util.Optional;

import org.springframework.data.repository.CrudRepository;

import translateit2.persistence.model.Loco;
import translateit2.persistence.model2.Project;

public interface ProjectRepository extends CrudRepository<Project, Long> {
	Optional<Project> findByProjectName(String projectName);
	Iterable <Project> findByOrderByIdAsc();
	
	Project findByManager(String managerName);
}