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
import org.springframework.web.servlet.LocaleContextResolver;
import org.springframework.web.servlet.LocaleResolver;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.i18n.CookieLocaleResolver;
import org.springframework.web.servlet.i18n.LocaleChangeInterceptor;
import org.springframework.web.servlet.i18n.SessionLocaleResolver;

import translateit2.fileloader.storage.StorageProperties;
import translateit2.service.TransuServiceImpl;

@SpringBootApplication 
@EnableConfigurationProperties(StorageProperties.class) 
public class IntegrationTestMockApplication {

	// String[] appArgs = {"--debug"};
	// => *.run(appArgs);

	public static void main(String[] args) {
		
		// who is is driving over this
		Locale.setDefault(Locale.ENGLISH);
		
		SpringApplication.run(IntegrationTestMockApplication.class, args);
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
	    slr.setDefaultLocale(Locale.ENGLISH);
	    return slr;
	}
	
	@Bean
	public MessageSource messageSource()
	{
		ReloadableResourceBundleMessageSource bean = 
				new ReloadableResourceBundleMessageSource();
		bean.setBasename("classpath:messages");
		bean.setDefaultEncoding("ISO-8859-1");
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
	public CommandLineRunner demo(TransuServiceImpl transuService, ModelMapper modelMapper) {
		return (args) -> {	
		};
	}

}