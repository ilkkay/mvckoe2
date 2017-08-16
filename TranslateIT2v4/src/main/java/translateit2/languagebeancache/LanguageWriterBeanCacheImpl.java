package translateit2.languagebeancache;

import org.springframework.stereotype.Component;

import translateit2.lngfileservice.LanguageFileFormat;

@Component
public class LanguageWriterBeanCacheImpl extends LanguageBeanCacheImpl<LanguageFileFormat, LanguageFileWriter> {

}