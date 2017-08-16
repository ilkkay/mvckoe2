package translateit2.languagebeancache;

import translateit2.languagefileservice.factory.LanguageFile;

public interface LanguageBeanCache <F, T extends LanguageFile <F>> 
    extends LanguageFileService <F, T> {
    
}