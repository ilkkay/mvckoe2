package translateit2.fileloader;

import java.nio.file.Path;
import java.util.stream.Stream;

import org.springframework.core.io.Resource;
import org.springframework.web.multipart.MultipartFile;

public interface FileLoader {

    void deleteUploadedFile(String filename);

    void deleteUploadedFiles();

    Path getPath(String filename);

    Stream<Path> getPathsOfDownloadableFiles();

    Resource loadAsResource(String filename);
    
    Path storeToUploadDirectory(MultipartFile file);
}
