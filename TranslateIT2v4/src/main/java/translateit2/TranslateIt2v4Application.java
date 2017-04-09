package translateit2;

import java.util.Locale;

import org.modelmapper.ModelMapper;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.MessageSource;
import org.springframework.context.annotation.Bean;
import org.springframework.context.support.ReloadableResourceBundleMessageSource;
import org.springframework.validation.beanvalidation.LocalValidatorFactoryBean;
import org.springframework.validation.beanvalidation.MethodValidationPostProcessor;
import org.springframework.web.servlet.LocaleResolver;
import org.springframework.web.servlet.i18n.AcceptHeaderLocaleResolver;
import org.springframework.web.servlet.i18n.FixedLocaleResolver;
import org.springframework.web.servlet.i18n.SessionLocaleResolver;

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
	
	// Implementation of LocaleResolver that uses a locale attribute 
	// in the user’s session in case of a custom setting, with a fallback 
	// to the specified default locale or the request’s accept-header locale”

	  
	@Bean
	public LocaleResolver localeResolver() {
	    SessionLocaleResolver slr = new SessionLocaleResolver();
	    slr.setDefaultLocale(new Locale("fi"));
	    return slr;
	}
	
	// should detect language based on browser. TODO: test it
	// Does not support setLocale, since the accept header can 
	// only be changed through changing the client's locale settings
	// => AcceptHeaderLocaleResolver will resolve the Locale from 
	// the request (using the accept header <= from the client's OS) 
	@Bean
	public AcceptHeaderLocaleResolver browserLocaleResolver() {
		AcceptHeaderLocaleResolver bean = new AcceptHeaderLocaleResolver();
		return bean;
	}
	
	@Bean
	public MessageSource messageSource()
	{
	    ReloadableResourceBundleMessageSource bean = 
	    		new ReloadableResourceBundleMessageSource();
	    bean.setBasename("classpath:messages");
	    bean.setDefaultEncoding("ISO-8859-1");
	    
	    // if this is turned false, the only
		// fallback will be the default file (e.g. "messages.properties" for
		// basename "messages").
	    bean.setFallbackToSystemLocale(false);
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
