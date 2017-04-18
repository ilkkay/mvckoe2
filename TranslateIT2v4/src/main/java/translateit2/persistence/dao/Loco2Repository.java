package translateit2.persistence.dao;

import org.springframework.data.jpa.repository.Modifying;
import org.springframework.transaction.annotation.Transactional;

import translateit2.persistence.model2.Loco2;

@Transactional
public interface Loco2Repository extends WorkBaseRepository<Loco2> {
    @Transactional(readOnly=true) 
	Iterable<Loco2> findByOrderByIdAsc();

    @Transactional(readOnly=true) 
	Iterable<Loco2> findByTranveId(long tranveId);
	
	//Loco2 findByName(String projectName);
	//Optional<Loco2> findByProjectName(String projectName);
}
