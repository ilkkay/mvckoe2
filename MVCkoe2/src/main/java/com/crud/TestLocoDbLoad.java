package com.crud;

import java.util.Collection;
import java.util.LinkedHashSet;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.EntityTransaction;
import javax.persistence.Persistence;

import org.apache.log4j.Logger;

import com.crud.model.Loco;
import com.crud.model.Transu;

public class TestLocoDbLoad {
    private static final Logger logger = Logger.getLogger(TestLocoDbLoad.class);

    public static void main(String[] args)
    {
        logger.debug("starting TestLocoDblLoad....");

        final EntityManagerFactory emf = 
                Persistence.createEntityManagerFactory("HelloWorldPU");
        EntityManager entityManager = emf.createEntityManager();
        EntityTransaction tx = entityManager.getTransaction();
        tx.begin();

        Transu transu = null;
        Collection<Transu> transus = new LinkedHashSet<Transu>();
        try {
            Loco loco = new Loco();
            loco.setName("Name of Localized Language Object");       
            transu = new Transu();
            transu.setSourceSegm("One");
            transu.setTargetSegm("Yksi");
            transus.add(transu);
            transu = new Transu();
            transu.setSourceSegm("Two");
            transu.setTargetSegm("Kaksi");
            transus.add(transu);
            
            loco.setTransus(transus);
       
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
