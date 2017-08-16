package translateit2.languagebeancache.reader;

import java.nio.charset.Charset;
import java.nio.file.Path;
import java.util.HashMap;
import java.util.List;

import translateit2.fileloader.FileLoaderException;
import translateit2.languagefile.LanguageFile;
import translateit2.languagefile.LanguageFileFormat;

public interface LanguageFileReader extends LanguageFile <LanguageFileFormat> {   
    
    HashMap<String, String> getSegments(Path inputPath, Charset charset) throws FileLoaderException;
    
    List<String> getOriginalFileAsList(Path storedOriginalFile, Charset charSet) throws FileLoaderException;

}
