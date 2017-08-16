package translateit2.languagebeancache.validator;

import java.nio.file.Path;
import java.util.Locale;

import translateit2.fileloader.FileLoaderException;
import translateit2.languagefile.LanguageFile;
import translateit2.languagefile.LanguageFileFormat;
import translateit2.languagefile.LanguageFileType;

public interface LanguageFileValidator extends LanguageFile <LanguageFileFormat> {   
    void validateCharacterSet(Path uploadedFile, LanguageFileType expectedType) throws FileLoaderException;

    void validateApplicationName(String appName, String expectedApplicationName) throws FileLoaderException;

    void validateLocale(Locale appLocale, Locale expectedLocale) throws FileLoaderException;

}
