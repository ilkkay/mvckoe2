package translateit2.validator;

import java.nio.charset.Charset;
import java.nio.file.Path;

public interface LngFileValidator {
	public void checkFileExtension(Path uploadedLngFile); 
	
	public String checkFileNameFormat(Path uploadedLngFile); 
	
	public void checkFileCharSet(Path uploadedLngFile, final long workId);
	
	public void checkEmptyFile(Path uploadedLngFile, final long workId);
	
	//TODO: could this be elsewhere or is it OK here?
	public Charset getCharSet(final long workId);
}
