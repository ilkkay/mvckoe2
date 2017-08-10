package translateit2.service;

import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;

import javax.management.RuntimeErrorException;
import javax.transaction.Transactional;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.EnableTransactionManagement;
import org.springframework.transaction.interceptor.TransactionAspectSupport;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.multipart.MultipartFile;

import translateit2.fileloader.FileLoadError;
import translateit2.fileloader.FileLoader;
import translateit2.fileloader.FileLoaderException;
import translateit2.filelocator.FileLocator;
import translateit2.filenameresolver.FileNameResolver;
import translateit2.languagefactory.LanguageBeanCache;
import translateit2.languagefactory.LanguageFileReader;
import translateit2.languagefactory.LanguageFileValidator;
import translateit2.languagefactory.LanguageFileWriter;
import translateit2.lngfileservice.LanguageFileFormat;
import translateit2.lngfileservice.LanguageFileType;
import translateit2.persistence.dao.FileInfoRepository;
import translateit2.persistence.dao.ProjectRepository;
import translateit2.persistence.dao.UnitRepository;
import translateit2.persistence.dao.WorkRepository;
import translateit2.persistence.model.FileInfo;
import translateit2.persistence.model.Source;
import translateit2.persistence.model.State;
import translateit2.persistence.model.Status;
import translateit2.persistence.model.Target;
import translateit2.persistence.model.Unit;
import translateit2.persistence.model.Work;

@Validated
@EnableTransactionManagement
@Service
public class LoadingContractorImpl implements LoadingContractor {
    static final Logger logger = LogManager.getLogger(LoadingContractorImpl.class);

    @Autowired
    private FileLoader fileloader;

    @Autowired
    private FileLocator filelocator;

    @Autowired
    private FileNameResolver fileNameResolver;

    @Autowired
    private LanguageBeanCache<LanguageFileReader, LanguageFileFormat> fileReaderCache;

    @Autowired
    private LanguageBeanCache<LanguageFileValidator, LanguageFileFormat> fileValidatorCache;

    @Autowired
    private LanguageBeanCache<LanguageFileWriter, LanguageFileFormat> fileWriterCache;

    @Autowired
    private ProjectRepository projectRepo;

    @Autowired
    private WorkRepository workRepo;

    @Autowired
    private FileInfoRepository fileInfoRepo;

    @Autowired
    private UnitRepository unitRepo;

