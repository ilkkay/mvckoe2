package translateit2.persistence.dto;

import static org.junit.Assert.*;

import org.junit.Test;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;

import translateit2.persistence.model.Loco;


public class LocoDtoTest {
	@Autowired 
	private ModelMapper modelMapper;

    private Loco convertToEntity(LocoDto locoDto) {
        Loco loco = modelMapper.map(locoDto, Loco.class);
        return loco;
    }
    
	@Test
	public void test() {
		fail("Not yet implemented");
	}

}
