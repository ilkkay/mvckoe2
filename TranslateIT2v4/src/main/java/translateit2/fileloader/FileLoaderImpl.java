package translateit2.fileloader;

import java.io.IOException;
import java.io.InputStream;
import java.net.MalformedURLException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.util.stream.Stream;

import javax.annotation.PostConstruct;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ResourceLoaderAware;
import org.springframework.core.io.Resource;
import org.springframework.core.io.ResourceLoader;
import org.springframework.core.io.UrlResource;
import org.springframework.stereotype.Component;
import org.springframework.util.FileSystemUtils;
import org.springframework.web.multipart.MultipartFile;

@Component // oli service mutta miksi
public class FileLoaderImpl implements FileLoader, ResourceLoaderAware {

    private ResourceLoader resourceLoader;

    private final Path rootLocation;

    @Autowired
    public FileLoaderImpl(FileLoaderProperties properties) {
        this.rootLocation = Paths.get(properties.getLocation());
    }

    @Override
    public void deleteUploadedFile(String filename) {
        // TODO Auto-generated method stub
        
    }

    @Override
    public void deleteUploadedFiles() {
        FileSystemUtils.deleteRecursively(rootLocation.toFile());
    }

    @Override
    public Path getPath(String filename) {
        return rootLocation.resolve(filename);
    }

    @Override
    public Stream<Path> getPathsOfDownloadableFiles() throws FileLoaderException { 
        try {
            return Files.walk(this.rootLocation, 1).filter(path -> !path.equals(this.rootLocation))
                    .map(path -> this.rootLocation.relativize(path));
        } catch (IOException e) {
            //throw new CannotReadFileException(e.getCause());
            throw new FileLoaderException(e.getCause());
        }
    }

    @Override
    public Resource loadAsResource(String filename) throws FileLoaderException {
        try {
            Path file = getPath(filename);
            Resource resource = new UrlResource(file.toUri());

            // TODO: test alternatives
            // like applicationContext.getResource(filename);
            // or like Resource resource2 = resourceLoader.getResource(filename);

            if (resource.exists() || resource.isReadable()) {
                return resource;
            } else {
                //throw new LoadedFileNotFoundException(filename);
                throw new FileLoaderException(FileLoadError.FILE_NOT_FOUND);
            }

        } catch (MalformedURLException e) {
            //throw new LoadedFileNotFoundException(filename, e.getCause());
            throw new FileLoaderException(FileLoadError.FILE_NOT_FOUND, e.getCause());
        }        
    }

    @Override
    public void setResourceLoader(ResourceLoader resourceLoader) {
        this.resourceLoader = resourceLoader;
    }

    @Override
    public Path storeToUploadDirectory(MultipartFile file) throws FileLoaderException {
        if (file.isEmpty()) 
            //throw new FileToLoadIsEmptyException(file.getOriginalFilename());
          //throw new LoadedFileNotFoundException(file.getOriginalFilename());
          throw new FileLoaderException(FileLoadError.FILE_TOBELOADED_IS_EMPTY);

        Path outFilePath;
        try(InputStream in = file.getInputStream()) {    
            
            // make sure that you have a directory where to upload
            if (Files.notExists(rootLocation)) Files.createDirectory(rootLocation);
            if (Files.notExists(rootLocation)) 
                //throw new CannotCreateUploadDirectoryException(rootLocation.toString());
                throw new FileLoaderException(FileLoadError.CANNOT_CREATE_UPLOAD_DIRECTORY);

            outFilePath = this.rootLocation.resolve(file.getOriginalFilename());
            Files.copy(file.getInputStream(), outFilePath, StandardCopyOption.REPLACE_EXISTING);
        } catch (IOException e) {
            //throw new CannotUploadFileException(file.getOriginalFilename(), e);
            throw new FileLoaderException(FileLoadError.CANNOT_UPLOAD_FILE,e.getCause());
        }

        return outFilePath;
    }


    @PostConstruct
    private void init() throws FileLoaderException {
        try {
            deleteUploadedFiles();
            
            if (Files.notExists(rootLocation)) Files.createDirectory(rootLocation);            
        } catch (IOException e) {
            //throw new CannotCreateUploadDirectoryException(rootLocation.toString(), e.getCause());
            throw new FileLoaderException(FileLoadError.CANNOT_CREATE_UPLOAD_DIRECTORY, e.getCause());
        }
    }
}