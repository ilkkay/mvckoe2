package translateit2.languagebeancache;

import org.springframework.stereotype.Component;

import translateit2.lngfileservice.LanguageFileFormat;

@Component
public class LanguageValidatorBeanCacheImpl extends LanguageBeanCacheImpl<LanguageFileFormat, LanguageFileValidator> {

}