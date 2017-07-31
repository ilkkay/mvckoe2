package translateit2.configuration;

import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;
import java.util.Locale;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.MessageSource;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.support.ReloadableResourceBundleMessageSource;
import org.springframework.validation.beanvalidation.LocalValidatorFactoryBean;
import org.springframework.validation.beanvalidation.MethodValidationPostProcessor;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;
import org.springframework.web.filter.CorsFilter;
import org.springframework.web.servlet.LocaleResolver;
import org.springframework.web.servlet.i18n.AcceptHeaderLocaleResolver;
import org.springframework.web.servlet.i18n.SessionLocaleResolver;

import translateit2.lngfileservice.LanguageFileType;
import translateit2.persistence.dao.ProjectRepository;
import translateit2.persistence.dto.ProjectDto;
import translateit2.persistence.model.Project;
import translateit2.persistence.model.Work;

@Configuration
public class LocaleResolverConfig {	
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
	public CharSetResolver charSetResolver() {		
		CharSetResolver bean = new CharSetResolver();
		return bean;
	}
	
}