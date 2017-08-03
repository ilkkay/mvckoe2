package translateit2.languagefileservice.factory;

import java.util.List;
import java.util.Optional;

import translateit2.languagefactory.AbstractLanguageFileFactory;
import translateit2.lngfileservice.LanguageFileFormat;
import translateit2.lngfileservice.LanguageFileStorage;

public interface LanguageFileServiceFactory extends AbstractLanguageFileFactory 
        <LanguageFileStorage, LanguageFileFormat> {
    Optional<LanguageFileStorage> getService(LanguageFileFormat type);

    List<LanguageFileFormat> listFormatsSupported();
}
