package translateit2.validator;

import java.nio.charset.Charset;
import java.nio.file.Path;

public interface LanguageFileValidator {
    void checkFileExtension(Path uploadedLngFile);

    String checkFileNameFormat(Path uploadedLngFile);

    void checkFileCharSet(Path uploadedLngFile, final long workId);

    void checkEmptyFile(Path uploadedLngFile, final long workId);

    // TODO: could this be elsewhere or is it OK here?
    Charset getCharSet(final long workId);
}
