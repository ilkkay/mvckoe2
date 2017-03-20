package translateit2.config;

import org.springframework.beans.factory.InitializingBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import translateit2.persistence.model.Loco;
import translateit2.persistence.model.Transu;
import translateit2.service.LocoServiceImpl;
 
@Configuration
public class LocoServiceTestConfig {
	
	@Bean
	InitializingBean loadLocoData(LocoServiceImpl locoService){
		return ()->{
			Loco l = new Loco();
	    	l.setProjectName("Translate IT 2");
	    	l.setName("Ilkka");
	    	
	    	Transu transu = null;
	    	transu = new Transu();
	    	transu.setSourceSegm("sourceSegm 1");
	    	transu.setTargetSegm("targetSegm 1");
	    	transu.setLoco(l);
	    	
	    	transu = new Transu();
	    	transu.setSourceSegm("sourceSegm 2");
	    	transu.setTargetSegm("targetSegm 2");
	    	transu.setLoco(l);
	    	
	    	transu = new Transu();
	    	transu.setSourceSegm("sourceSegm 3");
	    	transu.setTargetSegm("targetSegm 3");
	    	transu.setLoco(l);
	    	locoService.createLoco(l);
		};
	}

 
    @Bean
    LocoServiceImpl locoService(){
        return new LocoServiceImpl();
    }
}