package translateit2.lngfileservice.factory;

import static org.assertj.core.api.Assertions.not;
import static org.hamcrest.CoreMatchers.equalTo;
import static org.hamcrest.CoreMatchers.instanceOf;
import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.assertThat;
import static org.mockito.Matchers.anyString;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.mockito.runners.MockitoJUnitRunner;

import translateit2.lngfileservice.LngFileFormat;
import translateit2.lngfileservice.LngFileStorage;
import translateit2.lngfileservice.iso8859.ISO8859Storage;
import translateit2.lngfileservice.xliff.XLIFFStorage;


//http://stackoverflow.com/questions/28557385/mocking-factory-pattern-in-java
	
@RunWith(MockitoJUnitRunner.class)
public class LngFileServiceFactoryImplTest {
	
	private LngFileServiceFactory lngFileServiceFactory = null;
	
	//@Mock
	//private LngFileServiceFactory lngFileServiceFactory;
	
	@Mock 
	private ISO8859Storage mockIso8859Storage;
	
	@Mock 
	private XLIFFStorage mockXliffStorage;

	@Before
	public void setup(){
		MockitoAnnotations.initMocks(this);
	}
	
	@Test
	public void returnsFactoryInstance() {
		/*
		LngFileServiceFactory mockFactory = mock(LngFileServiceFactory.class);
		when(mockFactory.getService("ISO8859")).thenReturn(mockIso8859Storage);
	       
		LngFileStorage service = mockFactory.getService("ISO8859");

		assertThat(service, instanceOf(ISO8859Storage.class));
		*/
	}
}
