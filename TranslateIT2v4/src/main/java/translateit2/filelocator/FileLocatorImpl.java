package translateit2.filelocator;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.time.LocalDate;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import translateit2.fileloader.FileLoadError;
import translateit2.fileloader.FileLoader;
import translateit2.fileloader.FileLoaderException;
import translateit2.lngfileservice.LanguageFileFormat;

@Component
public class FileLocatorImpl implements FileLocator {
    
    private String rootDirectrory = "D:\\sw-tools\\STS\\translateit2testi\\TranslateIT2v4\\upload-dir4";
    
    @Autowired
    private FileLoader fileLoaderService;
    
    public void setRootDirectory(String rootDirectrory) {
        this.rootDirectrory = rootDirectrory;
    }
    
    @Override
    public Path moveUploadedFileIntoFilesystem(Path uploadedFile, 
            LanguageFileFormat format) throws FileLoaderException {
        
        // create new path for permanent file storage
        Path outFilePath = getUniquePath(format);
        Path dir = outFilePath.getParent();
        if (Files.notExists(dir))
            try {
                Files.createDirectory(dir);
            } catch (IOException e1) {
                throw new FileLoaderException(FileLoadError.CANNOT_CREATE_PERMANENT_DIRECTORY);
            }
        
        // and move the file from upload directory   
        try {
            Files.move(uploadedFile, outFilePath);
        } catch (IOException e) {
            throw new FileLoaderException(FileLoadError.CANNOT_MOVE_FILE);
        }       

        return outFilePath;
    }

    private Path getUniquePath(LanguageFileFormat format) {
        //Path test = fileLoaderService
                
        Path rootPath = Paths.get(rootDirectrory);
        
        Path fnamePath = Paths.get(java.util.UUID.randomUUID().toString());
        Path dirPath = Paths.get(LocalDate.now().toString());
        Path path = dirPath.resolve(fnamePath);
        path = path.resolveSibling(path.getFileName() + "." + format.toString());
        
        Path uniquePath = rootPath.resolve(path);
        
        return uniquePath;
    }
}