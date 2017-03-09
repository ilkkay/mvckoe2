package com.crud.dao;

import java.io.Serializable;
import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

public class AbstractJpaDAO<T extends Serializable> {
	private Class<T> clazz;

	@PersistenceContext
	private EntityManager entityManager;

	public final void setClazz(final Class<T> clazzToSet) {
	     this.clazz = clazzToSet;
	}

    public void save(final T entity) {
        entityManager.persist(entity);
    }

    public T update(final T entity) {
        return entityManager.merge(entity);
    }
	
	public T getClazzById(final long id) {
		// => Transu getTransuById(int id);
	    return entityManager.find(clazz, id);
	}

    public void deleteById(final long entityId) {
        final T entity = getClazzById(entityId);
        delete(entity);
    }
    
    public void delete(final T entity) {
        entityManager.remove(entity);
    }
    
    @SuppressWarnings("unchecked")
	public List<T> findAll() {
    	// => List<Transu> getTransus()
	    return entityManager.createQuery("from " + clazz.getName()).getResultList();
	}
}
