package translateit2.languagefactory;

import translateit2.languagefileservice.factory.AbstractLanguageFile;
import translateit2.lngfileservice.LanguageFileFormat;

public interface LanguageFileReader extends AbstractLanguageFile <LanguageFileFormat> {   
    void read();
}
