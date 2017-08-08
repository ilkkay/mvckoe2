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
    public void uploadSource(MultipartFile uploadedFile, long workId) throws FileLoaderException {        
        if (!(workRepo.exists(workId))) {
            logger.error("Work with id {} not found.", workId);
            throw new RuntimeErrorException(null, ""); // or something
        }
        
        checkServiceAvailability(workId);
        
        // get application name from filename
        String appName = fileNameResolver.getApplicationName(uploadedFile.getOriginalFilename());

        // check extension and get locale
        LanguageFileFormat format = getFormat(workId);        
        Locale appLocale = fileNameResolver.getLocaleFromString(uploadedFile.getOriginalFilename(), 
                ext -> ext.equals(format));

        // validate character set in file 
        LanguageFileValidator validator = fileValidatorCache.getService(format).get();
        validator.validateCharacterSet(fileloader.getPath(uploadedFile.getOriginalFilename()), getExpectedType(workId));        

        // move file to a permanent location
        Path uploadedFilePath = fileloader.getPath(uploadedFile.getOriginalFilename());        
        filelocator.moveUploadedFileIntoFilesystem(uploadedFilePath, format);

        // read key/values pairs from the language file
        LanguageFileReader reader = fileReaderCache.getService(format).get();
        LinkedHashMap<String, String> segments = (LinkedHashMap<String, String>)reader.
                getSegments(uploadedFilePath, getCharSet(workId));

        // upload to database 
        loadSourceSegmentsToDatabase( segments, workId);

        // update file info
        long fileInfoId = updateFileInfo(uploadedFilePath);
        
        // once you've loaded source file, the work status will be NEw
        updateWork(appName, appLocale, fileInfoId, Status.NEW, workId);
            
    }

    @Override
    public void uploadTarget(MultipartFile file, long workId) {
        try {

        } catch (ArithmeticException a) {
            
        }
 catch (Exception e) {
            
        }
    }

    private void checkServiceAvailability(long workId) throws RuntimeErrorException {
        if (!(fileValidatorCache.getService(getFormat(workId)).isPresent())) {
            logger.error("Language file validator for format {} was missing", getFormat(workId));
            throw new RuntimeErrorException(null, ""); // or something
        }      
        
        if (!(fileReaderCache.getService(getFormat(workId)).isPresent())) {
            logger.error("Language file reader for format {} was missing", getFormat(workId));
            throw new RuntimeErrorException(null, ""); // or something
        }

        if (!(fileWriterCache.getService(getFormat(workId)).isPresent())) {
            logger.error("Language file writer for format {} was missing", getFormat(workId));
            throw new RuntimeErrorException(null, ""); // or something
        }
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
    private void loadSourceSegmentsToDatabase(HashMap<String, String> segments, long workId)
        throws FileLoaderException {
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
            throw new FileLoaderException(FileLoadError.CANNOT_UPLOAD_FILE);                 
    }

}
