package translateit2.lngfileservice;

import java.io.IOException;
import java.nio.file.Path;
import java.util.stream.Stream;

import org.springframework.web.multipart.MultipartFile;

import translateit2.fileloader.FileLoaderException;

// services needed for uploading and downloading a language file
// factory.listFormatsSupported() will list available services, (formats are now enums)
public interface LanguageFileStorage {

    // if valid returns language filename
    String checkValidity(Path uploadedLngFile, long workId) throws FileLoaderException;

    // stream of all the files that can be loaded down
    Stream<Path> downloadFromDb(long locoId) throws IOException;

    Path downloadTargetLngFile(Path dstDir, final long workId) throws IOException;

    LanguageFileFormat getFileFormat();

    Path storeFile(MultipartFile uploadedLngFile) throws FileLoaderException;

    void uploadSourceToDb(Path uploadedLngFile, long workId) throws IOException;

    void uploadTargetToDb(Path uploadedLngFile, long workId) throws FileLoaderException;

}