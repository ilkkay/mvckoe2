package translateit2.filelocator;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.time.LocalDate;

import javax.transaction.Transactional;
import javax.validation.constraints.NotNull;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import translateit2.fileloader.FileLoader;
import translateit2.lngfileservice.LanguageFileFormat;
import translateit2.persistence.dao.FileInfoRepository;

@Component
public class FileLocatorImpl implements FileLocator {    
    @Autowired
    FileLoader fileLoaderService;
    
    public FileLocatorImpl() {
        // TODO Auto-generated constructor stub
    }

    @Override
    public Path moveUploadedFileIntoFilesystem(Path uploadedFile, 
            LanguageFileFormat format) {
        // create new path
        Path outFilePath = getUniquePath(format);
        
        // and move to permanent location   
        try {
            Files.copy(uploadedFile, outFilePath, StandardCopyOption.REPLACE_EXISTING);
        } catch (IOException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        
        // notice that in case of properties file these two are the same
        // but in case xliff and po files back file is the file which was loaded the first time
        // since xliff and po files dont have source and target files

        return null;
    }

    private Path getUniquePath(LanguageFileFormat format) {
        Path fnamePath = Paths.get(java.util.UUID.randomUUID().toString());
        Path dirPath = Paths.get(LocalDate.now().toString());
        Path path = dirPath.resolve(fnamePath);
        path = path.resolveSibling(path.getFileName() + "." + format.toString());
        return path;
    }
}
