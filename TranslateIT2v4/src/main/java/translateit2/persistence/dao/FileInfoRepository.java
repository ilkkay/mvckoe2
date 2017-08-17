package translateit2.persistence.dao;

import java.util.Optional;

import org.springframework.data.repository.CrudRepository;

import translateit2.persistence.model.FileInfo;

public interface FileInfoRepository extends CrudRepository<FileInfo, Long> {
    FileInfo findById(final long fileinfoId);
    
    Optional <FileInfo> findByWorkId(final long workId);
}