package translateit2;

import java.nio.file.Path;
import java.nio.file.Paths;
import java.time.LocalDate;
import java.util.Arrays;
import java.util.Locale;

import javax.annotation.PostConstruct;

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
import org.springframework.web.servlet.i18n.SessionLocaleResolver;

import translateit2.fileloader.storage.StorageProperties;
import translateit2.lngfileservice.LanguageFileFormat;
import translateit2.lngfileservice.LanguageFileType;
import translateit2.persistence.dto.InfoDto;
import translateit2.persistence.dto.PersonDto;
import translateit2.persistence.dto.ProjectDto;
import translateit2.persistence.dto.TranslatorGroupDto;
import translateit2.persistence.dto.WorkDto;
import translateit2.persistence.model.Priority;
import translateit2.service.ProjectService;

@SpringBootApplication 
@EnableConfigurationProperties(StorageProperties.class) 
public class TranslateIt2v4Application {

	static String[] appArgs = {"--debug"};
	// => *.run(appArgs);
	
	public static void main(String[] args) {	
		String newArgs[] = Arrays.copyOf(args, args.length);
		//newArgs[args.length] = "--debug";
		//newArgs[args.length ] = "-Dlog4j.configuration=file:logconf/log4j2.properties";
		// -Dlog4j.configuration="file:d:\log4j.xml"
		//newArgs[args.length ] = "-Dlog4j.configurationFile=\"log4j2-spring.xml\"";
		//Path p= Paths.get("logconf/log4j2.xml");
		//String d = p.toAbsolutePath().toString();
		//d="/sw-tools/STS/translateit2testi/TranslateIT2v4/src/main/resources/logconf/log4j2.xml";
		//newArgs[args.length ] = "-Dlog4j.configuration=file:" + d;
			
		SpringApplication.run(TranslateIt2v4Application.class, newArgs);
	}

	/*
	 * http://sivalabs.in/2016/03/how-springboot-autoconfiguration-magic/
	 */				   
/*	
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
*/
/*	
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
*/
    // TODO:
    @PostConstruct
    private void initializeApplication () {
    	// tee bean jossa Autowired projectService ja tekee saman kuin alla
    	
    		
    }
    
	/*
	 * With CommandLineRunner you can perform tasks after all Spring Beans 
	 * are created and the Application Context has been created.
	 */
	@Bean
	public CommandLineRunner demo(ModelMapper modelMapper,
			//WorkService workService,
			ProjectService projectService) {
		return (args) -> {
			PersonDto personDto = new PersonDto();
			personDto.setFullName("Ilkka");
			personDto = projectService.createPersonDto(personDto);
			
			InfoDto infoDto = new InfoDto();
			infoDto.setText("This is info");
			infoDto = projectService.createInfoDto(infoDto);
			
			TranslatorGroupDto groupDto = new TranslatorGroupDto();
			groupDto.setName("Group name");
			groupDto = projectService.createGroupDto(groupDto);
			
			long startCount = projectService.getProjectDtoCount(0);
			
			ProjectDto prj = new ProjectDto();
			prj.setName("Translate IT 2"); 
			prj.setSourceLocale(new Locale("en_EN"));
			prj.setFormat(LanguageFileFormat.PROPERTIES);
			prj.setType(LanguageFileType.UTF_8);
			prj.setPersonId(personDto.getId());
			prj.setInfoId(infoDto.getId());
			prj=projectService.createProjectDto(prj); 		
			
			// create Work
			WorkDto work = new WorkDto();
			work.setProjectId(prj.getId());
			work.setGroupId(666L);
			work.setLocale(new Locale("fi_FI"));
			work.setVersion("0.07");
			//work.setOriginalFile("dotcms");
			work.setSkeletonFile("skeleton file");
			//work.setStatus(Status.NEW); not yet!!
			work.setPriority(Priority.HIGH);
			work.setStarted(LocalDate.now());
			//LocalDate finishedDate = LocalDate.parse("2017-05-22");
			//work.setFinished(finishedDate); not yet!!
			LocalDate currentDate = LocalDate.now(); 
			LocalDate deadLine = currentDate.plusMonths(2L);
			deadLine = LocalDate.parse("2017-05-22");	
			work.setDeadLine(deadLine);
			work.setProgress(0.0);
			work.setGroupId(groupDto.getId());
			work=projectService.createWorkDto(work);	
		};
	}

}
