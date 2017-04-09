package translateit2.lngfileservice;

import java.nio.file.Path;
import java.util.List;
import java.util.Locale;
import java.util.stream.Stream;

import org.springframework.web.multipart.MultipartFile;

public interface LngFileStorage {
	// testing
	String getType();
	String getGreetings();
	Path getPath(String filename);
	
	// true stuff
	boolean isFormatSupported(LngFileFormat format);
	void storeFile(MultipartFile file);
	void checkValidity(Path uploadedLngFile,long locoId);
	String createTargetLngFile(Path dstDir,String originalFilename,Locale locale);
	void uploadToDb(Path targetLngFile,long locoId);
	Stream<Path> downloadFromDb(long locoId);
}
