package translateit2;

import java.math.BigDecimal;
import java.util.Set;

import org.springframework.beans.factory.InitializingBean;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;

import translateit2.fileloader.storage.StorageProperties;
import translateit2.persistence.dao.ProductRepository;
import translateit2.persistence.model.Loco;
import translateit2.persistence.model.Product;
import translateit2.persistence.model.Transu;
import translateit2.service.LocoServiceImpl;
import translateit2.service.TransuServiceImpl;

@SpringBootApplication
@EnableConfigurationProperties(StorageProperties.class)
public class TranslateIt2v4Application {

	public static void main(String[] args) {
		SpringApplication.run(TranslateIt2v4Application.class, args);
	}

	@Bean
	InitializingBean loadProductData(ProductRepository productRepository){
		return ()->{
	        Product shirt = new Product();
	        shirt.setDescription("Spring Framework Guru Shirt");
	        shirt.setPrice(new BigDecimal("18.95"));
	        shirt.setImageUrl("https://springframework.guru/");
	        shirt.setProductId("235268845711068308");
	        productRepository.save(shirt);

	        Product mug = new Product();
	        mug.setDescription("Spring Framework Guru Mug");
	        mug.setImageUrl("https://springframework.guru/");
	        mug.setProductId("168639393495335947");
	        mug.setPrice(new BigDecimal("11.95"));
	        productRepository.save(mug);
		};
	}
	
	/*
	 * http://sivalabs.in/2016/03/how-springboot-autoconfiguration-magic/
	 */
/*
	@Bean
	InitializingBean loadISO8859Data(LocoServiceImpl locoService){
		return ()->{
	        List <Transu> transus=ISO8859Loader.getTransus();	              
   		    final Loco loco  = new Loco();
	    	loco.setProjectName("Translate IT II");
	    	loco.setName("IlkkaYläkoski");
	    	transus.stream().limit(3).forEach(t->{
	    		Transu transu = new Transu();
		    	transu.setSourceSegm(t.getSourceSegm());
		    	transu.setTargetSegm(t.getTargetSegm());
		    	transu.setLoco(loco); // fails here <=> null exception
	    	});
	    	locoService.createLoco(loco);			
	    	};
		}
*/
	
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

	public void testLoco(LocoServiceImpl locoService,
			TransuServiceImpl transuService) {
		//
    	// reread database
		//
    	Loco loco = locoService.getLocoByProjectName("Translate IT 2");
    	System.out.println("Project : " + loco.getProjectName());
    	
    	Set <Transu> transus = loco.getTransus();    	
    	// "failed to lazily initialize a collection of role"
    	// => fetch type = EAGER, not good?!
    	// or PagingLocoRepository and no loco.getTransus() call at all???
    	
    	System.out.println("*** Listing all transus ***");
    	transus.stream().forEach(t->System.out.println(t.getSourceSegm()));
    	System.out.println("*** Finished listing ***");
    	
    	// transus for testing
    	Transu transu = null;
    	Transu newTransu = new Transu();
    	Transu oldTransu = transus.stream()				   
    			.filter(t -> "sourceSegm 3".equals(t.getSourceSegm()))	
    			.findAny()				// If not found, return null
    			.orElse(null);			// or EMPTY transu object ???
    	Transu oldTransuRow = loco.getTransuByRowId(5);
    	
    	//pagination ??? => transuService !!    	
    	//Transu transuServiceTransu=transuService.getTransuById(5);
    	//transuServiceTransu.setLoco(loco); transuServiceTransu != oldTransuRow 

    	//transu=oldTransu;
    	transu=oldTransuRow;
    	System.out.println("Old value :" +  transu.getSourceSegm());
    	System.out.println("*** Updating old one ***");
    	
    	//transu=newTransu;
    	//System.out.println("*** Adding a new one ***");
    	transu.setSourceSegm("sourceSegm 4");
    	transu.setTargetSegm("targetSegm 4");
    	transu.setLoco(loco);
    	locoService.updateLoco(loco);
    	
    	transus = loco.getTransus();
    	transus.stream().forEach(t->System.out.println(t.getSourceSegm()));
    	System.out.println("***Adding: Using transuService ***");
    	Iterable <Transu> transut = transuService.getAllTransus();
    	transut.forEach(t->System.out.println(t.getSourceSegm()));

    	System.out.println("*** Reupdating the transu ***");
    	transu.setSourceSegm("No way ");
    	transu.setTargetSegm("Yeaah!");
    	transu.setLoco(loco);
    	locoService.updateLoco(loco);
    	
    	transus = loco.getTransus();
    	transus.stream().forEach(t->System.out.println(t.getSourceSegm()));
    	System.out.println("*** Reupdating: Using transuService ***");
    	transut = transuService.getAllTransus();
    	transut.forEach(t->System.out.println(t.getSourceSegm()));
    	
    	System.out.println("*** Removing the transu ***");
    	//loco.removeTransu(transu);
    	transu.setLoco(null);
    	transu = null;
    	locoService.updateLoco(loco);
    	
    	transus = loco.getTransus();
    	transus.stream().forEach(t->System.out.println(t.getSourceSegm()));
    	System.out.println("*** Removing: Using transuService ***");
    	transut = transuService.getAllTransus();
    	transut.forEach(t->System.out.println(t.getSourceSegm()));
	}

	public void testTransu(LocoServiceImpl locoService,
			TransuServiceImpl transuService) {
		//
    	// reread database
		//
		long locoId ;
			Loco loco = locoService.getLocoByProjectName("Translate IT 2");
			System.out.println("Project : " + loco.getProjectName());
			locoId = loco.getId();

    	Iterable<Transu>  transus = transuService.getTransusByLocoId(locoId);   	
    	System.out.println("*** Listing all transus ***");
    	transus.forEach(t->System.out.println(t.getSourceSegm()));
    	System.out.println("*** Finished listing ***");
    	
    	// transus for testing
    	Transu transu = null;
    	Transu newTransu = new Transu();
    	Transu transuServiceTransu = transuService.getTransuById(5);

    	transu=transuServiceTransu; 
    	System.out.println("Old value :" +  transu.getSourceSegm());
    	System.out.println("*** Updating old one ***");
    	
    	//transu=newTransu;
    	//transu.setLoco(loco);
    	//System.out.println("*** Adding a new one ***");
    	transu.setSourceSegm("sourceSegm 4");
    	transu.setTargetSegm("targetSegm 4");
    	transuService.update(transu);
    	
    	System.out.println("***Adding: Using transuService ***");
    	Iterable <Transu> transut = transuService.getTransusByLocoId(locoId);
    	transut.forEach(t->System.out.println(t.getSourceSegm()));

    	transu.setSourceSegm("No way ");
    	transu.setTargetSegm("Yeaah!");
    	transuService.update(transu);
    	
    	System.out.println("*** Reupdating: Using transuService ***");
    	transut = transuService.getTransusByLocoId(locoId);
    	transut.forEach(t->System.out.println(t.getSourceSegm()));
    	
    	transuService.deleteTransu(transu);
    	System.out.println("*** Removing: Using transuService ***");
    	transut = transuService.getTransusByLocoId(locoId);
    	transut.forEach(t->System.out.println(t.getSourceSegm()));
	}

	/*
	 * With CommandLineRunner you can perform tasks after all Spring Beans 
	 * are created and the Application Context has been created.
	 */
	@Bean
	public CommandLineRunner demo(TransuServiceImpl transuService,
			LocoServiceImpl locoService) {
		return (args) -> {	
			testLoco(locoService, transuService);
			//testTransu(locoService, transuService);
		};
	}

}
