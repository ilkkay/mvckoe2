package translateit2.persistence.dao;

import java.util.Optional;

import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;

import translateit2.persistence.model.Person;

public interface PersonRepository extends CrudRepository<Person, Long> {
    Optional<Person> findByFullName(@Param("fullName") String fullName);
}
