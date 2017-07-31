package translateit2.web;

import java.io.IOException;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import javax.servlet.http.HttpServletRequest;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.util.UriComponentsBuilder;

import translateit2.fileloader.storage.StorageService;
import translateit2.languagefileservice.factory.LanguageFileServiceFactory;
import translateit2.lngfileservice.LanguageFileStorage;
import translateit2.persistence.dto.ProjectDto;
import translateit2.persistence.dto.TranslatorGroupDto;
import translateit2.persistence.dto.WorkDto;
import translateit2.persistence.model.Priority;
import translateit2.persistence.model.Status;
import translateit2.restapi.AvailablePriority;
import translateit2.restapi.CustomErrorType;
import translateit2.restapi.Works;
import translateit2.service.ProjectService;

@RestController
@RequestMapping("/api")
public class RestWorkController {

	private ProjectService projectService;
	private LanguageFileServiceFactory languageFileServiceFactory;

	@Autowired
	public RestWorkController(StorageService storageService, 
			ProjectService projectService,
			LanguageFileServiceFactory languageFileServiceFactory) {
		this.projectService = projectService;
		this.languageFileServiceFactory = languageFileServiceFactory;
	}
	
	public static final Logger logger = LoggerFactory.getLogger(RestWorkController.class);
	
	// ------------------- Delete a Work-----------------------------------------
	@RequestMapping(value = "/work/{id}", method = RequestMethod.DELETE)
	public ResponseEntity<?> deleteWork(@PathVariable("id") long id) {
		logger.info("Fetching & Deleting Work with id {}", id);

		WorkDto wrk = projectService.getWorkDtoById(id);
		if (wrk == null) {
			logger.error("Unable to delete work with id {} not found.", id);
			return new ResponseEntity<>(new CustomErrorType("Unable to delete. Work with id " + id + " not found."),
					HttpStatus.NOT_FOUND);
		}
		
		projectService.removeWorkDto(id);

		return new ResponseEntity<>(HttpStatus.NO_CONTENT);
	}

	
	// ------------------- Update a Work ------------------------------------------------
	@RequestMapping(value = "/work/{id}", method = RequestMethod.PUT)
	public ResponseEntity<?> updateWork(@PathVariable("id") long id, @RequestBody WorkDto work) {
		logger.info("Updating Work with id {}", id);
	
		WorkDto wrk = projectService.getWorkDtoById(id);
		if (wrk == null) {
			logger.error("Unable to update. Work with id {} not found.", id);
			return new ResponseEntity<>(new CustomErrorType("Unable to update. Work with id " + id + " not found."),
					HttpStatus.NOT_FOUND);
		}

		logger.info("Updating Work with id {}", wrk.getId());

		wrk = projectService.updateWorkDto(work);
		
		return new ResponseEntity<>(wrk, HttpStatus.OK);
	}

	// -------------------Retrieve Single Work------------------------------------------
	@RequestMapping(value = "/work/{id}", method = RequestMethod.GET)
	public ResponseEntity<?> getWork(@PathVariable("id") long id) {
		logger.info("Fetching Work with id {}", id);

		WorkDto wrk = projectService.getWorkDtoById(id); 
						
		if (wrk == null) {
			logger.error("Work with id {} not found.", id);
			return new ResponseEntity<> (new CustomErrorType("Work with id " + id 
					+ " not found"), HttpStatus.NOT_FOUND);
		}
		return new ResponseEntity<>(wrk, HttpStatus.OK);
	}

	// -------------------Create a Work-------------------------------------------
	// 400 bad request
	@RequestMapping(value = "/work/", method = RequestMethod.POST)
	public ResponseEntity<?> createWork(@RequestBody WorkDto work,
	UriComponentsBuilder ucBuilder) {
		logger.info("Creating Project : {}", work);

		if (work.getId() != null) {
			logger.error("Unable to create. A Work with name {} already exist", work.getVersion());
			return new ResponseEntity<>(new CustomErrorType("Unable to create. A Project with version " + 
			work.getVersion() + " already exist."),HttpStatus.CONFLICT);
		}
		
		TranslatorGroupDto group = projectService.getGroupDtoByName("Group name");
		work.setGroupId(group.getId()); //TODO: mock this
		WorkDto wrk = projectService.createWorkDto(work);
		
		HttpHeaders headers = new HttpHeaders();
		headers.setLocation(ucBuilder.path("/api/work/{id}").buildAndExpand(wrk.getId()).toUri());
		return new ResponseEntity<Void>(headers, HttpStatus.CREATED);
	}

	// -------------------Retrieve All Works---------------------------------------------
	@RequestMapping(value = "/project/{projectId}/work/", method = RequestMethod.GET)
	public ResponseEntity<?> listAllWorks(@PathVariable("projectId") long projectId) {

		//TODO: where should I put these
		AvailablePriority pr = null;
		List <AvailablePriority> priorities = new ArrayList<AvailablePriority>();
		pr = new AvailablePriority();
		pr.setType(Priority.LOW);
		priorities.add(pr);
		pr = new AvailablePriority();
		pr.setType(Priority.MEDIUM);
		priorities.add(pr);
		pr = new AvailablePriority();
		pr.setType(Priority.HIGH);
		priorities.add(pr);
			
		List <WorkDto> projectWorks = projectService.listProjectWorkDtos(projectId);		

		if (projectWorks.isEmpty()) projectWorks = Collections.emptyList();
		
		Works wrks = new Works();
		wrks.setWorks(projectWorks);
		wrks.setAvailablePriorities(priorities);
		return new ResponseEntity<>(wrks, HttpStatus.OK);
	}

	// -------------------Upload source file---------------------------------------------
	// status code 404 (Not Found)
	// TODO: download /work/1/file?format=...
	@RequestMapping(value = "/work/{id}/sourceFile", method = RequestMethod.POST)
	public ResponseEntity<?> uploadSourceFile(@RequestParam(value = "workId") Long id,
			@RequestParam(value = "file") MultipartFile file,HttpServletRequest request) {
		
		WorkDto wrk = projectService.getWorkDtoById(id); 	
		if (wrk == null) {
			logger.error("Work with id {} not found.", id);
			return new ResponseEntity<> (new CustomErrorType("Work with id " + id 
					+ " not found"), HttpStatus.NOT_FOUND);
		}
		
		ProjectDto prj = projectService.getProjectDtoById(wrk.getProjectId());				
		LanguageFileStorage storageService = languageFileServiceFactory.getService(prj.getFormat()).get();
		Path uploadedLngFile = null;
		String appName = null;
		try {
			/*
			 * Upload language file
			 */
			uploadedLngFile = storageService.storeFile(file);
			/*
			 * check file format validity
			 */
			appName = storageService.checkValidity(uploadedLngFile, wrk.getId());
			/*
			 * upload
			 */
			storageService.uploadSourceToDb(uploadedLngFile, wrk.getId());
			
		} catch (IOException e) {
			logger.error("Could not upload source language file for workId {}: ", id);
			return new ResponseEntity<> (new CustomErrorType("Source language file for work with id " + id 
					+ " have not been uploaded"), HttpStatus.NOT_FOUND);
		}

		wrk = projectService.getWorkDtoById(wrk.getId());
		wrk.setStatus(Status.NEW);
		wrk.setOriginalFile(appName);
		wrk = projectService.updateWorkDto(wrk);
				
		return new ResponseEntity<>(wrk, HttpStatus.OK);
	}
}