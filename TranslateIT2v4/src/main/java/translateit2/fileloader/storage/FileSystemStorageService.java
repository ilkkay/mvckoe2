package translateit2.fileloader.storage;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.Resource;
import org.springframework.core.io.UrlResource;
import org.springframework.stereotype.Service;
import org.springframework.util.FileSystemUtils;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.io.InputStream;
import java.net.MalformedURLException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.time.LocalDate;
import java.util.stream.Stream;

import javax.validation.constraints.NotNull;

@Service
public class FileSystemStorageService implements StorageService {

    private final Path rootLocation;

    @Autowired
    public FileSystemStorageService(StorageProperties properties) {
        this.rootLocation = Paths.get(properties.getLocation());
    }

    @Override
    public Path store(MultipartFile file) throws StorageException {
    	Path outFilePath = null;
        try {
            if (file.isEmpty()) {
                throw new StorageException("Failed to store empty file " + file.getOriginalFilename());
            }
            InputStream in = file.getInputStream();
			if (in == null) {
				throw new StorageException("File:" + file.getOriginalFilename() + " not found");
			}
			Path target = this.rootLocation.resolve(file.getOriginalFilename()); 			

            // if (!Files.isDirectory(path))
            //		Files.createDirectory(path);
			Path absTgtParent = target.toAbsolutePath().getParent();
			 if (!Files.exists(absTgtParent)) 
				 Files.createDirectory(target);

            if (!Files.exists(absTgtParent)) {
                throw new StorageException("Upload directory " + absTgtParent.toString() + 
                		" was missing and could not be recreated");
            }
            
            /*
            if (Files.exists(target)) {
                boolean success = Files.deleteIfExists(target);
                if (!success) {
                	throw new StorageException("File " + target.toString() + 
                		" existed already and could not be removed");
                }
            }
            */

            outFilePath = this.rootLocation.resolve(file.getOriginalFilename());           
            Files.copy(file.getInputStream(), outFilePath,StandardCopyOption.REPLACE_EXISTING);            
            
        } catch (IOException e) {
            throw new StorageException("Failed to store file " + file.getOriginalFilename(), e);
        }
        
        return outFilePath;
    }
    
    @Override
    public Stream<Path> loadAll() {
        try {
            return Files.walk(this.rootLocation, 1)
                    .filter(path -> !path.equals(this.rootLocation))
                    .map(path -> this.rootLocation.relativize(path));
        } catch (IOException e) {
            throw new StorageException("Failed to read stored files", e);
        }

    }

    @Override
    public Path load(String filename) {
        return rootLocation.resolve(filename);
    }

    @Override
    public Resource loadAsResource(String filename) {
        try {
            Path file = load(filename);
            Resource resource = new UrlResource(file.toUri());
            if(resource.exists() || resource.isReadable()) {
                return resource;
            }
            else {
                throw new StorageFileNotFoundException("Could not read file: " + filename);

            }
        } catch (MalformedURLException e) {
            throw new StorageFileNotFoundException("Could not read file: " + filename, e);
        }
    }

    @Override
    public void deleteAll() {
        FileSystemUtils.deleteRecursively(rootLocation.toFile());
    }

    @Override
    public void init() {
        try {
            Files.createDirectory(rootLocation);
        } catch (IOException e) {
            throw new StorageException("Could not initialize storage", e);
        }
    }
    
    @Override
    public String getFileType(Path lngFile){
    	return "ISO8859";
    }
    
    @Override
    public Path getUniquePath(@NotNull String extension) {
    	Path fnamePath = Paths.get(java.util.UUID.randomUUID().toString());
    	Path dirPath = Paths.get(LocalDate.now().toString());
    	Path path = dirPath.resolve(fnamePath);
    	path = path.resolveSibling(path.getFileName() + extension);    	
    	return path;
    }
}
