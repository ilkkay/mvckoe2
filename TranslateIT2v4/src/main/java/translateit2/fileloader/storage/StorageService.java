package translateit2.fileloader.storage;

import org.springframework.core.io.Resource;
import org.springframework.web.multipart.MultipartFile;

import java.nio.file.Path;
import java.util.stream.Stream;

import javax.validation.constraints.NotNull;

public interface StorageService {

    void init();

    Path store(MultipartFile file);

    Stream<Path> loadAll();

    Path load(String filename);

    Resource loadAsResource(String file);

    void deleteAll();
    
    //TODO: use of an external tool is assumed here 
    Path getUniquePath(@NotNull String extension);
    
    String getFileType(Path lngFile);

}
