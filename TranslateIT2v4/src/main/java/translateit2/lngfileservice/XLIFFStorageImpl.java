package translateit2.lngfileservice;

import java.nio.file.Path;

import org.springframework.beans.factory.annotation.Autowired;

import org.springframework.stereotype.Component;

import translateit2.fileloader.storage.FileSystemStorageService;

@Component
public class XLIFFStorageImpl implements XLIFFStorage{
	@Autowired
	FileSystemStorageService fileStorage;
	
	@Override
	public String getType() {
		return "XLIFF";
	}
	
	
	@Override
	public String getGreetings() {
		return "XLIFFServiceImpl";
	}
	
	public Path getPath(String filename) {
		return fileStorage.load(filename);
	}
}
