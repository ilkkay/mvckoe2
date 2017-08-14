package translateit2.languagebeancache;

import java.util.List;
import java.util.Optional;

import translateit2.lngfileservice.LanguageFileFormat;
import translateit2.lngfileservice.LanguageFileStorage;

public interface LanguageFileServiceCache <F, T> {
    Optional<T> getService(F format);

    List<LanguageFileFormat> listFormatsSupported();

    Optional<LanguageFileStorage> getService(LanguageFileFormat type);
}
