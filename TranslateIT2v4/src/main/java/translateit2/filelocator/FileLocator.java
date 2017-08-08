package translateit2.filelocator;

import java.nio.file.Path;

import translateit2.fileloader.FileLoaderException;
import translateit2.lngfileservice.LanguageFileFormat;

public interface FileLocator {
    Path moveUploadedFileIntoFilesystem(Path uploadedFile,
            LanguageFileFormat format) throws FileLoaderException;
}
