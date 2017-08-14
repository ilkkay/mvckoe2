package translateit2.configuration;

import org.springframework.context.annotation.Bean;

import translateit2.languagebeancache.LanguageBeanCache;
import translateit2.languagebeancache.LanguageBeanCacheImpl;
import translateit2.languagebeancache.LanguageFileReader;
import translateit2.languagebeancache.LanguageFileValidator;
import translateit2.languagebeancache.LanguageFileWriter;
import translateit2.lngfileservice.LanguageFileFormat;

//@Configuration
public class LanguageBeanConfig {
    
    @Bean
    public LanguageBeanCache<LanguageFileFormat, LanguageFileValidator> LanguageBeanValidatorCache() {
        LanguageBeanCache<LanguageFileFormat, LanguageFileValidator> bean = new LanguageBeanCacheImpl<LanguageFileFormat, LanguageFileValidator>();
        return bean;        
    }
     
    @Bean
    public LanguageBeanCache<LanguageFileFormat, LanguageFileReader> LanguageBeanReaderCache() {
        LanguageBeanCache<LanguageFileFormat, LanguageFileReader> bean = new LanguageBeanCacheImpl<LanguageFileFormat, LanguageFileReader>();
        return bean;        
    }
    
    @Bean
    public LanguageBeanCache<LanguageFileFormat, LanguageFileWriter> LanguageBeanWriterCache() {
        LanguageBeanCache<LanguageFileFormat, LanguageFileWriter> bean = new LanguageBeanCacheImpl<LanguageFileFormat, LanguageFileWriter>();
        return bean;        
    }
    

}
