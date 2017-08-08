package translateit2.validator;

import java.nio.charset.Charset;
import java.nio.file.Path;

import translateit2.fileloader.FileLoaderException;

public interface LanguageFileValidator {
    void checkEmptyFile(Path uploadedLngFile, final long workId) throws FileLoaderException;

    void checkFileCharSet(Path uploadedLngFile, final long workId) throws FileLoaderException;

    void checkFileExtension(Path uploadedLngFile) throws FileLoaderException;

    String checkFileNameFormat(Path uploadedLngFile) throws FileLoaderException;

    // TODO: could this be elsewhere or is it OK here?
    Charset getCharSet(final long workId);
}
