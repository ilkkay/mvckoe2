package translateit2.config;

import org.springframework.beans.factory.InitializingBean;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import translateit2.persistence.dto.LocoDto;
import translateit2.persistence.dto.TransuDto;
import translateit2.service.Loco2ServiceImpl;
 
@Configuration
public class LocoServiceTestConfig { 

	/*
	 * @Configuration classes ... and 
	 * may not use @Autowired constructor parameters. Any nested 
	 * configuration classes must be static
	 */

	private Loco2ServiceImpl loco2Service;    
    @Autowired
    public void setLoco2Service(Loco2ServiceImpl loco2Service) {
        this.loco2Service = loco2Service;
    }
    
	@Bean
	InitializingBean loadLocoData(){
		return ()->{        
		LocoDto locoDto = new LocoDto();
		locoDto.setProjectName("Translate IT 2");
		locoDto.setName("Ilkka");
		locoDto=loco2Service.createLocoDto(locoDto);
    	
    	System.out.println("Initializing Bean loadLocoData()");
    	int count = loco2Service.listAllLocoDtos().size();
    	System.out.println("Loco lines in db " + count);
    	int count2 = loco2Service.listAllTransuDtos(locoDto).size();
    	System.out.println("Loco lines in db " + count2);

    	TransuDto transuDto = new TransuDto();
    	
    	transuDto.setSourceSegm("SourceSegm 1");
    	transuDto.setTargetSegm("TargetSegm 1");
    	transuDto.setRowId(1);
    	locoDto=loco2Service.createTransuDto(transuDto, locoDto);
    	
    	transuDto.setSourceSegm("SourceSegm 2");
    	transuDto.setTargetSegm("TargetSegm 2");
    	transuDto.setRowId(2);
    	locoDto=loco2Service.createTransuDto(transuDto, locoDto);
    	
    	transuDto.setSourceSegm("SourceSegm 3");
    	transuDto.setTargetSegm("TargetSegm 3");
    	transuDto.setRowId(3);
    	locoDto=loco2Service.createTransuDto(transuDto, locoDto);
    	
    	locoDto = loco2Service.updateLocoDto(locoDto);
    	
    	// Empty project
		locoDto = new LocoDto();
		locoDto.setProjectName("Translate IT 4");
		locoDto.setName("Jukka");
		locoDto=loco2Service.createLocoDto(locoDto);
		};
	}

}