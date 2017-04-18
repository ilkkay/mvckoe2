package translateit2.util;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.nio.charset.Charset;
import java.nio.charset.MalformedInputException;
import java.nio.charset.StandardCharsets;
import java.nio.file.FileSystems;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.PathMatcher;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.Locale;
import java.util.Set;
import java.util.function.Predicate;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import translateit2.fileloader.storage.StorageException;
import translateit2.persistence.dto.TranveDto;
import translateit2.service.WorkService;

@Component
public class ISO8859util {
	// TODO: public <= unit testing
	private WorkService workService;
	@Autowired
	public void setWorkService(WorkService workService) {
		this.workService = workService;
	}

	private Messages messages;
	@Autowired
	public void setMessages(Messages messages) {
		this.messages = messages;
	}

	// return application name other wise null
	public String sanityCheck(String localeString)
	{
		if (localeString == null) return null;
		localeString = localeString.trim();

		// Extract application name
		int appIndex = localeString.indexOf('_');
		if (appIndex == -1)
		{
			// No further "_" so this is "{application}" only file
			return null;
		}
		else
			return localeString.substring(0, appIndex);        
	}

	/**
	 * Convert a string based locale into a Locale Object.
	 * Accepts following forms
	 *  "_{language}".
	 *  "_{language}_{country}".
	 *  "_{language}_{country}_{variant}"
	 * "_{language}_{country}.properties".
	 *  
	 * @param localeString The String
	 * @return the Locale
	 */
	public Locale getLocaleFromString(String localeString,Predicate<String> p)
	{
		if (localeString == null) return null;

		localeString = localeString.trim();

		String extension = "";
		int i = localeString.lastIndexOf('.');
		if (i > 0) extension = localeString.substring(i+1);

		if (!p.test(extension)) return null;

		// get application name end position
		int appIndex = localeString.indexOf('_');

		int languageIndex = localeString.indexOf('_',appIndex + 1);

		String language = null;
		if (languageIndex == -1)
		{
			// No further "_" so is "{language}" only
			language = localeString.substring(appIndex + 1, appIndex + 3);
			return new Locale(language, language.toUpperCase());
		}
		else
		{
			language = localeString.substring(appIndex + 1, languageIndex);
		}        

		// Extract language which is exactly two characters long
		if (languageIndex - appIndex != 3) return null;

		// Extract country
		int countryIndex = localeString.indexOf('_', languageIndex + 1);
		if (countryIndex == -1) countryIndex = localeString.indexOf('.', languageIndex + 1);

		// Extract country which is exactly two characters long
		if (countryIndex - languageIndex != 3) return null;

		String country = null;
		if (countryIndex == -1)
		{
			// No further "_" so is "{language}_{country}"
			country = localeString.substring(languageIndex+1);
			country = country.substring(0, 2);
			return new Locale(language, country.toUpperCase());
		}
		else
		{
			// Assume all remaining is the variant so is "{language}_{country}_{variant}"
			country = localeString.substring(languageIndex+1, countryIndex);
			String variant = localeString.substring(countryIndex+1);
			return new Locale(language, country.toUpperCase());
		}
	}

	public boolean isCorrectCharset(Path uploadedLngFile, Charset charset) throws StorageException {
		try {
			Files.readAllLines(uploadedLngFile, charset);
			// isCorrectCharset(uploadedLngFile,StandardCharsets.UTF_8 ); 
			// isCorrectCharset(uploadedLngFile,StandardCharsets.ISO_8859_1 );
		} catch (MalformedInputException e) {
			// TODO Auto-generated catch block
			return false;
		}
		catch (IOException e) {
			// TODO Auto-generated catch block
			throw new StorageException("Unexpected exception thrown while testing charset ofa properties file");
		}
		return true; // if charset == UTF8 and no exceptions => file is UTF8 encoded 

	}


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
		if (ISO8859Checker.sanityCheck(uploadedLngFile.getFileName().toString())== null)
			throw new StorageException((messages.get("FileStorageService.code_missing"))
					+ " " + uploadedLngFile.getFileName());

		Locale locale = ISO8859Checker.getLocaleFromString(
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
			isUploadedUTF_8 = ISO8859Checker.isCorrectCharset(uploadedLngFile,StandardCharsets.UTF_8 );
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		boolean isUploadedISO8859 = false;
		if (!isUploadedUTF_8)
			try {
				isUploadedISO8859 = ISO8859Checker.isCorrectCharset(uploadedLngFile,StandardCharsets.ISO_8859_1 );
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
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
			segments = (LinkedHashMap<String, String>) ISO8859Loader.getPropSegments(uploadedLngFile,charset);
		} catch (IOException e) { // 
			throw new StorageException((messages.get("FileStorageService.not_read_properties_file"))
					+ " " + uploadedLngFile.getFileName());
		}
		if (segments.isEmpty())
			throw new StorageException((messages.get("FileStorageService.empty_properties_file"))
					+ " " + uploadedLngFile.getFileName());

	}

	public static HashMap<String, String> getPropSegments(Path inputPath, Charset charset)
			throws StorageException, IOException{

		InputStreamReader isr = null;    	
		InputStream stream = null;
		HashMap<String, String> map = new LinkedHashMap<String, String>();
		OrderedProperties srcProp = new OrderedProperties();

		try {
			stream = new FileInputStream(inputPath.toString());
			isr = new InputStreamReader(stream,charset);
			srcProp.load(isr); 
			Set<String> keys = srcProp.stringPropertyNames();
			// checks for at least one (ASCII) alphanumeric character.
			map = keys.stream().filter(k -> k.toString().matches(".*\\w.*"))  
					.collect(Collectors.toMap (
							k -> k.toString(),k -> srcProp.getProperty(k),
							(v1,v2)->v1,LinkedHashMap::new));

			map.forEach((k,v)->System.out.println(k + "\n" + v));

		} catch (FileNotFoundException e) {
			throw new StorageException("Did not find the property file:" + " " + inputPath.toString(), e);
		} 
		catch (IOException e) {
			throw new StorageException("Error while reading property file:" + " " + inputPath.toString(), e);
		} 
		finally {
			stream.close();
			isr.close();
		}

		return map;
	}

}
