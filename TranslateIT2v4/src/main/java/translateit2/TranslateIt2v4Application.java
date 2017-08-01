package translateit2;

import java.nio.file.Path;
import java.nio.file.Paths;
import java.time.LocalDate;
import java.util.Arrays;
import java.util.Locale;

import javax.annotation.PostConstruct;

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
import org.springframework.web.servlet.LocaleResolver;
import org.springframework.web.servlet.i18n.AcceptHeaderLocaleResolver;
import org.springframework.web.servlet.i18n.SessionLocaleResolver;

import translateit2.configuration.DatabaseInitializer;
import translateit2.configuration.DemoDatabaseInitializer;
import translateit2.fileloader.storage.FileLoaderProperties;
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
@EnableConfigurationProperties(FileLoaderProperties.class)
public class TranslateIt2v4Application {

    @Autowired
    private DatabaseInitializer demoDatabaseInitializer;
    
    static String[] additionalArgs = { };
    
    public static void main(String[] args) {
       
        String newArgs[] = Arrays.copyOf(args, args.length + additionalArgs.length);
        for (int i = 0 ; i < additionalArgs.length ; i++) newArgs[args.length + i] = additionalArgs[i];

        SpringApplication.run(TranslateIt2v4Application.class, newArgs);
    }

    @PostConstruct
    private void initializeApplication() {
        demoDatabaseInitializer.loadDemo();
    }

    /*
     * With CommandLineRunner you can perform tasks after all Spring Beans are
     * created and the Application Context has been created. Used for reading command line parameters.
        @Bean
        public CommandLineRunner demo() {
            return (args) -> {};
        }
     */
}
