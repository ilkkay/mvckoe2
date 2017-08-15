package translateit2.filenameresolver;

import java.util.Locale;
import java.util.function.Predicate;

import translateit2.fileloader.FileLoaderException;
import translateit2.lngfileservice.LanguageFileFormat;

public interface FileNameResolver {
    
    public String getApplicationName(String filename) throws FileLoaderException;
        
    Locale getLocaleFromString(String fileName, Predicate<String> p) throws FileLoaderException;
    
    String getDownloadFilename(String originalFileName,Locale locale,LanguageFileFormat format);

    //String getUniqueFilename(String originalFileName,Locale locale,LanguageFileFormat format);
}
