package translate.it2.version1.util;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.LinkedHashSet;
import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.EntityTransaction;
import javax.persistence.Persistence;

import org.apache.log4j.Logger;

import translate.it2.version1.model.Loco;
import translate.it2.version1.model.Transu;


public class ISO8859LocoDbLoader {
    private static final Logger logger = Logger.getLogger(ISO8859LocoDbLoader.class);

    public static void Load()
    {
        logger.debug("starting ISO8859LocoDbLoader....");

        final EntityManagerFactory emf = 
                Persistence.createEntityManagerFactory("com.crud.one");
        EntityManager entityManager = emf.createEntityManager();
        EntityTransaction tx = entityManager.getTransaction();
        tx.begin();

        Loco loco = new Loco();
        loco.setName("Name of Localized Language Object"); 
        
        List<Transu> transus = null;
	    try {
	    	transus=ISO8859Loader.getTransus();
	    } catch (IOException e) {
	        e.printStackTrace();
	    }
	    
	    Collection<Transu> transusDB = new LinkedHashSet<Transu>();
	    
	    for (int i=0; i< 100 ;i++){
	    	// äh, öh ISO8859Loader asettaa id:t vaikka määritelty automaattiseksi
	    	// joten tehdään luonnit uudestaan
	    	Transu t = new Transu();
	        t.setSourceSegm(transus.get(i).getSourceSegm());
	        t.setTargetSegm(transus.get(i).getTargetSegm());
	        transusDB.add(t);
	    }
	    
        try {            
            loco.setTransus(transusDB);
            entityManager.persist(loco); 
            tx.commit();
        }
        catch (Exception e) {
            logger.error("cannot commit transaction", e);
            tx.rollback();
        }
        finally {
            entityManager.close();
        }
        emf.close();
    }
}
