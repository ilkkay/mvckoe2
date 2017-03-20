package translateit2.persistence.dao;

import org.springframework.data.repository.CrudRepository;

import translateit2.persistence.model.Loco;

public interface LocoRepository extends CrudRepository<Loco, Long> {
	Loco findByProjectName(String projectName);
}