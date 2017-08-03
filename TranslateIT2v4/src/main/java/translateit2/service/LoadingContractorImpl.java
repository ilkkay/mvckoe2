package translateit2.service;

import java.nio.file.Path;

import javax.management.RuntimeErrorException;
import javax.transaction.Transactional;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.EnableTransactionManagement;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.multipart.MultipartFile;

import translateit2.fileloader.FileLoader;
import translateit2.filelocator.FileLocator;
import translateit2.languagefactory.LanguageFileFactory;
import translateit2.languagefactory.LanguageFileReader;
import translateit2.languagefactory.LanguageFileValidator;
import translateit2.languagefactory.LanguageFileWriter;
import translateit2.lngfileservice.LanguageFileFormat;
import translateit2.persistence.dao.ProjectRepository;
import translateit2.persistence.dao.WorkRepository;
import translateit2.persistence.model.Status;
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
    private LanguageFileFactory<LanguageFileReader, LanguageFileFormat> fileReaderFactory;

    @Autowired
    private LanguageFileFactory<LanguageFileWriter, LanguageFileFormat> fileWriterFactory;

    @Autowired
    private LanguageFileFactory<LanguageFileValidator, LanguageFileFormat> fileValidatorFactory;

    @Autowired
    private ProjectRepository projectRepo;

    @Autowired
    private WorkRepository workRepo;

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
    }

    @Override
    public void uploadSource(MultipartFile uploadedFile, long workId) {        
        if (!(workRepo.exists(workId))) {
            logger.error("Work with id {} not found.", workId);
            throw new RuntimeErrorException(null, ""); // or something
        }

        // check that we have respective service available
        LanguageFileFormat format = getFormat(workId);        
        if (!(fileValidatorFactory.getService(getFormat(workId)).isPresent())) {
            logger.error("Language file validator for format {} was missing", format);
            throw new RuntimeErrorException(null, ""); // or something
        }
        LanguageFileValidator validator = fileValidatorFactory.getService(format).get();
        
        
        // get application name and locale
        // appName = storageService.checkValidity(uploadedLngFile, workId);
        // locale = ...

        // validate contents
        validator.validate(uploadedFile.getOriginalFilename(),format);        

        // move to a permanent location
        Path uploadedFilePath = fileloader.getPath(uploadedFile.getOriginalFilename());        
        filelocator.moveUploadedFileIntoFilesystem(uploadedFilePath, format);

        // upload to database and do updates to Fileinfo and Work entities

        // update status
        updateStatus(Status.NEW, workId);
        /*    
        Project prj = projectRepo.get Service.getProjectDtoById(wrk.getProjectId());
        LanguageFileStorage storageService = languageFileServiceFactory.getService(prj.getFormat()).get();
        Path uploadedLngFile = null;
        String appName = null;
        try {
            uploadedLngFile = storageService.storeFile(file);
            appName = storageService.checkValidity(uploadedLngFile, workId);
            storageService.uploadSourceToDb(uploadedLngFile, wrk.getId());

        } catch (IOException e) {
            logger.error("Could not upload source language file for workId {}: ", id);
            return new ResponseEntity<>(
                    new CustomErrorType("Source language file for work with id " + id + " have not been uploaded"),
                    HttpStatus.NOT_FOUND);
        }
         */
    }

    @Override
    public void uploadTarget(MultipartFile file, long workId) {
    }

    @Override
    public Path downloadTarget(long workId) {
        // TODO Auto-generated method stub
        return null;
    }

}
