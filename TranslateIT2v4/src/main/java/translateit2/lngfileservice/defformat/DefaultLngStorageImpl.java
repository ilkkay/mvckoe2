package translateit2.lngfileservice.defformat;

import java.io.IOException;
import java.nio.charset.Charset;
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
	public LngFileFormat getFileFormat() {
		return LngFileFormat.DEFAULT;
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
	public Path storeFile(MultipartFile file) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public String checkValidity(Path uploadedLngFile, long locoId) {
		// TODO Auto-generated method stub
		return null;		
	}


	@Override
	public Stream<Path> downloadFromDb(long locoId) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Path downloadTargetLngFile(Path dstDir, long workId) throws IOException {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public void uploadSourceToDb(Path uploadedLngFile, long workId) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void uploadTargetToDb(Path uploadedLngFile, long workId) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public Path createSkeletonLngFile(Path storedOriginalFile, long workId) throws IOException {
		// TODO Auto-generated method stub
		return null;
	}


}
