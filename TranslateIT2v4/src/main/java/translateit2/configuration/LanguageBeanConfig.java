package translateit2.configuration;

import org.springframework.context.annotation.Bean;

import translateit2.languagefactory.LanguageBeanCache;
import translateit2.languagefactory.LanguageBeanCacheImpl;
import translateit2.languagefactory.LanguageFileReader;
import translateit2.languagefactory.LanguageFileValidator;
import translateit2.languagefactory.LanguageFileWriter;
import translateit2.lngfileservice.LanguageFileFormat;

//@Configuration
public class LanguageBeanConfig {
    
    @Bean
    public LanguageBeanCache<LanguageFileValidator, LanguageFileFormat> LanguageBeanValidatorCache() {
        LanguageBeanCache<LanguageFileValidator, LanguageFileFormat> bean = new LanguageBeanCacheImpl<LanguageFileValidator, LanguageFileFormat>();
        return bean;        
    }
     
    @Bean
    public LanguageBeanCache<LanguageFileReader, LanguageFileFormat> LanguageBeanReaderCache() {
        LanguageBeanCache<LanguageFileReader, LanguageFileFormat> bean = new LanguageBeanCacheImpl<LanguageFileReader, LanguageFileFormat>();
        return bean;        
    }
    
    @Bean
    public LanguageBeanCache<LanguageFileWriter, LanguageFileFormat> LanguageBeanWriterCache() {
        LanguageBeanCache<LanguageFileWriter, LanguageFileFormat> bean = new LanguageBeanCacheImpl<LanguageFileWriter, LanguageFileFormat>();
        return bean;        
    }
    

}