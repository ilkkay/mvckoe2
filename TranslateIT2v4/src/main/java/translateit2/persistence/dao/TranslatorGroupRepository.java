package translateit2.persistence.dao;

import java.util.Optional;

import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;

import translateit2.persistence.model.TranslatorGroup;

public interface TranslatorGroupRepository extends CrudRepository<TranslatorGroup, Long> {
    Optional<TranslatorGroup> findByName(@Param("name") String name);
}