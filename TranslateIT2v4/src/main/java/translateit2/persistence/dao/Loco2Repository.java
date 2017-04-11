package translateit2.persistence.dao;

import java.util.Optional;

import translateit2.persistence.model.Loco;
import translateit2.persistence.model2.Loco2;

public interface Loco2Repository extends WorkBaseRepository<Loco2> {
	//Optional<Loco2> findByProjectName(String projectName);
	Iterable <Loco2> findByOrderByIdAsc();
	
	//Loco2 findByName(String projectName);
}
