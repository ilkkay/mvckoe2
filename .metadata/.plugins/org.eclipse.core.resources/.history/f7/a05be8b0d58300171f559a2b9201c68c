package translateit2.filelocator;

import java.nio.charset.Charset;
import java.nio.file.Path;
import java.util.List;

import translateit2.fileloader.FileLoaderException;
import translateit2.languagefile.LanguageFileFormat;

public interface FileLocator {
    
    Path createFileIntoPermanentFileSystem(List<String> downloadFileAsList, LanguageFileFormat format,  
            Charset charset) throws FileLoaderException;
    
    Path moveUploadedFileIntoPermanentFileSystem(Path uploadedFile,
            LanguageFileFormat format) throws FileLoaderException;
    
    void deleteFileFromPermanentFileSystem(Path fileToDeletePath);
    
}
