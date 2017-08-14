package translateit2.languagebeancache;

import java.nio.file.Path;
import java.util.Locale;

import translateit2.fileloader.FileLoaderException;
import translateit2.languagefileservice.factory.LanguageFile;
import translateit2.lngfileservice.LanguageFileFormat;
import translateit2.lngfileservice.LanguageFileType;

public interface LanguageFileValidator extends LanguageFile <LanguageFileFormat> {   
    void validateCharacterSet(Path uploadedFile, LanguageFileType expectedType) throws FileLoaderException;

    void validateApplicationName(String appName, String expectedApplicationName) throws FileLoaderException;

    void validateLocale(Locale appLocale, Locale expectedLocale) throws FileLoaderException;

}
