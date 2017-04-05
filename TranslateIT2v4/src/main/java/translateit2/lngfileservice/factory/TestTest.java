package translateit2.lngfileservice.factory;

import static org.assertj.core.api.Assertions.not;
import static org.hamcrest.CoreMatchers.equalTo;
import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.*;

import org.junit.Test;

public class TestTest {

	@Test
	public void test() {
		boolean answer = false;
		
		// when loading file
		/*LngFileFormat format = new LngFileFormat("ISO8859", "input.txt");
		try{
			lngFileServiceFactory.getService(format.getFormat());
		} catch (Exception e) {
			e.printStackTrace();
			System.out.println(e.getMessage());
			answer = true;
		}
		*/
		// then fail is not supported
		assertThat(answer, is(equalTo(true)));
	}

}
