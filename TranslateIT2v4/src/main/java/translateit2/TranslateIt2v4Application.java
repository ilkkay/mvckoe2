package translateit2;

import org.modelmapper.ModelMapper;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.validation.beanvalidation.LocalValidatorFactoryBean;

import translateit2.fileloader.storage.StorageProperties;
import translateit2.service.LocoServiceImpl;
import translateit2.service.TransuServiceImpl;

@SpringBootApplication 
@EnableConfigurationProperties(StorageProperties.class) 
public class TranslateIt2v4Application {

	// String[] appArgs = {"--debug"};
	// => *.run(appArgs);
	
	public static void main(String[] args) {
		SpringApplication.run(TranslateIt2v4Application.class, args);
	}

	/*
	 * http://sivalabs.in/2016/03/how-springboot-autoconfiguration-magic/
	 */				   
	
	@Bean
	public LocalValidatorFactoryBean validator() {
		return new LocalValidatorFactoryBean();
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
