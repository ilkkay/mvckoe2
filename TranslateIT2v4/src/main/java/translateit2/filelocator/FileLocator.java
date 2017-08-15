package translateit2.filelocator;

import java.nio.charset.Charset;
import java.nio.file.Path;
import java.util.List;

import translateit2.fileloader.FileLoaderException;
import translateit2.lngfileservice.LanguageFileFormat;

public interface FileLocator {
    Path moveUploadedFileIntoFilesystem(Path uploadedFile,
            LanguageFileFormat format) throws FileLoaderException;
    
    Path createTemporaryFile(List<String> downloadFileAsList, LanguageFileFormat format,  
            Charset charset) throws FileLoaderException;
    
    void deleteTemporaryFile(Path fileToDeletePath);
}
