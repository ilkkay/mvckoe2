package translateit2.service;

import java.util.List;

import org.springframework.data.domain.Page;

import translateit2.persistence.model.Transu;
import translateit2.persistence.model2.Transu2;

public interface Transu2Service {
    public void update(final Transu2 entity);
    
    public void update(final List<Transu2> transus);
    
    public void create(final Transu2 entity);
    
    public void create(final List<Transu2> transus);

    public Transu getTransuById(final long id);

    public void deleteTransu(final long id);
    
    public void deleteTransu(final Transu2 entity);
    
    public void deleteTransu(final List<Transu2> transus);
    
    public Iterable<Transu2> getAllTransus();

    public Iterable<Transu2> getTransusByWorkId(final long locoId);
    
    public Transu2 getTransuByWorkIdAndRowId(final long workId,final Integer rowId);
    
    public Page<Transu2> getTransusPage(final int startPage, final int endPage);
}
