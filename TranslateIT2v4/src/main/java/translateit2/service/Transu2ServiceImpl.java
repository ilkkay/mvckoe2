package translateit2.service;

import java.util.List;

import javax.transaction.Transactional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
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
    
    public void update(final List <Transu2> entities) {
    	transuRepo.save(entities);
    }
    
    public void create(final Transu2 entity) {
    	transuRepo.save(entity);
    }

    public void create(final List <Transu2> entities) {
    	transuRepo.save(entities);
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
    
    public void deleteTransu(final List <Transu2> entities) {
    	transuRepo.delete(entities);
    }
    
    public Iterable<Transu2> getAllTransus() {
    	return transuRepo.findByOrderByIdAsc();
    	//return transuRepo.findAll();
    }

    public Iterable<Transu2> getTransusOrderByWorkId(final long workId) {
    	// error: ParameterBinder.binding
    	return transuRepo.findByOrderByWorkIdAsc(workId);
    }
    
    public Iterable<Transu2> getTransusByWorkId(final long workId) {
        return transuRepo.findByWorkId(workId);
    }
    
    public Transu2 getTransuByWorkIdAndRowId(final long workId,
    		final Integer rowId) {
        return transuRepo.findByWorkIdAndRowId(workId,rowId);
    }
    
    Page<Transu2> getTransusPage(final int startPage, final int endPage) {
    	return transuRepo.findAll(new PageRequest(startPage, endPage));
    }
}
