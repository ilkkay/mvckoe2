package translateit2.service;

import java.nio.file.Path;

import javax.validation.Valid;

import org.springframework.validation.annotation.Validated;
import org.springframework.web.multipart.MultipartFile;

@Validated
public interface LoadingContractor {

    Path downloadTarget(long workId);
    
    void uploadSource(@Valid MultipartFile file, long workId);
    
    void uploadTarget(@Valid MultipartFile file, long workId);
}
