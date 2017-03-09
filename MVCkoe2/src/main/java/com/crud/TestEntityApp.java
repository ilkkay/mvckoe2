package com.crud;


import org.apache.log4j.Logger;
import org.hibernate.Session;

import java.sql.Date;
import java.util.Collection;
import java.util.LinkedHashSet;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.EntityTransaction;
import javax.persistence.Persistence;

import com.crud.model.TestEntity;

// https://marcin-chwedczuk.github.io/hibernate-hello-world-app
public class TestEntityApp 
{
    private static final Logger logger = Logger.getLogger(TestEntityApp.class);

    public static void main(String[] args)
    {
        logger.debug("starting VERY NEW application....");

        final EntityManagerFactory emf = 
                Persistence.createEntityManagerFactory("com.crud.one");
        EntityManager entityManager = emf.createEntityManager();
        EntityTransaction tx = entityManager.getTransaction();
        tx.begin();

        try {
            TestEntity testEntity = new TestEntity();
            testEntity.setName("super foo");    
            entityManager.persist(testEntity);
            
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
        
        
