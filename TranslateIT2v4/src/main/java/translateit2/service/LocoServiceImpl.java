package translateit2.service;

import javax.transaction.Transactional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import translateit2.persistence.dao.LocoRepository;
import translateit2.persistence.model.Loco;

@Service
@Transactional // we don't need this annotation <= CrudRepository
public class LocoServiceImpl implements LocoService{
    @Autowired
    private LocoRepository locoRepo;
    // private PagingLocoRepository locoRepo;
    
    public LocoRepository getLocoRepo() {
		return locoRepo;
	}

	public void setLocoRepo(LocoRepository locoRepo) {
		this.locoRepo = locoRepo;
	}

	/* Are these the same thing
    private LocoRepository locoRepo;
    @Autowired
    public void setProductRepository(LocoRepository locoRepository) {
        this.locoRepo = locoRepository;
    }
    */
    public LocoServiceImpl() {
        super();
    }

    @Override
	public void updateLoco(final Loco entity) {
    	locoRepo.save(entity);
    }
    
    @Override
	public void createLoco(final Loco entity) {
        locoRepo.save(entity);
    }

    @Override
	public Loco getLocoById(final long id) {
        return locoRepo.findOne(id);
    }
    
    @Override
	public Iterable<Loco> listAllLocos() {
        return locoRepo.findAll();
    }
    
    @Override
	public Loco getLocoByProjectName(String projectName) {
        return locoRepo.findByProjectName(projectName);
    }
    
    @Override
	public void removeTransu(final long id){
    	
    };
}
