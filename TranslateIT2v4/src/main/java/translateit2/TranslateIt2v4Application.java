package translateit2;

import java.io.IOException;
import java.util.List;
import org.modelmapper.ModelMapper;
import org.modelmapper.PropertyMap;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;

import translateit2.config.LocoServiceTestConfig;
import translateit2.fileloader.storage.StorageProperties;
import translateit2.persistence.dto.LocoDto;
import translateit2.persistence.dto.LocoMapper;
import translateit2.persistence.dto.TransuDto;
import translateit2.persistence.model.Loco;
import translateit2.persistence.model.Transu;
import translateit2.service.LocoServiceImpl;
import translateit2.service.TransuServiceImpl;
import translateit2.util.ISO8859Loader;

@SpringBootApplication 
@EnableConfigurationProperties(StorageProperties.class) 
public class TranslateIt2v4Application {

	// String[] appArgs = {"--debug"};
	// => *.run(appArgs);
	
	public static void main(String[] args) {
		SpringApplication.run(TranslateIt2v4Application.class, args);
	}

	PropertyMap<Transu, TransuDto> transuMap = new PropertyMap<Transu, TransuDto>() {
		  @Override
		protected void configure() {
		    map().setLoco(source.getLoco().getId());
		  }
	};
	
	@Bean
	public ModelMapper modelMapper() {
		ModelMapper mp = new ModelMapper();
		mp.addMappings(transuMap);
	    return mp;
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
	
	/*
	 * http://sivalabs.in/2016/03/how-springboot-autoconfiguration-magic/
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
	 * With CommandLineRunner you can perform tasks after all Spring Beans 
	 * are created and the Application Context has been created.
	 */
	@Bean
	public CommandLineRunner demo(TransuServiceImpl transuService,
			LocoServiceImpl locoService, ModelMapper modelMapper) {
		return (args) -> {	
		};
	}

}
