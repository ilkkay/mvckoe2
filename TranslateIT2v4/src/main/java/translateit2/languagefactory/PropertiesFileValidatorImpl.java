package translateit2.languagefactory;

import java.io.IOException;
import java.nio.charset.Charset;
import java.nio.charset.MalformedInputException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;

import org.springframework.stereotype.Component;

import translateit2.fileloader.FileLoadError;
import translateit2.fileloader.FileLoaderException;
import translateit2.lngfileservice.LanguageFileFormat;
import translateit2.lngfileservice.LanguageFileType;

@Component
public class PropertiesFileValidatorImpl implements LanguageFileValidator {

    public PropertiesFileValidatorImpl() {
        // TODO Auto-generated constructor stub
    }

    @Override
    public LanguageFileFormat getFileFormat() {
        return LanguageFileFormat.PROPERTIES;
    }

    private boolean isCorrectCharset(Path uploadedLngFile, Charset charset) throws FileLoaderException {
        try {
            Files.readAllLines(uploadedLngFile, charset);
        } catch (MalformedInputException e) {
            return false; // do nothing is OK
        } catch (IOException e) {
            throw new FileLoaderException("Unexpected exception thrown while testing charset of a properties file");
        }
        return true; // if charset == UTF8 and no exceptions => file is UTF8
    }

    @Override
    public void validateCharacterSet(Path uploadedLngFile, LanguageFileType typeExpected) throws FileLoaderException {

        boolean isUploadedUTF_8 = true;
        try {
            isUploadedUTF_8 = isCorrectCharset(uploadedLngFile, StandardCharsets.UTF_8);
        } catch (FileLoaderException e) {
            throw e;
        }

        boolean isUploadedISO8859 = false;
        if (!isUploadedUTF_8)
            try {
                isUploadedISO8859 = isCorrectCharset(uploadedLngFile, StandardCharsets.ISO_8859_1);
            } catch (FileLoaderException e) {
                throw e;
            }

        // UTF-8 is identical to ISO8859 for the first 128 ASCII characters
        // which
        // include all the standard keyboard characters. After that, characters
        // are encoded as a multi-byte sequence.
        // if written in english it is both UTF-8 and ISO8859 encoded

        // if typeExpected == ISO8859 and uploaded is UTF-8 => reject
        if (typeExpected.equals(LanguageFileType.ISO8859_1) && isUploadedUTF_8)
            throw new FileLoaderException(FileLoadError.IMPROPER_CHARACTERSET_IN_FILE);
        // ("The encoding is not same as defined for the version. It should be
        // ISO8859.");

        // if typeExpected == UTF-8 and uploaded is ISO8859 => reject
        if (typeExpected.equals(LanguageFileType.UTF_8) && isUploadedISO8859)
            throw new FileLoaderException(FileLoadError.IMPROPER_CHARACTERSET_IN_FILE);
        // ("The encoding is not same as defined for the version. It should be
        // UTF-8.");
    }
}
