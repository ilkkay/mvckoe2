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

import translateit2.exception.TranslateIt2ErrorCode;
import translateit2.exception.TranslateIt2Exception;
import translateit2.service.LoadingContractorImpl;

@Component 
public class FileLoaderImpl implements FileLoader, ResourceLoaderAware {
    static final Logger logger = LogManager.getLogger(LoadingContractorImpl.class);

    private ResourceLoader resourceLoader;

    private final Path uploadLocation;

    private final Path downloadLocation;
    
    private final Path rootTemporaryLocation;

    @Autowired
    public FileLoaderImpl(FileLoaderProperties properties) {
        this.rootTemporaryLocation = Paths.get(properties.getRootTemporaryDirectory());
        this.uploadLocation = Paths.get(properties.getUploadLocation());
        this.downloadLocation = Paths.get(properties.getDownloadLocation());
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
        if (Files.notExists(getFullPath(uploadLocation)))
            try {
                Files.createDirectory(getFullPath(uploadLocation));
            } catch (IOException e) {
                throw new TranslateIt2Exception(TranslateIt2ErrorCode.CANNOT_CREATE_UPLOAD_DIRECTORY);
            }
        
        return getFullPath(uploadLocation).resolve(filename);
    }

    @Override
    public Path getDownloadPath(String filename) {
        if (Files.notExists(getFullPath(downloadLocation)))
            try {
                Files.createDirectory(getFullPath(downloadLocation));
            } catch (IOException e) {
                throw new TranslateIt2Exception(TranslateIt2ErrorCode.CANNOT_CREATE_DOWNLOAD_DIRECTORY);
            }
        
        return getFullPath(downloadLocation).resolve(filename);
    }

    @Override
    public Stream <Path> storeToDownloadDirectory(Path temporaryFilePath,String downloadFilename) {

        Path downloadFilePath = getDownloadPath(downloadFilename);   
        Path dir = downloadFilePath.getParent();
        if (Files.notExists(dir))
            try {
                Files.createDirectory(dir);
            } catch (IOException e1) {
                throw new TranslateIt2Exception(TranslateIt2ErrorCode.CANNOT_CREATE_PERMANENT_DIRECTORY);
            }

        if (Files.exists(temporaryFilePath))
            try {                                
                Files.move(temporaryFilePath, downloadFilePath,StandardCopyOption.REPLACE_EXISTING );
                return Files.walk(downloadFilePath);
            } catch (IOException e) {
                throw new TranslateIt2Exception(e.getCause());
            }        
        else
            throw new TranslateIt2Exception(TranslateIt2ErrorCode.FILE_NOT_FOUND);
    }

    @Override
    public Stream<Path> getPathsOfDownloadableFiles() { 
        try {
            return Files.walk(this.downloadLocation, 1).filter(path -> !path.equals(this.downloadLocation))
                    .map(path -> this.downloadLocation.relativize(path));
        } catch (IOException e) {
            throw new TranslateIt2Exception(e.getCause());
        }
    }

    @Override
    public Resource loadAsResource(String filename) {
        try {
            Path file = getUploadPath(filename);
            Resource resource = new UrlResource(file.toUri());

            // TODO: test alternatives
            // like applicationContext.getResource(filename);
            // or like Resource resource2 = resourceLoader.getResource(filename);

            if (resource.exists() || resource.isReadable()) {
                return resource;
            } else {
                throw new TranslateIt2Exception(TranslateIt2ErrorCode.FILE_NOT_FOUND);
            }

        } catch (MalformedURLException e) {
            //throw new LoadedFileNotFoundException(filename, e.getCause());
            throw new TranslateIt2Exception(TranslateIt2ErrorCode.FILE_NOT_FOUND, e.getCause());
        }        
    }

    @Override
    public void setResourceLoader(ResourceLoader resourceLoader) {
        this.resourceLoader = resourceLoader;
    }

    @Override
    public Path storeToUploadDirectory(MultipartFile file) {
        if (file.isEmpty()) 
            throw new TranslateIt2Exception(TranslateIt2ErrorCode.FILE_TOBELOADED_IS_EMPTY);

        Path outFilePath;
        try(InputStream in = file.getInputStream()) {    

            outFilePath = getUploadPath(file.getOriginalFilename());
            
            Files.copy(file.getInputStream(), outFilePath, StandardCopyOption.REPLACE_EXISTING);
        } catch (IOException e) {
            throw new TranslateIt2Exception(TranslateIt2ErrorCode.CANNOT_UPLOAD_FILE,e.getCause());
        }

        return outFilePath;
    }


    @PostConstruct
    private void init() {
        deleteUploadedFiles();

        try {
            if (Files.notExists(rootTemporaryLocation)) Files.createDirectory(rootTemporaryLocation);
        } catch (IOException e) {
            throw new TranslateIt2Exception(TranslateIt2ErrorCode.CANNOT_CREATE_ROOT_DIRECTORY, e.getCause());
        }
        
    }
    
    private Path getFullPath(Path p) {
        return rootTemporaryLocation.resolve(p);
    }
}
