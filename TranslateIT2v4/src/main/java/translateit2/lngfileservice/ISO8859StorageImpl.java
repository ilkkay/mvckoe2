package translateit2.lngfileservice;

import java.nio.file.Path;

import org.springframework.beans.factory.annotation.Autowired;

import translateit2.fileloader.storage.FileSystemStorageService;

import org.springframework.stereotype.Component;

@Component
public class ISO8859StorageImpl implements  ISO8859Storage {
	@Autowired
	FileSystemStorageService fileStorage;
	
	@Override
	public String getType() {
		return "ISO8859";
	}
	
	public String getGreetings() {
		return "ISO8859ServiceImpl";
	}
	
	public Path getPath(String filename) {
		return fileStorage.load(filename);
	}
}
