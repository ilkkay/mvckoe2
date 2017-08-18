package translateit2.web;

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

import translateit2.configuration.LanguageServicesConfig;
import translateit2.persistence.dto.ProjectDto;
import translateit2.restapi.ViewProjects;
import translateit2.service.ProjectService;

@RestController
@RequestMapping("/api")
public class RestProjectController {

    public static final Logger logger = LoggerFactory.getLogger(RestProjectController.class);

    @Autowired
    private ProjectService projectService;

    @Autowired
    private LanguageServicesConfig languageServices;

    // -------------------Create a new Project
    // ------------------------------------------
    @RequestMapping(value = "/project/", method = RequestMethod.POST)
    public ResponseEntity<?> createProject(@Valid @RequestBody ProjectDto project, UriComponentsBuilder ucBuilder) {
        logger.info("Creating Project : {}", project);

        ProjectDto prj = projectService.createProjectDto(project,"Ilkka");

        HttpHeaders headers = new HttpHeaders();
        headers.setLocation(ucBuilder.path("/api/project/{id}").buildAndExpand(prj.getId()).toUri());
        return new ResponseEntity<Void>(headers, HttpStatus.CREATED);
    }

    // ------------------- Delete a Project
    // ----------------------------------------
    @RequestMapping(value = "/project/{id}", method = RequestMethod.DELETE)
    public ResponseEntity<?> deleteProject(@PathVariable("id") long id) {
        logger.info("Fetching & Deleting Project with id {}", id);

        projectService.removeProjectDto(id);

        return new ResponseEntity<>(HttpStatus.OK);
    }

    // -------------------Get a Project
    // -------------------------------------------
    @RequestMapping(value = "/project/{id}", method = RequestMethod.GET)
    public ResponseEntity<?> getProject(@PathVariable("id") long id) {
        logger.info("Fetching Project with id {}", id);

        ProjectDto prj = projectService.getProjectDtoById(id);

        return new ResponseEntity<>(prj, HttpStatus.OK);
    }

    // -------------------Retrieve All Projects
    // ---------------------------------------------
    @RequestMapping(value = "/project/", method = RequestMethod.GET)
    public ResponseEntity<?> listAllProjects(UriComponentsBuilder ucBuilder) {

        ViewProjects viewPrjs = new ViewProjects();

        viewPrjs.setProjects(projectService.listAllProjectDtos());
        viewPrjs.setProjectWorkMap(projectService.getWorkCountPerProject("Ilkka"));
        viewPrjs.setSupportedFormats(languageServices.listSupportedFormats());
        viewPrjs.setSupportedCharacterSets(languageServices.listSupportedCharacterSets());

        return new ResponseEntity<>(viewPrjs, HttpStatus.OK);
    }

    // ------------------- Update a Project
    // ------------------------------------------------
    @RequestMapping(value = "/project/{id}", method = RequestMethod.PUT)
    public ResponseEntity<?> updateProject(@PathVariable("id") long id, @Valid @RequestBody ProjectDto project) {

        logger.info("Updating Project with id {}", project.getId());
        
        ProjectDto prj = projectService.getProjectDtoById(id);
        prj = projectService.updateProjectDto(project);
        
        return new ResponseEntity<>(prj, HttpStatus.OK);
    }

}