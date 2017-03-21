package translateit2;

import java.io.IOException;
import java.math.BigDecimal;
import java.util.List;
import java.util.Set;

import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;

import translateit2.fileloader.storage.StorageProperties;
import translateit2.persistence.dao.ProductRepository;
import translateit2.persistence.dto.LocoDto;
import translateit2.persistence.dto.TransuDto;
import translateit2.persistence.model.Loco;
import translateit2.persistence.model.Product;
import translateit2.persistence.model.Transu;
import translateit2.service.LocoService;
import translateit2.service.LocoServiceImpl;
import translateit2.service.TransuServiceImpl;
import translateit2.util.ISO8859Loader;

@SpringBootApplication
@EnableConfigurationProperties(StorageProperties.class)
public class TranslateIt2v4Application {

	public static void main(String[] args) {
		SpringApplication.run(TranslateIt2v4Application.class, args);
	}

	@Bean
	public ModelMapper modelMapper() {
	    return new ModelMapper();
	}

	private void testMapperLoco(ModelMapper modelMapper,
			LocoServiceImpl locoService){
        LocoDto locoDto = new LocoDto();
		locoDto.setProjectName("Translate IT 2");
		locoDto.setName("Ilkka");
		Loco loco = modelMapper.map(locoDto, Loco.class);
		
    	Transu transu = null;
    	transu = new Transu();
    	transu.setSourceSegm("sourceSegm 1");
    	transu.setTargetSegm("targetSegm 1");
    	transu.setRowId(1);
    	transu.setLoco(loco);
    	
    	transu = new Transu();
    	transu.setSourceSegm("sourceSegm 2");
    	transu.setTargetSegm("targetSegm 2");
    	transu.setRowId(1);
    	transu.setLoco(loco);
    	
		locoService.createLoco(loco);
	}
	
	private void testMapperLocoDto(LocoServiceImpl locoService){
        LocoDto locoDto = new LocoDto();
		locoDto.setProjectName("Translate IT 2");
		locoDto.setName("Ilkka");
    	locoService.createLocoDto(locoDto);

    	TransuDto transuDto = new TransuDto();
    	
    	transuDto.setSourceSegm("sourceSegm 1");
    	transuDto.setTargetSegm("targetSegm 1");
    	transuDto.setRowId(1);
    	locoService.createTransuDto(transuDto);
    	
    	transuDto.setSourceSegm("sourceSegm 2");
    	transuDto.setTargetSegm("targetSegm 2");
    	transuDto.setRowId(2);
    	locoService.createTransuDto(transuDto);
    	
    	transuDto.setSourceSegm("sourceSegm 3");
    	transuDto.setTargetSegm("targetSegm 3");
    	transuDto.setRowId(3);
    	locoService.createTransuDto(transuDto);
    	
    	locoDto = locoService.updateLocoDto();
		}
	
	private void testMapperISO8859Data(LocoServiceImpl locoService) throws IOException{
        List <Transu> transus=ISO8859Loader.getTransus();	              
		
        LocoDto locoDto = new LocoDto();
		locoDto.setProjectName("Translate IT 2");
		locoDto.setName("Ilkka");	
    	locoService.createLocoDto(locoDto);
    	
    	int rowId=0;
    	for (Transu t : transus) {
    		TransuDto transuDto = new TransuDto();
	    	transuDto.setSourceSegm(t.getSourceSegm());
	    	transuDto.setTargetSegm(t.getTargetSegm());
	    	transuDto.setRowId(++rowId);
	    	locoService.createTransuDto(transuDto);
	    	if (rowId == 3) break;
    	}
    	locoService.updateLocoDto();			
	}

