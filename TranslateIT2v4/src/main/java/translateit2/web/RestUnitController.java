package translateit2.web;

import java.io.IOException;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.stream.Stream;

import javax.servlet.http.HttpServletRequest;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.Resource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.util.UriComponents;
import org.springframework.web.util.UriComponentsBuilder;

import translateit2.fileloader.FileLoader;
import translateit2.fileloader.FileLoaderException;
import translateit2.languagefileservice.factory.LanguageFileServiceFactory;
import translateit2.lngfileservice.LanguageFileStorage;
import translateit2.persistence.dto.ProjectDto;
import translateit2.persistence.dto.UnitDto;
import translateit2.persistence.dto.WorkDto;
import translateit2.persistence.model.State;
import translateit2.persistence.model.Status;
import translateit2.persistence.model.Target;
import translateit2.restapi.CustomErrorType;
import translateit2.restapi.Statistics;
import translateit2.restapi.Units;
import translateit2.service.ProjectService;
import translateit2.service.WorkService;

@RestController
@RequestMapping("/api")
public class RestUnitController {
    public static final Logger logger = LoggerFactory.getLogger(RestUnitController.class);

    private LanguageFileServiceFactory languageFileServiceFactory;
    private ProjectService projectService;
    private final FileLoader storageService;
    private WorkService workService;

    boolean isMockStatInitialized = false;
    Statistics mockStat = new Statistics(); // TODO: this is a mock solution

    @Autowired
    public RestUnitController(FileLoader storageService, 
            ProjectService projectService,
            WorkService workService,
            LanguageFileServiceFactory languageFileServiceFactory) {
        this.projectService = projectService;
        this.workService = workService;
        this.languageFileServiceFactory = languageFileServiceFactory;
        this.storageService = storageService;
    }

    // -------------------Get path to download
    // file------------------------------------------
    @RequestMapping(value = "/work/{id}/downloadUrl", method = RequestMethod.GET)
    public ResponseEntity<?> getDownloadPath(@PathVariable("id") long id, UriComponentsBuilder ucBuilder) {
        // http://www.baeldung.com/spring-uricomponentsbuilder
        logger.info("Creating url path for workId {}", id);

        WorkDto work = workService.getWorkDtoById(id);
        ProjectDto prj = projectService.getProjectDtoById(work.getProjectId());
        LanguageFileStorage storageService = languageFileServiceFactory.getService(prj.getFormat()).get();

        Stream<Path> paths = null;
        try {
            paths = storageService.downloadFromDb(id);
        } catch (IOException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }

        // http://localhost:8080/files/dotcms_fi_FI.properties
        String filename = "/api/files/" + paths.findFirst().get().getFileName().toString();
        UriComponents uriComponents = ucBuilder.scheme("http").host("localhost").path(filename).build();

        return new ResponseEntity<>(uriComponents, HttpStatus.OK);
    }


    // -------------------Retrieve Single
    // Unit------------------------------------------
    @RequestMapping(value = "/work/unit/{id}", method = RequestMethod.GET)
    public ResponseEntity<?> getUnit(@PathVariable("id") long id) {
        logger.info("Fetching Unit with id {}", id);

        UnitDto unt = workService.getUnitDtoById(id);

        if (unt == null) {
            logger.error("Unit with id {} not found.", id);
            return new ResponseEntity<>(new CustomErrorType("Unit with id " + id + " not found"), HttpStatus.NOT_FOUND);
        }

        return new ResponseEntity<>(unt, HttpStatus.OK);
    }

    // -------------------Retrieve the Units by
    // pages---------------------------------------------
    @RequestMapping(value = "/work/{workId}/unit/{pageNum}", method = RequestMethod.GET)
    public ResponseEntity<?> listAllUnits(@PathVariable("workId") long workId, @PathVariable("pageNum") int pageNum) {

        int pageSize = 4;
        int pageIndex = pageNum - 1;
        long pageCount = workService.getUnitDtoCount(workId);
        List<UnitDto> workUnits = workService.getPage(workId, pageIndex, pageSize);

        if (workUnits.isEmpty()) {
            return new ResponseEntity<>(Collections.emptyList(), HttpStatus.OK);
        }

        Units unts = new Units();
        unts.setUnits(workUnits);
        unts.setPageCount(pageCount / 4 + 1);

        if (!(isMockStatInitialized))
            InitMockStat(workId);
        unts.setStatistics(mockStat);

        return new ResponseEntity<>(unts, HttpStatus.OK);
    }

