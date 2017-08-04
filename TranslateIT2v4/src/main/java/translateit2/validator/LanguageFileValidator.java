package translateit2.validator;

import java.nio.charset.Charset;
import java.nio.file.Path;

public interface LanguageFileValidator {
    void checkEmptyFile(Path uploadedLngFile, final long workId);

    void checkFileCharSet(Path uploadedLngFile, final long workId);

    void checkFileExtension(Path uploadedLngFile);

    String checkFileNameFormat(Path uploadedLngFile);

    // TODO: could this be elsewhere or is it OK here?
    Charset getCharSet(final long workId);
}