	public void testLocoDto2(LocoServiceImpl locoService,
			TransuServiceImpl transuService) {
		//
    	// reread database
		//
    	LocoDto locoDto = locoService.getLocoDtoByProjectName("Translate IT 2");
    	System.out.println("Project : " + locoDto.getProjectName());
    	
    	List <TransuDto> transuDtos = locoService.listAllTransuDtos();
    	//List<Transu> transuDtos = (List<Transu>) locoService.listAllTransus();
    	
    	System.out.println("*** Listing all transus ***");
    	transuDtos.stream().forEach(t->System.out.println(t.getSourceSegm()));
    	System.out.println("*** Finished listing ***");
    	    	
    	TransuDto transuDto = null;
    	TransuDto newTransuDto = new TransuDto();
    	TransuDto oldTransuDto = transuDtos.stream()				   
    			.filter(t -> "sourceSegm 3".equals(t.getSourceSegm()))	
    			.findAny()				// If not found, return null
    			.orElse(null);			// or EMPTY transu object ???
    	TransuDto oldTransuDtoRow = locoService.getTransuDtoByRowId(2);
    	
    	/*
    	//transu=oldTransu;
    	transuDto=oldTransuDtoRow;
    	System.out.println("Old value :" +  transuDto.getSourceSegm());
    	System.out.println("*** Updating old one ***");
    	transuDto.setSourceSegm("sourceSegm update");
    	transuDto.setTargetSegm("targetSegm update");
    	locoService.updateTransuDto(transuDto); // transu.setLoco (persistent loco)
    	locoService.updateLocoDto(); // use persistent loco
    	*/
    	
    	transuDto=newTransuDto;
    	System.out.println("*** Adding a new one ***");
    	transuDto.setSourceSegm("sourceSegm 4");
    	transuDto.setTargetSegm("targetSegm 4");
    	transuDto.setRowId(4);
    	locoService.createTransuDto(transuDto); // transu.setLoco (persistent loco)
    	locoDto = locoService.updateLocoDto(); // use persistent loco
    	
    	
    	transuDtos = locoService.listAllTransuDtos();
    	transuDtos.stream().forEach(t->System.out.println(t.getSourceSegm()));
    	System.out.println("***Adding: Using transuService ***");
    	Iterable <Transu> transut = transuService.getAllTransus();
    	transut.forEach(t->System.out.println(t.getSourceSegm()));
    	
    	System.out.println("*** Reupdating the transu ***");
    	transuDto.setSourceSegm("No way ");
    	transuDto.setTargetSegm("Yeaah!");
    	locoService.updateTransuDto(transuDto); // transu.setLoco (persistent loco)
    	locoDto = locoService.updateLocoDto(); // use persistent loco
    	
    	transuDtos = locoService.listAllTransuDtos();
    	transuDtos.stream().forEach(t->System.out.println(t.getSourceSegm()));
    	System.out.println("*** Reupdating: Using transuService ***");
    	transut = transuService.getAllTransus();
    	transut.forEach(t->System.out.println(t.getSourceSegm()));
    	
    	System.out.println("*** Removing the transu ***");
    	locoService.removeTransuDto(transuDto);
    	locoDto = locoService.updateLocoDto();
    	
    	transuDtos = locoService.listAllTransuDtos();
    	transuDtos.stream().forEach(t->System.out.println(t.getSourceSegm()));
    	System.out.println("*** Removing: Using transuService ***");
    	transut = transuService.getAllTransus();
    	transut.forEach(t->System.out.println(t.getSourceSegm()));    
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
			
	        LocoDto locoDto = new LocoDto();
			locoDto.setProjectName("Translate IT 2");
			locoDto.setName("Ilkka");	
	    	locoService.createLocoDto(locoDto);
	    	
	    	int rowId=0;
	    	for (Transu t : transus) {
	    		TransuDto transuDto = new TransuDto();
		    	transuDto.setSourceSegm(t.getSourceSegm());
		    	transuDto.setTargetSegm(t.getTargetSegm());
		    	transuDto.setRowId(++rowId);
		    	locoService.createTransuDto(transuDto);
		    	if (rowId == 3) break;
	    	}
	    	locoService.updateLocoDto();			
	    	};
		}
*/

