package my.test.LocoHibDao;

import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;

import com.crud.dao.LocoHibDaoImpl;
import com.crud.model.Transu;

/*
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.transaction.TransactionConfiguration;
import org.springframework.transaction.annotation.Transactional;

// You need to use the Spring JUnit runner 
// in order to wire in Spring beans from your context.
// http://stackoverflow.com/questions/16261838/junit-test-case-for-database-insert-method-with-dao-and-web-service

@RunWith(SpringJUnit4ClassRunner.class)
@Transactional
@TransactionConfiguration(defaultRollback=true)
@ContextConfiguration(locations={"classpath:test-context.xml",
"classpath:/META-INF/spring/applicationContext.xml"})
*/
public class HibDaoTests {
    @Autowired  
    LocoHibDaoImpl locoDaoDb;

    @Test
    public void testFindById()
    {
        Transu t = locoDaoDb.getTransuById(47);

        Assert.assertEquals("Result", t.getSourceSegm());
        Assert.assertEquals("Tulos", t.getSourceSegm());
        return;
    }

}
