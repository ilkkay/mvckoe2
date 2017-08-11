package translateit2.languagefileservice.xliff;

import java.io.IOException;
import java.nio.file.Path;
import java.util.stream.Stream;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.web.multipart.MultipartFile;

import translateit2.fileloader.FileLoaderImpl;
import translateit2.lngfileservice.LanguageFileFormat;

@Component
public class XLIFFStorageImpl implements XLIFFStorage {
    @Autowired
    FileLoaderImpl fileStorage;

    @Override
    public String checkValidity(Path uploadedLngFile, long locoId) {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    public Stream<Path> downloadFromDb(long locoId) {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    public Path downloadTargetLngFile(Path dstDir, long workId) throws IOException {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    public LanguageFileFormat getFileFormat() {
        return LanguageFileFormat.XLIFF;
    }

    public Path getPath(String filename) {
        return fileStorage.getUploadPath(filename);
    }

    @Override
    public Path storeFile(MultipartFile file) {
        // TODO Auto-generated method stub
        return null;

    }

    @Override
    public void uploadSourceToDb(Path uploadedLngFile, long workId) {
        // TODO Auto-generated method stub

    }

    @Override
    public void uploadTargetToDb(Path uploadedLngFile, long workId) {
        // TODO Auto-generated method stub

    }
}