/*
	@Bean
	InitializingBean loadLocoData(LocoServiceImpl locoService){
		return ()->{
			
			LocoDto locoDto = new LocoDto();
			locoDto.setProjectName("Translate IT 2");
			locoDto.setName("Ilkka");	
	    	locoService.createLocoDto(locoDto);
	    		    	
	    	TransuDto transuDto = null;
	    	transuDto = new TransuDto();
	    	transuDto.setSourceSegm("sourceSegm 1");
	    	transuDto.setTargetSegm("targetSegm 1");
	    	transuDto.setRowId(1);
	    	locoService.createTransuDto(transuDto);
	    	
	    	transuDto = new TransuDto();
	    	transuDto.setSourceSegm("sourceSegm 2");
	    	transuDto.setTargetSegm("targetSegm 2");
	    	transuDto.setRowId(2);
	    	locoService.createTransuDto(transuDto);
	    	
	    	transuDto = new TransuDto();
	    	transuDto.setSourceSegm("sourceSegm 3");
	    	transuDto.setTargetSegm("targetSegm 3");
	    	transuDto.setRowId(3);
	    	locoService.createTransuDto(transuDto);
	    	
	    	locoService.updateLocoDto();
	    	
			
			Loco loco = new Loco();
			loco.setProjectName("Translate IT 2");
			loco.setName("Ilkka");	
	    	//locoService.createLocoDto(locoDto);			
	    		    	
	    	Transu transu = null;
	    	transu = new Transu();
	    	transu.setSourceSegm("sourceSegm 1");
	    	transu.setTargetSegm("targetSegm 1");
	    	transu.setRowId(1);
	    	//locoService.createTransuDto(transuDto);
	    	transu.setLoco(loco);
	    	
	    	transu = new Transu();
	    	transu.setSourceSegm("sourceSegm 2");
	    	transu.setTargetSegm("targetSegm 2");
	    	transu.setRowId(2);
	    	transu.setLoco(loco);
	    	//locoService.createTransuDto(transu);
	    	
	    	transu = new Transu();
	    	transu.setSourceSegm("sourceSegm 3");
	    	transu.setTargetSegm("targetSegm 3");
	    	transu.setRowId(3);
	    	transu.setLoco(loco);
	    	//locoService.createTransuDto(transuDto);
	    	
	    	//locoService.updateLocoDto();
	    	loco=locoService.updateLoco(loco);
		};
	}
*/
	
	public void testLocoDto(LocoServiceImpl locoService,
			TransuServiceImpl transuService) {
		//
    	// reread database
		//
    	LocoDto locoDto = locoService.getLocoDtoByProjectName("Translate IT 2");
    	System.out.println("Project : " + locoDto.getProjectName());
    	
    	List <TransuDto> transuDtos = locoService.listAllTransuDtos();
    	
    	System.out.println("*** Listing all transus ***");
    	transuDtos.stream().forEach(t->System.out.println(t.getSourceSegm()));
    	System.out.println("*** Finished listing ***");
    	
    	TransuDto transuDto = null;
    	TransuDto newTransuDto = new TransuDto();
    	TransuDto oldTransuDto = transuDtos.stream()				   
    			.filter(t -> "sourceSegm 3".equals(t.getSourceSegm()))	
    			.findAny()				// If not found, return null
    			.orElse(null);			// or EMPTY transu object ???
    	TransuDto oldTransuDtoRow = locoService.getTransuDtoByRowId(2);
    	
    	
    	//transu=oldTransu;
    	transuDto=oldTransuDtoRow;
    	System.out.println("Old value :" +  transuDto.getSourceSegm());
    	System.out.println("*** Updating old one ***");
    	transuDto.setSourceSegm("sourceSegm update");
    	transuDto.setTargetSegm("targetSegm update");
    	locoService.updateTransuDto(transuDto); // transu.setLoco (persistent loco)
    	locoService.updateLocoDto(); // use persistent loco
    	
    	/*
    	transu=newTransu;
    	System.out.println("*** Adding a new one ***");
    	transu.setSourceSegm("sourceSegm 4");
    	transu.setTargetSegm("targetSegm 4");
    	transu.setRowId(4);
    	locoService.createTransuDto(transu); // transu.setLoco (persistent loco)
    	locoDto = locoService.updateLocoDto(); // use persistent loco
    	*/
    	//locoDto = locoService.getLocoDtoByProjectName("Translate IT 2");
    	transuDtos = locoService.listAllTransuDtos();
    	transuDtos.stream().forEach(t->System.out.println(t.getSourceSegm()));
    	System.out.println("***Adding: Using transuService ***");
    	Iterable <Transu> transut = transuService.getAllTransus();
    	transut.forEach(t->System.out.println(t.getSourceSegm()));

    	System.out.println("*** Reupdating the transu ***");
    	transuDto.setSourceSegm("No way ");
    	transuDto.setTargetSegm("Yeaah!");
    	locoService.updateTransuDto(transuDto); // transu.setLoco (persistent loco)
    	locoDto = locoService.updateLocoDto(); // use persistent loco
    	
    	//locoDto = locoService.getLocoDtoByProjectName("Translate IT 2");
    	transuDtos = locoService.listAllTransuDtos();
    	transuDtos.stream().forEach(t->System.out.println(t.getSourceSegm()));
    	System.out.println("*** Reupdating: Using transuService ***");
    	transut = transuService.getAllTransus();
    	transut.forEach(t->System.out.println(t.getSourceSegm()));
    	
    	System.out.println("*** Removing the transu ***");
    	locoService.removeTransuDto(transuDto);
    	locoDto = locoService.updateLocoDto();
    	
    	//locoDto = locoService.getLocoDtoByProjectName("Translate IT 2");
    	transuDtos = locoService.listAllTransuDtos();
    	transuDtos.stream().forEach(t->System.out.println(t.getSourceSegm()));
    	System.out.println("*** Removing: Using transuService ***");
    	transut = transuService.getAllTransus();
    	transut.forEach(t->System.out.println(t.getSourceSegm()));
    	}
	