    @GetMapping("/files/{filename:.+}")
    public ResponseEntity<Resource> serveFile(@PathVariable String filename) {

        Resource file = null;
        try {
            file = storageService.loadAsResource(filename);
        } catch (FileLoaderException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        ResponseEntity<Resource> response = null;
        response = ResponseEntity.ok()
                .header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=\"" + file.getFilename() + "\"")
                .body(file);
        return response;
    }

    // ------------------- Update a Unit
    // ------------------------------------------------
    @RequestMapping(value = "/work/unit/{id}", method = RequestMethod.PUT)
    public ResponseEntity<?> updateUnit(@PathVariable("id") long id, @RequestBody UnitDto unit) {
        logger.info("Updating Work with id {}", id);

        if (unit.getId() == null) {
            logger.error("Unable to update unit. Unit with id {} not found.", unit.getId());
            return new ResponseEntity<>(
                    new CustomErrorType("Unable to update. Unit with id " + unit.getId() + " not found."),
                    HttpStatus.NOT_FOUND);
        }

        // TODO: update mock statistics
        // IF the target.Text is != empty AND current state is untranslated,
        // THEN increment translated value and set state translated
        if ((unit.getTarget().getText().trim().length() > 0)
                && ((unit.getTarget().getState() == State.NEEDS_TRANSLATION))
                || (unit.getTarget().getState() == State.NEW)) {
            Target t = unit.getTarget();
            t.setState(State.TRANSLATED);
            mockStat.setTranslated(mockStat.getTranslated() + 1);
        } else { // ELSE do nothing (state == translated)
            int j = 1;
        }

        // IF target.Text.Trim() == empty AND current state is translated,
        // THEN decrement translated value and set state untranslated
        if ((unit.getTarget().getText().trim().length() == 0) && (unit.getTarget().getState() == State.TRANSLATED)) {
            Target t = unit.getTarget();
            t.setState(State.NEEDS_TRANSLATION);
            mockStat.setTranslated(mockStat.getTranslated() - 1);
        } else { // ELSE do nothing (state == untranslated)
            int j = 1;
        }

        List<UnitDto> newUnitDtos = new ArrayList<UnitDto>();
        newUnitDtos.add(unit);
        workService.updateUnitDtos(newUnitDtos, id);

        unit = workService.getUnitDtoById(unit.getId());
        return new ResponseEntity<>(unit, HttpStatus.OK);
    }

    // -------------------Upload target
    // file---------------------------------------------
    // status code 404 (Not Found)
    @RequestMapping(value = "/work/{id}/targetFile", method = RequestMethod.POST)
    public ResponseEntity<?> uploadTargetFile(@RequestParam(value = "workId") Long id,
            @RequestParam(value = "file") MultipartFile file, HttpServletRequest request // ({
            , UriComponentsBuilder ucBuilder) {

        WorkDto work = workService.getWorkDtoById(id);
        if (work == null) {
            logger.error("Unable to upload target file. Work with id {} not found.", id);
            return new ResponseEntity<>(
                    new CustomErrorType("Unable to upload target. Work with id " + id + " not found."),
                    HttpStatus.NOT_FOUND);
        }

        ProjectDto prj = projectService.getProjectDtoById(work.getProjectId());
        LanguageFileStorage storageService = languageFileServiceFactory.getService(prj.getFormat()).get();
        Path uploadedLngFile = null;
        /*
         * Upload language file
         */
        try {
            uploadedLngFile = storageService.storeFile(file);
            /*
             * check file format validity
             */
            storageService.checkValidity(uploadedLngFile, work.getId());
            /*
             * upload
             */
            storageService.uploadTargetToDb(uploadedLngFile, work.getId());

            work = workService.getWorkDtoById(work.getId());
            work.setStatus(Status.OPEN);
            work = workService.updateWorkDto(work);

            // TODO: mock statistics
            InitMockStat(work.getId());
            // return new ResponseEntity<>(mockStat, HttpStatus.OK);

            HttpHeaders headers = new HttpHeaders();
            headers.setLocation(ucBuilder.path("/api/work/{id}/targetFile").buildAndExpand(work.getId()).toUri());
            return new ResponseEntity<Void>(headers, HttpStatus.CREATED);
        }
        catch (FileLoaderException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        return null;
    }

    private void InitMockStat(final long workId) {
        isMockStatInitialized = true;

        List<UnitDto> units = workService.listUnitDtos(workId);
        long total = units.size();
        long translated = 0;
        for (UnitDto unit : units) {
            if ((unit.getTarget().getState() == State.TRANSLATED)
                    || ((unit.getTarget().getState() == State.NEEDS_REVIEW)))
                translated++;
        }

        mockStat.setReviewed(0L);
        mockStat.setTotal(total);
        mockStat.setTranslated(translated);
        mockStat.setWorkId(workId);
    }

}