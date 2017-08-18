package translateit2.fileloader;

import java.nio.file.Path;
import java.util.stream.Stream;

import org.springframework.core.io.Resource;
import org.springframework.web.multipart.MultipartFile;

import translateit2.exception.TranslateIt2Exception;

public interface FileLoader {

    void deleteUploadedFile(Path filePath);

    void deleteUploadedFiles();

    Path getUploadPath(String filename);
    
    Path getDownloadPath(String filename);

    Stream<Path> getPathsOfDownloadableFiles() throws TranslateIt2Exception;

    Resource loadAsResource(String filename) throws TranslateIt2Exception;
    
    Path storeToUploadDirectory(MultipartFile file) throws TranslateIt2Exception;

    Stream <Path> storeToDownloadDirectory(Path temporaryFilePath, String downloadFilename) throws TranslateIt2Exception;
    
}