/*
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
    	   	
    	//Transu transuServiceTransu=transuService.getTransuById(5);
    	//transuServiceTransu.setLoco(loco); transuServiceTransu != oldTransuRow 

    	//transu=oldTransu;
    	//transu=oldTransuRow;
    	//System.out.println("Old value :" +  transu.getSourceSegm());
    	//System.out.println("*** Updating old one ***");
    	
    	transu=newTransu;
    	System.out.println("*** Adding a new one ***");
    	transu.setSourceSegm("sourceSegm 4");
    	transu.setTargetSegm("targetSegm 4");
    	transu.setLoco(loco);
    	loco=locoService.updateLoco(loco);
    	transu = loco.getTransuBySrcSegment(transu.getSourceSegm());
    	
    	transus = loco.getTransus();
    	transus.stream().forEach(t->System.out.println(t.getSourceSegm()));
    	System.out.println("***Adding: Using transuService ***");
    	Iterable <Transu> transut = transuService.getAllTransus();
    	transut.forEach(t->System.out.println(t.getSourceSegm()));

    	System.out.println("*** Reupdating the transu ***");
    	transu.setSourceSegm("No way ");
    	transu.setTargetSegm("Yeaah!");
    	transu.setLoco(loco);
    	loco=locoService.updateLoco(loco);
    	transu = loco.getTransuBySrcSegment(transu.getSourceSegm());
    	
    	transus = loco.getTransus();
    	transus.stream().forEach(t->System.out.println(t.getSourceSegm()));
    	System.out.println("*** Reupdating: Using transuService ***");
    	transut = transuService.getAllTransus();
    	transut.forEach(t->System.out.println(t.getSourceSegm()));
    	
    	System.out.println("*** Removing the transu ***");
    	//loco.removeTransu(transu);
    	transu.setLoco(null);
    	transu = null;
    	loco=locoService.updateLoco(loco);
    	
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
*/
	/*
	 * With CommandLineRunner you can perform tasks after all Spring Beans 
	 * are created and the Application Context has been created.
	 */
	@Bean
	public CommandLineRunner demo(TransuServiceImpl transuService,
			LocoServiceImpl locoService, ModelMapper modelMapper) {
		return (args) -> {	
			//testLoco(locoService, transuService);
			//testLocoDto(locoService, transuService);
			//testTransu(locoService, transuService);
			//testMapperLoco(modelMapper,locoService);
			//testMapperLocoDto(locoService);
			testMapperISO8859Data(locoService);
			testLocoDto2(locoService, transuService);
		};
	}

}
