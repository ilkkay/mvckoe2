package my.test.LocoHibDao;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.sql.Connection;
import java.sql.SQLException;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.Persistence;

import org.h2.tools.RunScript;
import org.hibernate.Session;
import org.hibernate.jdbc.Work;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.hibernate.Session;
import org.hibernate.jdbc.Work;
import org.junit.Test;
import static org.junit.Assert.*;

public class DbConnectionTest {

    protected static EntityManagerFactory emf;
    protected static EntityManager em;

    @BeforeClass
    public static void init() throws FileNotFoundException, SQLException {
        emf = Persistence.createEntityManagerFactory("com.crud.one");
        em = emf.createEntityManager();
    }

	@Test
	public void test() {
		System.out.println("in test");
	}
	
    @AfterClass
    public static void tearDown(){
        em.clear();
        em.close();
        emf.close();
    }
}

