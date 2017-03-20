package translateit2.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import translateit2.persistence.dao.TransuRepository;
import translateit2.persistence.model.Transu;

@Service
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
    	/*
    	List<Transu> orderedList=(List<Transu>) transuRepo.findByLocoId(locoId);
    	orderedList.stream().sorted((o1, o2)->Long.compare(o1.getId(),o2.getId())).
                collect(Collectors.toList());
    	return orderedList;
    	*/
        return transuRepo.findByLocoId(locoId);
    }
}
