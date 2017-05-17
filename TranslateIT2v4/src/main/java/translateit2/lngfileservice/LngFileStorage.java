package translateit2.lngfileservice;

import java.io.IOException;
import java.nio.file.Path;
import java.util.stream.Stream;

import org.springframework.web.multipart.MultipartFile;

// services needed for uploading and downloading a language file
// factory.listFormatsSupported() will list available services, (formats are now enums)
public interface LngFileStorage {
	// TODO: remove this, just for testing
	Path getPath(String filename);
	
	// true stuff
	LngFileFormat getFileFormat();
	
	boolean isFormatSupported(LngFileFormat format);
	
	Path storeFile(MultipartFile uploadedLngFile); 
	
	// if valid returns language filename
	String checkValidity(Path uploadedLngFile,long workId);
		
	void uploadSourceToDb(Path uploadedLngFile,long workId) throws IOException;
	
	void uploadTargetToDb(Path uploadedLngFile,long workId);
	
	// stream of all the files that can be loaded down
	Stream<Path> downloadFromDb(long locoId) throws IOException; 

	Path downloadTargetLngFile(Path dstDir, final long workId) throws IOException;

	// not in use at the moment
	Path createSkeletonLngFile(Path storedOriginalFile, final long workId) throws IOException;
}
