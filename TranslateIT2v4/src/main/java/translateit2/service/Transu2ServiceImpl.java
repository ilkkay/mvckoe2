package translateit2.service;

import javax.transaction.Transactional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import translateit2.persistence.dao.Transu2Repository;
import translateit2.persistence.dao.TransuRepository;
import translateit2.persistence.model.Transu;
import translateit2.persistence.model2.Transu2;

@Service
@Transactional //
public class Transu2ServiceImpl {
    @Autowired
    private Transu2Repository transuRepo;

    public Transu2ServiceImpl() {
        super();
    }
    
    public void update(final Transu2 entity) {
    	transuRepo.save(entity);
    }
    
    public void create(final Transu2 entity) {
    	transuRepo.save(entity);
    }

    public Transu2 getTransuById(final long id) {
        return transuRepo.findOne(id);
    }

    public void deleteTransu(final Transu entity) {
    	transuRepo.delete(entity.getId());
    }
    
    public void deleteTransu(final long id) {
    	transuRepo.delete(id);
    }
    
    public Iterable<Transu2> getAllTransus() {
    	return transuRepo.findByOrderByIdAsc();
    	//return transuRepo.findAll();
    }

    public Iterable<Transu2> getTransusOrderByLocoId(final long locoId) {
    	// error: ParameterBinder.binding
    	return transuRepo.findByOrderByWorkIdAsc(locoId);
    }
    
    public Iterable<Transu2> getTransusByLocoId(final long locoId) {
        return transuRepo.findByWorkId(locoId);
    }
    
    public Transu2 getTransuByWorkIdAndRowId(final long locoId,
    		final Integer rowId) {
        return transuRepo.findByWorkIdAndRowId(locoId,rowId);
    }
}
