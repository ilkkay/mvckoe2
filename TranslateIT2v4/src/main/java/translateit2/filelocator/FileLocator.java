package translateit2.filelocator;

import java.nio.charset.Charset;
import java.nio.file.Path;
import java.util.List;

import translateit2.exception.TranslateIt2Exception;
import translateit2.languagefile.LanguageFileFormat;

public interface FileLocator {
    
    Path createFileIntoPermanentFileSystem(List<String> downloadFileAsList, LanguageFileFormat format,  
            Charset charset) throws TranslateIt2Exception;
    
    Path moveUploadedFileIntoPermanentFileSystem(Path uploadedFile,
            LanguageFileFormat format) throws TranslateIt2Exception;
    
    void deleteFileFromPermanentFileSystem(Path fileToDeletePath);
    
}
