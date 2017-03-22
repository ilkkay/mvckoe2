package translateit2.service;

import translateit2.persistence.model.Transu;

public interface TransuService {
    public void update(final Transu entity);
    
    public void create(final Transu entity);

    public Transu getTransuById(final long id);

    public void deleteTransu(final long id);
    
    public void deleteTransu(final Transu entity);
    
    public Iterable<Transu> getAllTransus();

    public Iterable<Transu> getTransusByLocoId(final long locoId);
    
    public Transu getTransuByLocoIdAndRowId(final long locoId,final Integer rowId);
}
