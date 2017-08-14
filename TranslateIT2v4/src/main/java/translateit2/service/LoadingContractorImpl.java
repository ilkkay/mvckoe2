package translateit2.service;

import java.io.IOException;
import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.stream.Collectors;

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
import translateit2.languagebeancache.LanguageBeanCache;
import translateit2.languagebeancache.LanguageFileReader;
import translateit2.languagebeancache.LanguageFileValidator;
import translateit2.languagebeancache.LanguageFileWriter;
import translateit2.lngfileservice.LanguageFileFormat;
import translateit2.lngfileservice.LanguageFileType;
import translateit2.persistence.dao.FileInfoRepository;
import translateit2.persistence.dao.ProjectRepository;
import translateit2.persistence.dao.UnitRepository;
import translateit2.persistence.dao.WorkRepository;
import translateit2.persistence.model.FileInfo;
import translateit2.persistence.model.Project;
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
    private LanguageBeanCache<LanguageFileFormat, LanguageFileReader> fileReaderCache;

    @Autowired
    private LanguageBeanCache<LanguageFileFormat, LanguageFileWriter> fileWriterCache;

    @Autowired
    private LanguageBeanCache<LanguageFileFormat, LanguageFileValidator> fileValidatorCache;

    @Autowired
    private ProjectRepository projectRepo;

    @Autowired
    private WorkRepository workRepo;

    @Autowired
    private FileInfoRepository fileInfoRepo;

    @Autowired
    private UnitRepository unitRepo;

    @Override
    public Path downloadTarget(long workId) throws FileLoaderException {
        if (!(workRepo.exists(workId))) {
            logger.error("Work with id {} not found.", workId);
            throw new FileLoaderException(FileLoadError.CANNOT_UPLOAD_FILE); // or something
        }

        // get a map of translated units (i.e source segment and its translation)
        Map<String, String> map = getSegmentsMap(workId);
        
        // get original language file (i.e backup file) in
        List<String> originalFileAsList = getOriginalSegmentKeys(workId);
        
        // merge the map of translated units into the original language file
        List<String> downloadFileAsList = combine(map, originalFileAsList);
        
        // create the new translated language file
        Path downloadDirectory = null;;
        createDownloadFile(downloadDirectory, downloadFileAsList);
        
        // move file to download directory
        
        return null;
    }

    @Override
    public void uploadTarget(MultipartFile multipartFile, long workId) throws FileLoaderException {
        if (!(workRepo.exists(workId))) {
            logger.error("Work with id {} not found.", workId);
            throw new FileLoaderException(FileLoadError.CANNOT_UPLOAD_FILE); // or something
        }

        // move file from server to temporary location i.e. to upload directory
        Path uploadedFile = fileloader.storeToUploadDirectory(multipartFile);

        // get application name from filename
        String originalFileName = uploadedFile.getFileName().toString();
        String appName = fileNameResolver.getApplicationName(uploadedFile.getFileName().toString());

        // check extension and get locale from filename
        LanguageFileFormat format = getFormat(workId);        
        Locale appLocale = fileNameResolver.getLocaleFromString(uploadedFile.getFileName().toString(), 
                ext -> ext.equals(format.toString().toLowerCase()));

        // validate appName, locale and character set used in file 
        LanguageFileValidator validator = fileValidatorCache.getService(format).get();
        validator.validateCharacterSet(uploadedFile, getExpectedType(workId));
        validator.validateApplicationName(appName, getExpectedApplicationName(workId));
        validator.validateLocale(appLocale, getExpectedTargetLocale(workId));

        // read key/values pairs from the language file
        LanguageFileReader reader = fileReaderCache.getService(format).get();
        LinkedHashMap<String, String> segments = (LinkedHashMap<String, String>)reader.
                getSegments(uploadedFile, getCharSet(workId));

        // upload segments to data base
        loadTargetSegmentsToDatabase(segments, workId);

        // the uploaded (target) language file will be removed silently
        fileloader.deleteUploadedFile(uploadedFile);        
    }

    @Override
    public void uploadSource(MultipartFile multipartFile, long workId) throws FileLoaderException {        
        if (!(workRepo.exists(workId))) {
            logger.error("Work with id {} not found.", workId);
            throw new FileLoaderException(FileLoadError.CANNOT_UPLOAD_FILE); // or something
        }

        if (isSourceFileReload(workId)){
            logger.error("Trying to reload source file.", workId);
            throw new FileLoaderException(FileLoadError.CANNOT_UPLOAD_FILE); // or something
        }

        // check that availability of validator, reader and writer service for this format
        checkServiceAvailability(workId);

        // commit that file processing starts  

        // move file from server to temporary location i.e. to upload directory
        Path uploadedFile = fileloader.storeToUploadDirectory(multipartFile);

        // get application name from filename
        String originalFileName = uploadedFile.getFileName().toString();
        String appName = fileNameResolver.getApplicationName(uploadedFile.getFileName().toString());

        // check extension and get locale from filename
        LanguageFileFormat format = getFormat(workId);        
        Locale appLocale = fileNameResolver.getLocaleFromString(uploadedFile.getFileName().toString(), 
                ext -> ext.equals(format.toString().toLowerCase()));

        // validate character set used in file 
        LanguageFileValidator validator = fileValidatorCache.getService(format).get();
        validator.validateCharacterSet(uploadedFile, getExpectedType(workId));   
        validator.validateLocale(appLocale, getExpectedSourceLocale(workId));

        // move file to a permanent location       
        Path uploadedFilePath = filelocator.moveUploadedFileIntoFilesystem(uploadedFile, format);

        // read key/values pairs from the language file
        LanguageFileReader reader = fileReaderCache.getService(format).get();
        LinkedHashMap<String, String> segments = (LinkedHashMap<String, String>)reader.
                getSegments(uploadedFilePath, getCharSet(workId));

        // we move all the relevant into to the database, BUT
        // if there has been a rollback, we need to remove the uploaded file
        // and notify what has happened
        // test something somewhere (javax.persistence.RollbackException)
        loadSourceSegmentsToDatabase(originalFileName, appName, appLocale, uploadedFilePath, segments, workId);       

        // commit that file processing has finished  
    }

    @Transactional
    private void loadTargetSegmentsToDatabase(HashMap<String, String> segments, final long workId) {
        List<Unit> units = unitRepo.findAll().stream().filter(unit -> workId == unit.getWork().getId())
                .collect(Collectors.toList());
        for (Unit unit : units) {
            unit.getTarget().setText(segments.get(unit.getSegmentKey()));
            unit.getTarget().setState(State.TRANSLATED);
        }
        unitRepo.save(units);
    }


    private boolean isSourceFileReload(final long workId) {
        return (unitRepo.countByWorkId(workId) > 0);
    }

    // Only unchecked exceptions (that is, subclasses of java.lang.RuntimeException)
    // are rollbacked by default
    // we can safely assume you are doing your database operations through Spring, Hibernate, 
    // or other JDBC wrappers. These JDBC wrappers don't typically throw checked exceptions, 
    // they throw runtime exceptions that wrap the JDBC SQLException types.
    @Transactional //(rollbackOn = Exception.class)
    private boolean loadSourceSegmentsToDatabase(String originalFileName, String appName, Locale appLocale,
            Path uploadedFilePath, HashMap<String, String> segments, long workId) {


        // upload to database 
        loadSourceSegmentsToDatabase( segments, workId);

        // update file info
        long fileInfoId = updateFileInfo(uploadedFilePath,originalFileName, workId);

        // once you've loaded source file, the work status will be NEw
        updateWork(appName, fileInfoId, Status.NEW, workId);    

        return true;
    }

    @Transactional
    private void removeUnitDtos(final long workId) {        
        List<Unit> units = unitRepo.findAll().stream().filter(unit -> workId == unit.getWork().getId())
                .collect(Collectors.toList());
        unitRepo.delete(units);        
    }

    @Transactional
    private Locale getExpectedSourceLocale(long workId) {
        Project project = workRepo.findOne(workId).getProject();
        return workRepo.findOne(workId).getProject().getSourceLocale();
    }

    @Transactional
    private Locale getExpectedTargetLocale(long workId) {
        Work work = workRepo.findOne(workId);
        return workRepo.findOne(workId).getLocale();
    }

    @Transactional
    private String getExpectedApplicationName(long workId) {
        Work work = workRepo.findOne(workId);
        return  work.getOriginalFile();
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

    private List<String> combine(Map<String, String> map, List<String> inLines) throws FileLoaderException {
        List<String> outLines = new ArrayList<String>();
        boolean isFirstLine = true; // <= optional byte order mark (BOM)
        for (String line : inLines) {
            if ((isEmptyLine(line)) || (isCommentLine(line)) || (isFirstLine)) {
                outLines.add(line);
                isFirstLine = false;
            } else if (isKeyValuePair(line)) {
                String key = getKey(line);
                System.out.println(getKey(line) + "=" + map.get(key));
                outLines.add(getKey(line) + "=" + map.get(key));
            } else
                throw new FileLoaderException("Could not create file for download");
        }
        
        return outLines;
    }

    private String getKey(String line) {
        String parts[] = line.split("=");
        if (parts.length < 2)
            return null;
        else
            return parts[0].trim();
    }
    
    private boolean isKeyValuePair(String line) {
        if (getKey(line) != null)
            return true;
        else
            return false;
    }
    
    private boolean isCommentLine(String line) {
        return (line.trim().startsWith("#") || line.trim().startsWith("<"));
    }

    private boolean isEmptyLine(String line) {
        return line.isEmpty();
    }
    
    private void createDownloadFile(Path dstDir, List <String> outLines) {
        Work work = null;
        try {
            String tgtFilenameStr = work.getOriginalFile() + "_" + work.getLocale().toString() + ".properties";

            // will NOT fail even if path is null or empty. It returns path where
            // file resides.
            if ((dstDir != null) && (!(Files.isDirectory(dstDir)))) {
                Files.createDirectory(dstDir);
            }

            Path target = null; //(dstDir == null) ? fileStorage.getUploadPath(tgtFilenameStr)
                    //: dstDir.resolve(dstDir.getFileSystem().getPath(tgtFilenameStr));
            
            Files.write(target, outLines);
        } catch (IOException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        
    }
    
    private List<String> getOriginalSegmentKeys(long workId) {
        Work work = workRepo.findOne(workId);
        Path storedOriginalFile = Paths.get(work.getBackupFile());

        try {
            return Files.readAllLines(storedOriginalFile, getCharSet(workId));
        } catch (IOException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        return null;
    }


    @Transactional
    private Map<String, String> getSegmentsMap(long workId) {
        List<Unit> units = unitRepo.findAll().stream().filter(unit -> workId == unit.getWork().getId())
                .collect(Collectors.toList());
        Map<String, String> map = new HashMap<String, String>();
        units.stream().forEach(dto -> map.put(dto.getSegmentKey(), dto.getTarget().getText()));

        return map;
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
    private long updateFileInfo(Path uploadedFilePath, String OriginalFile, long workId) {
        Work work = workRepo.findOne(workId);
        FileInfo fileInfo = null;
        if (work.getFileinfo() == null)
            fileInfo = new FileInfo();
        else {
            long infoId = workRepo.findOne(workId).getFileinfo().getId();
            fileInfo = fileInfoRepo.findOne(workRepo.findOne(workId).getFileinfo().getId());
        }
        fileInfo.setBackup_file(uploadedFilePath.toString());
        fileInfo.setOriginal_file(OriginalFile);

        fileInfo = fileInfoRepo.save(fileInfo);

        return fileInfo.getId();
    }

    @Transactional
    private void updateWork(String appName, long fileInfoId, 
            Status status, long workId) {

        Work work = workRepo.findOne(workId);

        work.setOriginalFile(appName);
        work.setFileinfo(fileInfoRepo.findOne(fileInfoId));

        work.setStatus(status);

        workRepo.save(work);

        logger.debug( "Leaving updateWork with {}", work.toString());
    }

    @Transactional
    private int loadSourceSegmentsToDatabase( HashMap<String, String> segments, long workId){

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
        return savedUnits.size();
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
