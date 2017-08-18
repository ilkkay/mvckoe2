package translateit2.filelocator;

import java.io.IOException;
import java.nio.charset.Charset;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.time.LocalDate;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import translateit2.fileloader.FileLoadError;
import translateit2.fileloader.FileLoader;
import translateit2.fileloader.FileLoaderException;
import translateit2.languagefile.LanguageFileFormat;

@Component
public class FileLocatorImpl implements FileLocator {
    
    private String uploadDirectory = "D:\\sw-tools\\STS\\translateit2testi\\TranslateIT2v4\\upload-dir4";
    
    private String downloadDirectory = "D:\\sw-tools\\STS\\translateit2testi\\TranslateIT2v4\\upload-dir4";

    @Autowired
    private FileLoader fileLoaderService;
    
    public void setUploadDirectory(String uploadDirectory) {
        this.uploadDirectory = uploadDirectory;
    }

    public void setDownloadDirectory(String downloadDirectory) {
        this.downloadDirectory = downloadDirectory;
    }

    @Override
    public Path moveUploadedFileIntoPermanentFileSystem(Path uploadedFile, 
            LanguageFileFormat format) throws FileLoaderException {
        
        // create new path for permanent file storage
        Path outFilePath = getUniquePath(format, uploadDirectory);
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

    private Path getUniquePath(LanguageFileFormat format, String rootDirectrory) {
        //Path test = fileLoaderService
                
        Path rootPath = Paths.get(rootDirectrory);
        
        Path fnamePath = Paths.get(java.util.UUID.randomUUID().toString());
        Path dirPath = Paths.get(LocalDate.now().toString());
        Path path = dirPath.resolve(fnamePath);
        path = path.resolveSibling(path.getFileName() + "." + format.toString());
        
        Path uniquePath = rootPath.resolve(path);
        
        return uniquePath;
    }

    @Override
    public void deleteFileFromPermanentFileSystem(Path fileToDeletePath) {
        try {
            Files.deleteIfExists(fileToDeletePath);
        } catch (IOException e) {
            //logger.warn("Could not remove file: {}", fileToDeletePath.toAbsolutePath().toString());
        }
    }
    
    @Override
    public Path createFileIntoPermanentFileSystem(List<String> downloadFileAsList, 
            LanguageFileFormat format, Charset charset) throws FileLoaderException {
        
        // create new path for temporary file in permanent storage
        Path outFilePath = getUniquePath(format, uploadDirectory);
        Path dir = outFilePath.getParent();
        if (Files.notExists(dir))
            try {
                Files.createDirectory(dir);
            } catch (IOException e) {
                throw new FileLoaderException(FileLoadError.CANNOT_CREATE_PERMANENT_DIRECTORY);
            }
        
        // and write contents to file   
        try {
            return Files.write(outFilePath,downloadFileAsList, charset);
        } catch (IOException e) {
            throw new FileLoaderException(FileLoadError.CANNOT_CREATE_FILE);
        }       

    }
}
