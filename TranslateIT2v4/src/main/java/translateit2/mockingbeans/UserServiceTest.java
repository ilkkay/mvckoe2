package translateit2.mockingbeans;

import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mockito;
import org.mockito.runners.MockitoJUnitRunner;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.context.web.WebAppConfiguration;

import translateit2.TranslateIt2v4Application;


// https://spring.io/blog/2016/04/15/testing-improvements-in-spring-boot-1-4
// http://www.baeldung.com/injecting-mocks-in-spring

@ActiveProfiles("test")
@RunWith(SpringRunner.class)
@SpringBootTest(classes = MocksApplication.class)
public class UserServiceTest {
 
    @Autowired
    private UserService userService;
 
    @Autowired
    private NameService nameService;
 
    @Test
    public void NoMock_whenUserIdIsProvided_thenRetrievedNameIsCorrect() {
        String testName = userService.getUserName("SomeId");
        Assert.assertEquals(null, testName);
    }
    
    @Test
    public void whenUserIdIsProvided_thenRetrievedNameIsCorrect() {
        Mockito.when(nameService.getUserName("SomeId")).thenReturn("Mock user name");
        String testName = userService.getUserName("SomeId");
        Assert.assertEquals("Mock user name", testName);
    }
}