package translateit2.service;

import java.nio.file.Path;

import javax.validation.Valid;

import org.springframework.validation.annotation.Validated;
import org.springframework.web.multipart.MultipartFile;

import translateit2.fileloader.FileLoaderException;

@Validated
public interface LoadingContractor {

    Path downloadTarget(long workId) throws FileLoaderException;
    
    void uploadSource(@Valid MultipartFile file, long workId) throws FileLoaderException;
    
    void uploadTarget(@Valid MultipartFile file, long workId) throws FileLoaderException;
}
