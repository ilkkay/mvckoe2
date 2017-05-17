package translateit2.web;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;

import javax.validation.Valid;

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
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.util.UriComponentsBuilder;

import translateit2.restapi.AvailableCharacterSet;
import translateit2.restapi.AvailableFormat;
import translateit2.restapi.CustomErrorType;
import translateit2.lngfileservice.LngFileFormat;
import translateit2.lngfileservice.LngFileType;
import translateit2.lngfileservice.factory.LngFileServiceFactory;
import translateit2.persistence.dto.InfoDto;
import translateit2.persistence.dto.PersonDto;
import translateit2.persistence.dto.ProjectDto;
import translateit2.restapi.Projects;
import translateit2.service.ProjectService;

/* A popular solution to this problem is the use of Cross-Origin Resource Sharing 
 * (CORS). CORS is a W3C Recommendation, supported by all modern browsers, 
 * that involves a set of procedures and HTTP headers that together allow a 
 * browser to access data (notably Ajax requests) from a site other than the one 
 * from which the current page was served.'*/

//https://github.com/angular-ui/ui-router/wiki/Quick-Reference#ui-view
//https://angular.io/docs/ts/latest/guide/style-guide.html
//http://f1angular.com/angular-js/file-upload-using-angularjs-and-spring-mvc-example/

@RestController
@RequestMapping("/api")
public class RestProjectController {

	private ProjectService projectService;    
	@Autowired
	public void setProjectService(ProjectService projectService) {
		this.projectService = projectService;
	}
	
	private LngFileServiceFactory lngFileServiceFactory;
	@Autowired
	public void setLngFileServiceFactory(LngFileServiceFactory lngFileServiceFactory) {
		this.lngFileServiceFactory = lngFileServiceFactory;
	}
	
	public static final Logger logger = LoggerFactory.getLogger(RestProjectController.class);
				
	// -------------------Retrieve All Projects---------------------------------------------
	@RequestMapping(value = "/project/", method = RequestMethod.GET)
	public ResponseEntity<?> listAllProjects() {
		
		List<ProjectDto> projects = projectService.listAllProjectDtos();		
		if (projects.isEmpty()) projects = Collections.emptyList();
		
		HashMap<Long, Integer> workCountMap = new LinkedHashMap<Long, Integer>();
		
		//TODO: make a new method
		projects.forEach(prj -> workCountMap.put(prj.getId(), 
				projectService.listProjectWorkDtos(prj.getId()).size()));
		
		//TODO: where should I put these	
		List <AvailableFormat> formats = new ArrayList<AvailableFormat>();
		lngFileServiceFactory.listFormatsSupported().forEach(f->formats.add(new AvailableFormat(f)));
		
		AvailableCharacterSet cs = null;
		List <AvailableCharacterSet> csets = new ArrayList<AvailableCharacterSet>();
		cs = new AvailableCharacterSet();
		cs.setType(LngFileType.UTF_8);
		csets.add(cs);
		cs = new AvailableCharacterSet();
		cs.setType(LngFileType.ISO8859_1);
		csets.add(cs);
		
		Projects prjs = new Projects();
		prjs.setProjects(projects);
		prjs.setAvailableformats(formats);
		prjs.setAvailableCharacterSets(csets);
		prjs.setProjectWorkMap(workCountMap);
		return new ResponseEntity<>(prjs, HttpStatus.OK);
	}
	
	// -------------------Retrieve Single Project------------------------------------------

	@RequestMapping(value = "/project/{id}", method = RequestMethod.GET)
	public ResponseEntity<?> getProject(@PathVariable("id") long id) {
		logger.info("Fetching Project with id {}", id);

		ProjectDto prj = projectService.getProjectDtoById(id); 
	
		if (prj == null) {
			logger.error("Project with id {} not found.", id);
			return new ResponseEntity<>(new CustomErrorType("Project with id " + id 
					+ " not found"), HttpStatus.NOT_FOUND);
		}
		return new ResponseEntity<>(prj, HttpStatus.OK);
	}
	
	// -------------------Create a Project-------------------------------------------

	@RequestMapping(value = "/project/", method = RequestMethod.POST)
	public ResponseEntity<?> createProject(@Valid @RequestBody ProjectDto project, UriComponentsBuilder ucBuilder) {
		logger.info("Creating Project : {}", project);

		ProjectDto prj = projectService.getProjectDtoById(project.getId());
		if (prj != null) {
			logger.error("Unable to create. A Project with name {} already exist", project.getName());
			return new ResponseEntity<>(new CustomErrorType("Unable to create. A Project with name " + 
			project.getName() + " already exist."),HttpStatus.CONFLICT);
		}
		
		InfoDto infoDto = new InfoDto();
		infoDto.setText("This is info");
		infoDto = projectService.createInfoDto(infoDto);
		
		PersonDto personDto = projectService.getPersonDtoByPersonName("Ilkka"); 
		
		project.setInfoId(infoDto.getId());
		project.setPersonId(personDto.getId());
		prj = projectService.createProjectDto(project);
		
		HttpHeaders headers = new HttpHeaders();
		headers.setLocation(ucBuilder.path("/api/project/{id}").buildAndExpand(prj.getId()).toUri());
		return new ResponseEntity<Void>(headers, HttpStatus.CREATED);
	}


	// ------------------- Update a Project ------------------------------------------------
//	@RequestMapping(value = "/project/{id:\\d+}", method = RequestMethod.PUT)
	@RequestMapping(value = "/project/{id}", method = RequestMethod.PUT)
	public ResponseEntity<?> updateProject(@PathVariable("id") long id, @Valid @RequestBody ProjectDto project) {
		ProjectDto prj = projectService.getProjectDtoById(id);
		if (prj == null) {
			logger.error("Unable to update. Project with id {} not found.", id);
			return new ResponseEntity<>(new CustomErrorType("Unable to update. Project with id " + id + " not found."),
					HttpStatus.NOT_FOUND);
		}

		logger.info("Updating Project with id {}", project.getId());

		prj = projectService.updateProjectDto(project);
		return new ResponseEntity<>(prj, HttpStatus.OK);
	}

	// ------------------- Delete a Project-----------------------------------------
	@RequestMapping(value = "/project/{id}", method = RequestMethod.DELETE)
	public ResponseEntity<?> deleteProject(@PathVariable("id") long id) {
		logger.info("Fetching & Deleting Project with id {}", id);

		ProjectDto prj = projectService.getProjectDtoById(id);
		if (prj == null) {
			logger.error("Unable to delete. project with id {} not found.", id);
			return new ResponseEntity<>(new CustomErrorType("Unable to delete. Project with id " + id + " not found."),
					HttpStatus.NOT_FOUND);
		}
		
		projectService.removeProjectDto(id);
		return new ResponseEntity<>(HttpStatus.NO_CONTENT);
	}
}