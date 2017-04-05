package translateit2.lngfileservice.defformat;

import java.nio.file.Path;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.web.multipart.MultipartFile;

import translateit2.fileloader.storage.FileSystemStorageService;
import translateit2.lngfileservice.LngFileFormat;
import translateit2.lngfileservice.LngFileStorage;

@Component
public class DefaultLngStorageImpl implements DefaultLngStorage {
	@Autowired
	FileSystemStorageService fileStorage;
	
	public String getType() {
		return "Default";
	}
	
	public String getGreetings() {
		return "DefaultLngServiceImpl";
	}
	
	public Path getPath(String filename) {
		return fileStorage.load(filename);
	}

	@Override
	public void upLoadToDb(MultipartFile file) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public boolean isFileFormatSupported(LngFileFormat fileFormat) {
		// TODO Auto-generated method stub
		return false;
	}


}
