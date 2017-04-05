package translateit2.lngfileservice.iso8859;

import java.nio.file.Path;

import org.springframework.beans.factory.annotation.Autowired;

import translateit2.fileloader.storage.FileSystemStorageService;
import translateit2.lngfileservice.LngFileFormat;

import org.springframework.stereotype.Component;
import org.springframework.web.multipart.MultipartFile;

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
