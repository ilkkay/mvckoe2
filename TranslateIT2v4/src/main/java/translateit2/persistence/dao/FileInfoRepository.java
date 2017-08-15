package translateit2.persistence.dao;

import org.springframework.data.repository.CrudRepository;

import translateit2.persistence.model.FileInfo;

public interface FileInfoRepository extends CrudRepository<FileInfo, Long> {
    FileInfo findById(final long fileinfoId);
}