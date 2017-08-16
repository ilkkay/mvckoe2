package translateit2.languagebeancache;

import org.springframework.stereotype.Component;

import translateit2.languagefile.LanguageFileFormat;

@Component
public class LanguageReaderBeanCacheImpl extends LanguageBeanCacheImpl<LanguageFileFormat, LanguageFileReader> {

}