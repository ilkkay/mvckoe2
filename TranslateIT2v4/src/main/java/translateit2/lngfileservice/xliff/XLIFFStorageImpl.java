package translateit2.lngfileservice.xliff;

import java.nio.file.Path;

import org.springframework.beans.factory.annotation.Autowired;

import org.springframework.stereotype.Component;
import org.springframework.web.multipart.MultipartFile;

import translateit2.fileloader.storage.FileSystemStorageService;
import translateit2.lngfileservice.LngFileFormat;

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
