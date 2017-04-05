package translateit2;

import static org.junit.Assert.fail;

import java.io.FileNotFoundException;
import java.sql.SQLException;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.Persistence;

import org.junit.AfterClass;
import org.junit.BeforeClass;
import org.junit.Test;

public class DbConnectionTest {

    protected static EntityManagerFactory emf;
    protected static EntityManager em;

    @BeforeClass
    public static void init() throws FileNotFoundException, SQLException {
        emf = Persistence.createEntityManagerFactory("translateit2.trial4");
        em = emf.createEntityManager();
    }

	@Test
	public void test() {
		System.out.println("in test");
		fail("test");
	}
	
    @AfterClass
    public static void tearDown(){
        em.clear();
        em.close();
        emf.close();
    }
}
