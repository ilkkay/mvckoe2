package translateit2;

import org.junit.runner.RunWith;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.context.web.WebAppConfiguration;

@RunWith(SpringRunner.class)
@SpringBootTest(classes = TranslateIt2v4Application.class)
@WebAppConfiguration
public class TransuIntegrationTests {
	public void failToInsertTransu_ifSourceSegmentEmptyOrNull() {
		
	}
	
	public void failToInsertTransu_ifLocoIsNotValid() {
		
	}
	
	public void failToUpdateTarget_ifNotContainSameParametersAsTarget() {
		
	}
	
}