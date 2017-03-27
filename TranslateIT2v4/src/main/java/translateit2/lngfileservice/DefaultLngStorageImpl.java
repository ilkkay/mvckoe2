package translateit2.lngfileservice;

import java.nio.file.Path;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import translateit2.fileloader.storage.FileSystemStorageService;

@Component
public class DefaultLngStorageImpl implements LngFileStorage {
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

}
