package translateit2.validator;

import java.nio.charset.Charset;
import java.nio.file.Path;

import translateit2.exception.TranslateIt2Exception;

public interface LanguageFileValidator {
    void checkEmptyFile(Path uploadedLngFile, final long workId) throws TranslateIt2Exception;

    void checkFileCharSet(Path uploadedLngFile, final long workId) throws TranslateIt2Exception;

    void checkFileExtension(Path uploadedLngFile) throws TranslateIt2Exception;

    String checkFileNameFormat(Path uploadedLngFile) throws TranslateIt2Exception;

    // TODO: could this be elsewhere or is it OK here?
    Charset getCharSet(final long workId);
}
