package translateit2.persistence.dao;

import java.util.Optional;

import org.springframework.data.repository.CrudRepository;

import translateit2.persistence.model.Loco;

public interface LocoRepository extends CrudRepository<Loco, Long> {
	Optional<Loco> findByProjectName(String projectName);
	Iterable <Loco> findByOrderByIdAsc();
	
	Loco findByName(String projectName);
}