package translate.it2.version1.dao;

import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.EntityTransaction;
import javax.persistence.Persistence;

import org.springframework.stereotype.Repository;

import translate.it2.version1.model.Transu;


// http://www.baeldung.com/transaction-configuration-with-jpa-and-spring
// https://howtoprogramwithjava.com/hibernate-creating-data-access-objects-daos/
// https://docs.jboss.org/hibernate/orm/3.6/quickstart/en-US/html/hibernate-gsg-tutorial-jpa.html

//@Repository
//@Transactional
public class LocoHibDaoImpl implements LocoDao {

	@Override
	public int update(Transu t) {
		// TODO Auto-generated method stub
		// id ei saa olla alustettu, mutta yhteen sopivuuden vuoksi
		Transu tmp_t = new Transu();
		tmp_t.setSourceSegm(t.getSourceSegm());
		tmp_t.setTargetSegm(t.getTargetSegm());
		save(tmp_t);
		return tmp_t.getId();
	}

	@Override
	public int save(Transu t) {
		// TODO Auto-generated method stub
        EntityManager entityManager = 
        		Persistence.createEntityManagerFactory( "translate.it2.version1" ).
        		createEntityManager();
		entityManager.getTransaction().begin();
		entityManager.persist( t );
		entityManager.getTransaction().commit();
		entityManager.close();
		return t.getId();
	}

	// https://www.tutorialspoint.com/jpa/jpa_entity_managers.htm
	@Override
	public Transu getTransuById(int id) {
		// TODO Auto-generated method stub
        EntityManager entityManager = 
        		Persistence.createEntityManagerFactory( "translate.it2.version1" ).
        		createEntityManager();
        
        Transu t = entityManager.find(Transu.class, id);
        
        entityManager.close();
		return t;
	}

	@Override
	public int delete(int id) {
		// TODO Auto-generated method stub
        EntityManager entityManager = 
        		Persistence.createEntityManagerFactory( "translate.it2.version1" ).
        		createEntityManager();
        entityManager.getTransaction().begin();
	      
        Transu t = entityManager.find(Transu.class, id);
        int tmp_id = t.getId();
	    entityManager.remove( t );
	    entityManager.getTransaction( ).commit( );
	    entityManager.close( );
		return tmp_id;
	}

	@Override
	public List<Transu> getTransus() {
		// TODO Auto-generated method stub
        EntityManager entityManager = 
        		Persistence.createEntityManagerFactory( "translate.it2.version1" ).
        		createEntityManager();
        entityManager.getTransaction().begin();
        List<Transu> result = entityManager.createQuery( "from Transu", Transu.class ).getResultList();
        for ( Transu t : result ) {
            System.out.println( t.getSourceSegm() );
        }
        entityManager.getTransaction().commit();
        entityManager.close();
        
		return result;
	}

}
