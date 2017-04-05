package translateit2;

import javax.validation.Configuration;
import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorFactory;
import javax.validation.Validation;
import javax.validation.Validator;
import javax.validation.ValidatorFactory;

import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.MessageSource;
import org.springframework.context.annotation.Bean;
import org.springframework.context.support.ReloadableResourceBundleMessageSource;
import org.springframework.validation.beanvalidation.LocalValidatorFactoryBean;
import org.springframework.validation.beanvalidation.MethodValidationPostProcessor;

import translateit2.fileloader.storage.StorageProperties;
import translateit2.persistence.dao.LocoRepository;
import translateit2.service.LocoServiceImpl;
import translateit2.service.TransuServiceImpl;
import translateit2.validator.LocoValidator;

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
	
	public MessageSource messageSource()
	{
	    ReloadableResourceBundleMessageSource bean = 
	    		new ReloadableResourceBundleMessageSource();
	    bean.setBasename("classpath:messages");
	    bean.setDefaultEncoding("ISO-8859-1");
	    return bean;
	}
	
	@Bean
	public LocalValidatorFactoryBean validator() {
	    LocalValidatorFactoryBean bean = new LocalValidatorFactoryBean();
	    bean.setValidationMessageSource(messageSource());
	    return bean;
	}
	
    @Bean
    public MethodValidationPostProcessor methodValidationPostProcessor() {
         
        MethodValidationPostProcessor processor =
                new MethodValidationPostProcessor();
        processor.setValidator(validator());
        return processor;
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
