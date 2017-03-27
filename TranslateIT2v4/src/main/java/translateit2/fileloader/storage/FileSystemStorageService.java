package translateit2.fileloader.storage;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.Resource;
import org.springframework.core.io.UrlResource;
import org.springframework.stereotype.Service;
import org.springframework.util.FileSystemUtils;
import org.springframework.web.multipart.MultipartFile;

import translateit2.util.OrderedProperties;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.MalformedURLException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Set;
import java.util.stream.Stream;

@Service
public class FileSystemStorageService implements StorageService {

    private final Path rootLocation;

    @Autowired
    public FileSystemStorageService(StorageProperties properties) {
        this.rootLocation = Paths.get(properties.getLocation());
    }

    @Override
    public void store(MultipartFile file) {
        try {
            if (file.isEmpty()) {
                throw new StorageException("Failed to store empty file " + file.getOriginalFilename());
            }
            InputStream in = file.getInputStream();
            Path target = this.rootLocation.resolve(file.getOriginalFilename());      
            /*
             */            
			if (in == null) {
				throw new FileNotFoundException("file not found");
			}	
			Path absTgtParent = target.toAbsolutePath().getParent();
            File f = new File(absTgtParent.toString());
            if (!f.isDirectory()) {
            	File dir = new File(absTgtParent.toString());
            	dir.mkdir();
            	System.out.println("Upload directory " + absTgtParent.toString() + " is missing.");
                //Paths.get(absTgtParent.toString());
            }
            if (!Files.exists(absTgtParent)) {
                throw new StorageException("Upload directory " + absTgtParent.toString() + 
                		" was missing and could not be recreated");
            }
            File t = new File(target.toString());
            boolean success = Files.deleteIfExists(t.toPath());
            if (!success) {
                throw new StorageException("File " + target.toString() + 
                		" existed already and could not be removed");
            }
            /*
             */
            Files.copy(file.getInputStream(), this.rootLocation.resolve(file.getOriginalFilename()));
        } catch (IOException e) {
            throw new StorageException("Failed to store file " + file.getOriginalFilename(), e);
        }
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
}