    @Override
    public Path downloadTarget(long workId) {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    public void uploadTarget(MultipartFile file, long workId) {
        // TODO Auto-generated method stub
        return;
    }

    @Override
    public void uploadSource(MultipartFile multipartFile, long workId) throws FileLoaderException {        
        if (!(workRepo.exists(workId))) {
            logger.error("Work with id {} not found.", workId);
            throw new FileLoaderException(FileLoadError.CANNOT_UPLOAD_FILE); // or something
        }

        // check that availability of validator, reader and writer service for this format
        checkServiceAvailability(workId);

        // move file from server to temporary location i.e. to upload directory
        Path uploadedFile = fileloader.storeToUploadDirectory(multipartFile);

        // get application name from filename
        String appName = fileNameResolver.getApplicationName(uploadedFile.getFileName().toString());

        // check extension and get locale from filename
        LanguageFileFormat format = getFormat(workId);        
        Locale appLocale = fileNameResolver.getLocaleFromString(uploadedFile.getFileName().toString(), 
                ext -> ext.equals(format.toString().toLowerCase()));

        // validate character set used in file 
        LanguageFileValidator validator = fileValidatorCache.getService(format).get();
        validator.validateCharacterSet(uploadedFile, getExpectedType(workId));        

        // move file to a permanent location       
        Path uploadedFilePath = filelocator.moveUploadedFileIntoFilesystem(uploadedFile, format);

        // read key/values pairs from the language file
        LanguageFileReader reader = fileReaderCache.getService(format).get();
        LinkedHashMap<String, String> segments = (LinkedHashMap<String, String>)reader.
                getSegments(uploadedFilePath, getCharSet(workId));

        // we move all the relevant into to the database, BUT
        // if there has been a rollback, we need to remove the uploaded file
        // and notify what has happened
        if (!(loadSourceSegmentsToDatabase(appName, appLocale, uploadedFilePath, segments, workId))) {
            logger.error("Source file {} could not be loaded to database", uploadedFilePath.toString());                  
            fileloader.deleteUploadedFile(uploadedFilePath.toString());
            throw new FileLoaderException(FileLoadError.CANNOT_UPLOAD_FILE); // or something
        }

    }

    // Only unchecked exceptions (that is, subclasses of java.lang.RuntimeException)
    // are rollbacked by default
    // we can safely assume you are doing your database operations through Spring, Hibernate, 
    // or other JDBC wrappers. These JDBC wrappers don't typically throw checked exceptions, 
    // they throw runtime exceptions that wrap the JDBC SQLException types.
    @Transactional //(rollbackOn = Exception.class)
    private boolean loadSourceSegmentsToDatabase(String appName, Locale appLocale,
            Path uploadedFilePath, HashMap<String, String> segments, long workId) {
        
        // upload to database. Return false if all the key/value pairs were not stored in db 
        if (!(loadSourceSegmentsToDatabase( segments, workId)))
            return false;

        // update file info
        long fileInfoId = updateFileInfo(uploadedFilePath);

        // once you've loaded source file, the work status will be NEw
        updateWork(appName, appLocale, fileInfoId, Status.NEW, workId);    

        return true;
    }

    public boolean loadSourceSegmentsToDatabase() {
        try {
          // same as above
        } catch (Exception ex) {
          // trigger rollback programmatically
          TransactionAspectSupport.currentTransactionStatus().setRollbackOnly();
          return false;
        }
        
        return true;
      }
    
    @Transactional
    private LanguageFileType getExpectedType(long workId) {
        Work work = workRepo.findOne(workId);
        return  projectRepo.findOne(work.getProject().getId()).getType();
    }

    @Transactional
    private Charset getCharSet(long workId) {
        Work work = workRepo.findOne(workId);
        LanguageFileType typeExpected =  projectRepo.findOne(work.getProject().getId()).getType();
        if (typeExpected.equals(LanguageFileType.ISO8859_1))
            return StandardCharsets.ISO_8859_1;
        else
            return StandardCharsets.UTF_8;
    }

    @Transactional
    private LanguageFileFormat getFormat(long workId) {
        Work work = workRepo.findOne(workId);
        return projectRepo.findOne(work.getProject().getId()).getFormat();        
    }

    @Transactional
    private void updateStatus(Status newStatus, long workId) {
        Work work = workRepo.findOne(workId);
        work.setStatus(newStatus);
        workRepo.save(work);

        logger.debug( "Leaving updateWork with {}", work.toString());
    }

    @Transactional
    private long updateFileInfo(Path uploadedFilePath) {
        FileInfo fileInfo = new FileInfo();
        fileInfo.setBackup_file(uploadedFilePath.toString());
        fileInfo.setOriginal_file(uploadedFilePath.getFileName().toString());

        fileInfo = fileInfoRepo.save(fileInfo);

        return fileInfo.getId();
    }

    @Transactional
    private void updateWork(String appName, Locale appLocale, long fileInfoId, 
            Status status, long workId) {

        Work work = workRepo.findOne(workId);
        work.setOriginalFile(appName);
        work.setLocale(appLocale);

        work.setStatus(status);

        workRepo.save(work);

        logger.debug( "Leaving updateWork with {}", work.toString());
    }

    @Transactional
    private boolean loadSourceSegmentsToDatabase( 
            HashMap<String, String> segments, long workId){
        List<Unit> units = new ArrayList<Unit>();
        int serialNum = 0;
        for (Map.Entry<String, String> entry : segments.entrySet()) {
            serialNum++;

            Source s = new Source();
            s.setText(entry.getValue());

            Target t = new Target();
            t.setText("");
            t.setSkeletonTag("TARGET_TAG_" + serialNum);
            t.setState(State.NEEDS_TRANSLATION);

            Unit unit = new Unit();
            unit.setSerialNumber(serialNum);
            unit.setSegmentKey(entry.getKey());
            unit.setSource(s);
            unit.setTarget(t);

            units.add(unit);            
        }

        Work work = workRepo.findOne(workId);
        units.stream().forEach(unit -> unit.setWork(work));

        List<Unit> savedUnits =  (List<Unit>) unitRepo.save(units);        
        if (savedUnits.size() != units.size())
            return false;
        else
            return true;
    }

    private void checkServiceAvailability(long workId) throws FileLoaderException {
        if (!(fileValidatorCache.getService(getFormat(workId)).isPresent())) {
            logger.error("Language file validator for format {} was missing", getFormat(workId));
            throw new FileLoaderException(FileLoadError.CANNOT_UPLOAD_FILE); // or something
        }      

        if (!(fileReaderCache.getService(getFormat(workId)).isPresent())) {
            logger.error("Language file reader for format {} was missing", getFormat(workId));
            throw new FileLoaderException(FileLoadError.CANNOT_UPLOAD_FILE); // or something
        }

        if (!(fileWriterCache.getService(getFormat(workId)).isPresent())) {
            logger.error("Language file writer for format {} was missing", getFormat(workId));
            throw new FileLoaderException(FileLoadError.CANNOT_UPLOAD_FILE); // or something
        }
    }
}
