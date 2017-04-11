package translateit2.service;

import translateit2.persistence.model.Transu;
import translateit2.persistence.model2.Transu2;

public interface Transu2Service {
    public void update(final Transu2 entity);
    
    public void create(final Transu2 entity);

    public Transu getTransuById(final long id);

    public void deleteTransu(final long id);
    
    public void deleteTransu(final Transu2 entity);
    
    public Iterable<Transu2> getAllTransus();

    public Iterable<Transu2> getTransusByWorkId(final long locoId);
    
    public Transu getTransuByWorkIdAndRowId(final long locoId,final Integer rowId);
}
