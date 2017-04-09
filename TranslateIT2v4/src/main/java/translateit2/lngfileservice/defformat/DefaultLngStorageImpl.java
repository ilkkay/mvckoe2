package translateit2.lngfileservice.defformat;

import java.nio.file.Path;
import java.util.List;
import java.util.Locale;
import java.util.stream.Stream;

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
	
	@Override
	public String getType() {
		return "Default";
	}
	
	@Override
	public String getGreetings() {
		return "DefaultLngServiceImpl";
	}
	
	@Override
	public Path getPath(String filename) {
		return fileStorage.load(filename);
	}

	@Override
	public boolean isFormatSupported(LngFileFormat format) {
		// TODO Auto-generated method stub
		return false;
	}


	@Override
	public void storeFile(MultipartFile file) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void checkValidity(Path uploadedLngFile, long locoId) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public String createTargetLngFile(Path dstDir, String originalFilename, Locale locale) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public void uploadToDb(Path targetLngFile, long locoId) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public Stream<Path> downloadFromDb(long locoId) {
		// TODO Auto-generated method stub
		return null;
	}


}
