package translateit2.lngfileservice.iso8859;

import java.io.IOException;
import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;
import java.nio.file.FileSystems;
import java.nio.file.Path;
import java.nio.file.PathMatcher;
import java.util.Collections;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.Locale;
import java.util.stream.Stream;

import org.springframework.beans.factory.annotation.Autowired;

import translateit2.fileloader.storage.FileSystemStorageService;
import translateit2.fileloader.storage.StorageException;
import translateit2.lngfileservice.LngFileFormat;
import translateit2.persistence.dto.TranveDto;
import translateit2.service.WorkService;
import translateit2.util.ISO8859Checker;
import translateit2.util.ISO8859Loader;
import translateit2.util.ISO8859util;
import translateit2.util.LngFileType;
import translateit2.util.Messages;

import org.springframework.stereotype.Component;
import org.springframework.web.multipart.MultipartFile;

@Component
public class ISO8859StorageImpl implements  ISO8859Storage {
	private FileSystemStorageService fileStorage;
	@Autowired
	public void setFileStorage(FileSystemStorageService fileStorage) {
		this.fileStorage = fileStorage;
	}

	private WorkService workService;	
	@Autowired
	public void setWorkService(WorkService workService) {
		this.workService = workService;
	}

	Messages messages;
	@Autowired
	public void setMessages(Messages messages) {
		this.messages = messages;
	}

	ISO8859util iso8859util;
	@Autowired
	public void setISO8859util(ISO8859util iso8859util) {
		this.iso8859util = iso8859util;
	}

	@Override
	public String getType() {
		return "ISO8859";
	}

	@Override
	public String getGreetings() {
		return "ISO8859ServiceImpl";
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
		fileStorage.store(file);					
	}

	@Override
	public void checkValidity(Path uploadedLngFile, long tranveId) 
			throws StorageException {

		// validity checks for tranve
		iso8859util.checkFileExtension(uploadedLngFile, tranveId); 
		iso8859util.checkFileNameFormat(uploadedLngFile, tranveId); 
		iso8859util.checkFileCharSet(uploadedLngFile, tranveId);
		iso8859util.checkEmptyFile(uploadedLngFile, tranveId);

		// validity checks for loco
		// check if there are less untranslated segments in file than there are in db => reject
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

	/*
	public void checkFileExtension(Path uploadedLngFile, long tranveId) 
			throws StorageException {
		// check extension
		PathMatcher matcher = FileSystems.getDefault().getPathMatcher("glob:*.properties");

		if (!(matcher.matches(uploadedLngFile.getFileName()))) 
			throw new StorageException((messages.get("FileStorageService.not_properties_file"))
					+ " " + uploadedLngFile.getFileName());
	}

	public void checkFileNameFormat(Path uploadedLngFile, long tranveId) 
			throws StorageException {
		// check file name format i.e. appName_region_language*.properties 
		// or just appName_language*.properties => reject 
		if (iso8859util.sanityCheck(uploadedLngFile.getFileName().toString())== null)
			throw new StorageException((messages.get("FileStorageService.code_missing"))
					+ " " + uploadedLngFile.getFileName());

		Locale locale = iso8859util.getLocaleFromString(
				uploadedLngFile.getFileName().toString(), ext -> ext.equals("properties"));
		if (locale == null)
			throw new StorageException((messages.get("FileStorageService.code_missing"))
					+ " " + uploadedLngFile.getFileName());
	}

	public void checkFileCharSet(Path uploadedLngFile, long tranveId) 
			throws StorageException {
		// check encoding iso8859-1 or UTF-8 is same as defined for the project version => reject
		TranveDto tranveDto = workService.getTranveDtoById(tranveId);
		LngFileType typeExpected = tranveDto.getType();
		boolean isUploadedUTF_8 = true;
		try {
			isUploadedUTF_8 = iso8859util.isCorrectCharset(uploadedLngFile,StandardCharsets.UTF_8 );
		} catch (StorageException e) {
			throw e; 
		}

		boolean isUploadedISO8859 = false;
		if (!isUploadedUTF_8)
			try {
				isUploadedISO8859 = iso8859util.isCorrectCharset(uploadedLngFile,StandardCharsets.ISO_8859_1 );
			} catch (StorageException e) {
				throw e; 
			}

		// if typeExpected == ISO8859 and uploaded is UTF-8 => reject
		if (typeExpected.equals(LngFileType.ISO8859_1) && isUploadedUTF_8)
			throw new StorageException(messages.get("FileStorageService.false_ISO8859_encoding"));
		//("The encoding is not same as defined for the version. It should be ISO8859.");

		// if typeExpected == UTF-8 and uploaded is ISO8859 => reject
		if (typeExpected.equals(LngFileType.UTF_8) && isUploadedISO8859)
			throw new StorageException(messages.get("FileStorageService.false_UTF8_encoding"));
		//("The encoding is not same as defined for the version. It should be UTF-8.");		
	}

	public void checkEmptyFile(Path uploadedLngFile, long tranveId) 
			throws StorageException {
		TranveDto tranveDto = workService.getTranveDtoById(tranveId);
		LngFileType typeExpected = tranveDto.getType();
		Charset charset = StandardCharsets.UTF_8;
		if (typeExpected.equals(LngFileType.ISO8859_1)) charset = StandardCharsets.ISO_8859_1;
		LinkedHashMap <String, String> segments  = null;
		try {
			segments = (LinkedHashMap<String, String>) iso8859util.getPropSegments(uploadedLngFile,charset);
		}catch (StorageException e) { // 
			throw e; 
		}catch (IOException e) { // 
			throw new StorageException((messages.get("FileStorageService.not_read_properties_file"))
					+ " " + uploadedLngFile.getFileName());
		}
		if (segments.isEmpty())
			throw new StorageException((messages.get("FileStorageService.empty_properties_file"))
					+ " " + uploadedLngFile.getFileName());

	}
*/
