package translateit2.fileloader;

import java.nio.file.Path;
import java.util.stream.Stream;

import org.springframework.core.io.Resource;
import org.springframework.web.multipart.MultipartFile;

public interface FileLoader {

    void deleteUploadedFile(Path filePath);

    void deleteUploadedFiles();

    Path getUploadPath(String filename);
    
    Path getDownloadPath(String filename);

    Stream<Path> getPathsOfDownloadableFiles() throws FileLoaderException;

    Resource loadAsResource(String filename) throws FileLoaderException;
    
    Path storeToUploadDirectory(MultipartFile file) throws FileLoaderException;

    Stream <Path> storeToDownloadDirectory(Path temporaryFilePath, String downloadFilename) throws FileLoaderException;
    
}
