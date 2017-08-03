package translateit2.languagefactory;

import translateit2.languagefileservice.factory.AbstractLanguageFile;
import translateit2.lngfileservice.LanguageFileFormat;

public interface LanguageFileWriter extends AbstractLanguageFile <LanguageFileFormat> {
    void write();
}
