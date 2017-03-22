package translateit2.persistence.dao;

import org.springframework.data.repository.CrudRepository;

import translateit2.persistence.model.Loco;
//http://docs.spring.io/spring-data/jpa/docs/1.4.1.RELEASE/reference/html/jpa.repositories.html

public interface LocoRepository extends CrudRepository<Loco, Long> {
	Loco findByProjectName(String projectName);
}