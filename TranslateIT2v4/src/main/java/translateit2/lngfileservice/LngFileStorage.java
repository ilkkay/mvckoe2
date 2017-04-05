package translateit2.lngfileservice;

import java.nio.file.Path;

import org.springframework.web.multipart.MultipartFile;

public interface LngFileStorage {
	// testing
	String getType();
	String getGreetings();
	Path getPath(String filename);
	
	// true stuff
	boolean isFileFormatSupported(LngFileFormat fileFormat);
	public void upLoadToDb(MultipartFile file);
}
