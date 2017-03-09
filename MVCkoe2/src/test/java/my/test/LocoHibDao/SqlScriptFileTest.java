package my.test.LocoHibDao;

import static org.junit.Assert.*;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.sql.Connection;
import java.sql.SQLException;

import org.h2.tools.RunScript;
import org.hibernate.Session;
import org.hibernate.jdbc.Work;
import org.junit.Before;
import org.junit.Test;

public class SqlScriptFileTest extends DbConnectionTest {

	// http://www.asjava.com/junit/junit-3-vs-junit-4-comparison/
    @Before
    public void testScriptFile(){
        Session session = em.unwrap(Session.class);
        session.doWork(new Work() {
            @Override
            public void execute(Connection connection) throws SQLException {
                try {
                    File script = new File(getClass().getResource("/testfile.sql").getFile());
                    RunScript.execute(connection, new FileReader(script));
                } catch (FileNotFoundException e) {
                    throw new RuntimeException("could not read the script file");
                }
            }
        });
    }
    
	@Test
	public void test() {
		System.out.println("Test in progress...");
	}

}
