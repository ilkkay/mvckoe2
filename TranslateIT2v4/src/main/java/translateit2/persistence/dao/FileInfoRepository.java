package translateit2.persistence.dao;

import java.util.List;

import org.springframework.data.repository.CrudRepository;

import translateit2.persistence.model.FileInfo;
import translateit2.persistence.model.Work;

public interface FileInfoRepository extends CrudRepository<FileInfo, Long> {
    FileInfo findById(final long fileinfoId);
}