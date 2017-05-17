package translateit2.persistence.dao;

import org.springframework.data.repository.CrudRepository;

import translateit2.persistence.model.Info;

public interface InfoRepository extends CrudRepository<Info, Long> {

}