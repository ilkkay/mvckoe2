package translateit2.service;

import javax.transaction.Transactional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import translateit2.persistence.dao.TransuRepository;
import translateit2.persistence.model.Transu;

@Service
@Transactional //
public class TransuServiceImpl {
    @Autowired
    private TransuRepository transuRepo;

    public TransuServiceImpl() {
        super();
    }
    
    public void update(final Transu entity) {
    	transuRepo.save(entity);
    }
    
    public void create(final Transu entity) {
    	transuRepo.save(entity);
    }

    public Transu getTransuById(final long id) {
        return transuRepo.findOne(id);
    }

    public void deleteTransu(final Transu entity) {
    	transuRepo.delete(entity.getId());
    }
    
    public void deleteTransu(final long id) {
    	transuRepo.delete(id);
    }
    
    public Iterable<Transu> getAllTransus() {
    	return transuRepo.findByOrderByIdAsc();
    	//return transuRepo.findAll();
    }

    public Iterable<Transu> getTransusOrderByLocoId(final long locoId) {
    	// error: ParameterBinder.binding
    	return transuRepo.findByOrderByLocoIdAsc(locoId);
    }
    
    public Iterable<Transu> getTransusByLocoId(final long locoId) {
        return transuRepo.findByLocoId(locoId);
    }
    
    public Transu getTransuByLocoIdAndRowId(final long locoId,
    		final Integer rowId) {
        return transuRepo.findByLocoIdAndRowId(locoId,rowId);
    }
}
