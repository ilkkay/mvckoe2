package translateit2.languagefileservice.factory;

import java.util.List;
import java.util.Optional;

import translateit2.languagebeancache.LanguageFileServiceCache;
import translateit2.lngfileservice.LanguageFileFormat;
import translateit2.lngfileservice.LanguageFileStorage;

public interface LanguageFileServiceFactory extends LanguageFileServiceCache 
        <LanguageFileStorage, LanguageFileFormat> {
    @Override
    Optional<LanguageFileStorage> getService(LanguageFileFormat type);

    @Override
    List<LanguageFileFormat> listFormatsSupported();
}
