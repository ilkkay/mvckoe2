package translateit2.fileloader;

import org.springframework.core.io.Resource;
import org.springframework.web.multipart.MultipartFile;

import java.nio.file.Path;
import java.util.stream.Stream;

import javax.validation.constraints.NotNull;

public interface FileLoader {

    Path storeToUploadDirectory(MultipartFile file);

    Stream<Path> getPathsOfDownloadableFiles();

    Path getPath(String filename);

    Resource loadAsResource(String filename);

    void deleteUploadedFiles();
    
    void deleteUploadedFile(String filename);
}
