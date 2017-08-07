package translateit2.languagefactory;

import java.nio.charset.Charset;
import java.nio.file.Path;
import java.util.HashMap;

import translateit2.languagefileservice.factory.AbstractLanguageFile;
import translateit2.lngfileservice.LanguageFileFormat;

public interface LanguageFileReader extends AbstractLanguageFile <LanguageFileFormat> {   
    public HashMap<String, String> getSegments(Path inputPath, Charset charset);

}
