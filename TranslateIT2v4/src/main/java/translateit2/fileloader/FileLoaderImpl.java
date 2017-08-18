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

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ResourceLoaderAware;
import org.springframework.core.io.Resource;
import org.springframework.core.io.ResourceLoader;
import org.springframework.core.io.UrlResource;
import org.springframework.stereotype.Component;
import org.springframework.util.FileSystemUtils;
import org.springframework.web.multipart.MultipartFile;

import translateit2.exception.TranslateIt2Error;
import translateit2.exception.TranslateIt2Exception;
import translateit2.service.LoadingContractorImpl;

@Component // oli service mutta miksi
public class FileLoaderImpl implements FileLoader, ResourceLoaderAware {
    static final Logger logger = LogManager.getLogger(LoadingContractorImpl.class);

    private ResourceLoader resourceLoader;

    private final Path uploadLocation;

    private final Path downloadLocation;

    @Autowired
    public FileLoaderImpl(FileLoaderProperties properties) {
        this.uploadLocation = Paths.get(properties.getLocation());
        this.downloadLocation = Paths.get(properties.getLocation());
    }

    @Override
    public void deleteUploadedFile(Path fileToDeletePath) {
        try {
            Files.deleteIfExists(fileToDeletePath);
        } catch (IOException e) {
            logger.warn("Could not remove file: {}", fileToDeletePath.toAbsolutePath().toString());
        }
    }

    @Override
    public void deleteUploadedFiles() {
        FileSystemUtils.deleteRecursively(uploadLocation.toFile());
    }

    @Override
    public Path getUploadPath(String filename) {
        return uploadLocation.resolve(filename);
    }

    @Override
    public Path getDownloadPath(String filename) {
        return downloadLocation.resolve(filename);
    }

    @Override
    public Stream <Path> storeToDownloadDirectory(Path temporaryFilePath,String downloadFilename) throws TranslateIt2Exception {

        Path downloadFilePath = getDownloadPath(downloadFilename);   
        Path dir = downloadFilePath.getParent();
        if (Files.notExists(dir))
            try {
                Files.createDirectory(dir);
            } catch (IOException e1) {
                throw new TranslateIt2Exception(TranslateIt2Error.CANNOT_CREATE_PERMANENT_DIRECTORY);
            }

        if (Files.exists(temporaryFilePath))
            try {                                
                Files.move(temporaryFilePath, downloadFilePath,StandardCopyOption.REPLACE_EXISTING );
                return Files.walk(downloadFilePath);
            } catch (IOException e) {
                throw new TranslateIt2Exception(e.getCause());
            }        
        else
            throw new TranslateIt2Exception(TranslateIt2Error.FILE_NOT_FOUND);
    }

    @Override
    public Stream<Path> getPathsOfDownloadableFiles() throws TranslateIt2Exception { 
        try {
            return Files.walk(this.downloadLocation, 1).filter(path -> !path.equals(this.downloadLocation))
                    .map(path -> this.downloadLocation.relativize(path));
        } catch (IOException e) {
            //throw new CannotReadFileException(e.getCause());
            throw new TranslateIt2Exception(e.getCause());
        }
    }

    @Override
    public Resource loadAsResource(String filename) throws TranslateIt2Exception {
        try {
            Path file = getUploadPath(filename);
            Resource resource = new UrlResource(file.toUri());

            // TODO: test alternatives
            // like applicationContext.getResource(filename);
            // or like Resource resource2 = resourceLoader.getResource(filename);

            if (resource.exists() || resource.isReadable()) {
                return resource;
            } else {
                //throw new LoadedFileNotFoundException(filename);
                throw new TranslateIt2Exception(TranslateIt2Error.FILE_NOT_FOUND);
            }

        } catch (MalformedURLException e) {
            //throw new LoadedFileNotFoundException(filename, e.getCause());
            throw new TranslateIt2Exception(TranslateIt2Error.FILE_NOT_FOUND, e.getCause());
        }        
    }

    @Override
    public void setResourceLoader(ResourceLoader resourceLoader) {
        this.resourceLoader = resourceLoader;
    }

    @Override
    public Path storeToUploadDirectory(MultipartFile file) throws TranslateIt2Exception {
        if (file.isEmpty()) 
            //throw new FileToLoadIsEmptyException(file.getOriginalFilename());
            //throw new LoadedFileNotFoundException(file.getOriginalFilename());
            throw new TranslateIt2Exception(TranslateIt2Error.FILE_TOBELOADED_IS_EMPTY);

        Path outFilePath;
        try(InputStream in = file.getInputStream()) {    

            // make sure that you have a directory where to upload
            if (Files.notExists(uploadLocation)) Files.createDirectory(uploadLocation);
            if (Files.notExists(uploadLocation)) 
                //throw new CannotCreateUploadDirectoryException(rootLocation.toString());
                throw new TranslateIt2Exception(TranslateIt2Error.CANNOT_CREATE_UPLOAD_DIRECTORY);

            outFilePath = this.uploadLocation.resolve(file.getOriginalFilename());
            Files.copy(file.getInputStream(), outFilePath, StandardCopyOption.REPLACE_EXISTING);
        } catch (IOException e) {
            //throw new CannotUploadFileException(file.getOriginalFilename(), e);
            throw new TranslateIt2Exception(TranslateIt2Error.CANNOT_UPLOAD_FILE,e.getCause());
        }

        return outFilePath;
    }


    @PostConstruct
    private void init() throws TranslateIt2Exception {
        try {
            deleteUploadedFiles();

            if (Files.notExists(uploadLocation)) Files.createDirectory(uploadLocation);            
        } catch (IOException e) {
            //throw new CannotCreateUploadDirectoryException(rootLocation.toString(), e.getCause());
            throw new TranslateIt2Exception(TranslateIt2Error.CANNOT_CREATE_UPLOAD_DIRECTORY, e.getCause());
        }
    }
}
