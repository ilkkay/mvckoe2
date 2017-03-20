package translateit2.service;

import translateit2.persistence.model.Loco;
import translateit2.persistence.model.Transu;

public interface LocoService {
    public void updateLoco(final Loco entity);
    
    public void createLoco(final Loco entity);

    public Loco getLocoById(final long id);
    
    public Iterable<Loco> listAllLocos();
    
    public Loco getLocoByProjectName(String projectName);
    
    public void removeTransu(final long id);
    
    //public void updateTransuInLoco(Transu transu, Loco loco);
}
