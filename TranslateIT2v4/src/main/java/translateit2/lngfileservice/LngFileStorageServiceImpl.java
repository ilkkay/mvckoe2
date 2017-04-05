package translateit2.lngfileservice;
import java.nio.file.Path;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.stereotype.Service;

import translateit2.fileloader.storage.FileSystemStorageService;
import translateit2.lngfileservice.defformat.DefaultLngStorageImpl;
import translateit2.lngfileservice.iso8859.ISO8859StorageImpl;
import translateit2.lngfileservice.xliff.XLIFFStorageImpl;

// https://stormpath.com/blog/spring-boot-dependency-injection
// https://stormpath.com/blog/spring-boot-default-starters
// http://stackoverflow.com/questions/6390810/implement-a-simple-factory-pattern-with-spring-3-annotations
@Service
class LngFileStorageServiceImpl implements LngFileStorageService {
	@Autowired
	FileSystemStorageService fileStorage;
	
	@Autowired
	ISO8859StorageImpl iso8859Service;
    
	@Autowired
	XLIFFStorageImpl xliffService;
	
	@Autowired
	DefaultLngStorageImpl defaultLngService;

    public LngFileStorage getService (Path lngFile){
    	String type = fileStorage.getFileType(lngFile.getFileName());
    	switch (type){
			case "ISO8859":
				return iso8859Service;

			case "XLIFF":
    			return xliffService;
    			    		
            default: 
            	return defaultLngService; // or throw something etc.            	
    	}    		

    }
       		
}
