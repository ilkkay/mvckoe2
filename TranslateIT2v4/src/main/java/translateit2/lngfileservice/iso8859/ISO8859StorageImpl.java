package translateit2.lngfileservice.iso8859;

import java.nio.file.Path;
import java.util.List;
import java.util.Locale;
import java.util.stream.Stream;

import org.springframework.beans.factory.annotation.Autowired;

import translateit2.fileloader.storage.FileSystemStorageService;
import translateit2.fileloader.storage.StorageException;
import translateit2.lngfileservice.LngFileFormat;

import org.springframework.stereotype.Component;
import org.springframework.web.multipart.MultipartFile;

@Component
public class ISO8859StorageImpl implements  ISO8859Storage {
	@Autowired
	FileSystemStorageService fileStorage;
	
	public void setFileStorage(FileSystemStorageService fileStorage) {
		this.fileStorage = fileStorage;
	}

	// http://crunchify.com/why-and-for-what-should-i-use-enum-java-enum-examples/
	public enum Version {
		UTF_8(1), ISO8859_1(2);
		private int value;
 
		private Version(int value) {
			this.value = value;
		}
	}
	
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
	public boolean isFormatSupported(LngFileFormat format) {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public void storeFile(MultipartFile file) {
		// TODO Auto-generated method stub
		fileStorage.store(file);					
	}

	@SuppressWarnings("unused")
	@Override
	public void checkValidity(Path uploadedLngFile, long locoId) 
			throws StorageException {
		// check file name format i.e. appName_region_language*.properties 
		// or just appName_language*.properties => reject
		if (false)
			throw new StorageException("The language code is missing from the filename: "
				 + uploadedLngFile.getFileName());
		
		if (false)
			throw new StorageException("This is not a valid properties file: "
				 + uploadedLngFile.getFileName());
		
		// check encoding iso8859-1 or UTF-8 is same as in loco object => reject
		if (false)
		 throw new StorageException("The encoding is not same as in source language file. " 
				 + "It should be " + "UTF-8 or ISO8859");
		 
		// check if segments are empty => reject if updating transus
		
		// check if there are less untranslated segments in file
		// than there are in db => reject
		
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
