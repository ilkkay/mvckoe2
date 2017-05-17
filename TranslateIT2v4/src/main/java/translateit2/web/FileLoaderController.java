package translateit2.web;
import translateit2.fileloader.storage.StorageException;
import translateit2.fileloader.storage.StorageFileNotFoundException;
import translateit2.fileloader.storage.StorageService;
import translateit2.lngfileservice.LngFileFormat;
import translateit2.lngfileservice.LngFileStorage;
import translateit2.lngfileservice.factory.LngFileServiceFactory;
import translateit2.persistence.dto.ProjectDto;
import translateit2.persistence.dto.WorkDto;
import translateit2.persistence.model.Status;
import translateit2.service.ProjectService;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.Resource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.mvc.method.annotation.MvcUriComponentsBuilder;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.io.IOException;
import java.nio.file.Path;
import java.util.List;
import java.util.stream.Collectors;

//
//TODO: This is not any more in use. This was used with html pages found in resources/templates folder
//The upload path works but the download is not in use because restApi uses at the moment the same path
//Rename the download url if need to use html pages
//
@Controller
public class FileLoaderController {
	private final StorageService storageService;
	private ProjectService projectService;
	private LngFileServiceFactory lngFileServiceFactory;

	@Autowired
	public FileLoaderController(StorageService storageService, 
			ProjectService projectService,
			LngFileServiceFactory lngFileServiceFactory) {
		this.storageService = storageService;
		this.projectService = projectService;
		this.lngFileServiceFactory = lngFileServiceFactory;
	}

	@GetMapping("/upload_source")
	public String listUploadedSourceFiles(Model model) throws IOException {
		model.addAttribute("destination","source");
		return "uploadForm";
	}

	@GetMapping("/upload_target")
	public String listUploadedTargetFiles(Model model) throws IOException {
		model.addAttribute("destination","target");
		return "uploadForm";
	}

	@GetMapping("/mock2download") //just for testing !!
	public String downloadFile2(Model model) throws IOException {
		model.addAttribute("message","Download translation");
		model.addAttribute("files", storageService
				.loadAll()
				.map(path ->
				MvcUriComponentsBuilder
				.fromMethodName(FileLoaderController.class, 
						"serveFile", path.getFileName().toString())
				.build().toString())
				.collect(Collectors.toList()));
		return "download";
	}
	
	@GetMapping("/mockDownload") //downloadFromDb
	public String downloadFile(Model model) throws IOException {
		ProjectDto prj = projectService.getProjectDtoByProjectName("Translate IT 2");
		List <WorkDto> works = projectService.listProjectWorkDtos(prj.getId());
		WorkDto work = works.get(0);
		LngFileStorage lngStorageService = lngFileServiceFactory.getService(prj.getFormat()).get();

		model.addAttribute("message","Download translation");
		model.addAttribute("files", lngStorageService
				.downloadFromDb(work.getId())
				.map(path ->
				MvcUriComponentsBuilder
				.fromMethodName(FileLoaderController.class, 
						"serveFile", path.getFileName().toString())
				.build().toString())
				.collect(Collectors.toList()));
		
		work = projectService.getWorkDtoById(work.getId());
		work.setStatus(Status.PENDING);
		projectService.updateWorkDto(work);
		return "download";
	}
	
	@GetMapping("/mockFiles/{filename:.+}")
	@ResponseBody
	public ResponseEntity<Resource> serveFile(@PathVariable String filename) {

		Resource file = storageService.loadAsResource(filename);
		ResponseEntity<Resource> response = null;
		response = ResponseEntity
				.ok()
				.header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=\""+file.getFilename()+"\"")
				.body(file);
		return response;
	}

	private String uploadLanguageFile(MultipartFile file, 
			String destination, RedirectAttributes redirectAttributes) 
					throws IOException{

		ProjectDto prj = projectService.getProjectDtoByProjectName("Translate IT 2");
		List <WorkDto> works = projectService.listProjectWorkDtos(prj.getId());
		WorkDto work = works.get(0);
		LngFileStorage storageService = lngFileServiceFactory.getService(prj.getFormat()).get();
		/*
		 * Upload language file
		 */
		Path uploadedLngFile = storageService.storeFile(file);

		/*
		 * check file format validity
		 */
		storageService.checkValidity(uploadedLngFile, work.getId());

		/*
		 * upload file to database
		 */ 
		if(("source").equals(destination)){
			storageService.uploadSourceToDb(uploadedLngFile, work.getId());
			work = projectService.getWorkDtoById(work.getId());
			work.setStatus(Status.NEW);
			projectService.updateWorkDto(work);
		}
		else if(("target").equals(destination)){
			storageService.uploadTargetToDb(uploadedLngFile, work.getId());
			work = projectService.getWorkDtoById(work.getId());
			work.setStatus(Status.OPEN);
			projectService.updateWorkDto(work);
		}
		else
			throw new StorageException("Unexpected error. Destination was neither source or target.");
		
		redirectAttributes.addFlashAttribute("message",
				"You uploaded " + destination + " file:" + file.getOriginalFilename() + "!");      

		
		return "redirect:/upload" + "_" + destination;
	}

	@PostMapping("/upload")	public String handleFileSourceUpload(@RequestParam("file") MultipartFile file,
			@RequestParam("destination") String destination,
			RedirectAttributes redirectAttributes) throws IOException {

		return uploadLanguageFile(file, destination, redirectAttributes);
	}
	
	@ExceptionHandler(StorageFileNotFoundException.class)
	public ResponseEntity<?> handleStorageFileNotFound(StorageFileNotFoundException exc) {
		return errorPage(exc);
	}

	@ExceptionHandler(StorageException.class)
	public ResponseEntity<?> handleStorage(StorageException exc) {
		return errorPage(exc);
	}

	private ResponseEntity<String> errorPage(StorageException exc){
		ResponseEntity<String> errorResponse = null;
		HttpHeaders responseHeaders = new HttpHeaders();
		responseHeaders.set("HeaderKey", "HeaderData");
		errorResponse =  new ResponseEntity<String>(
				"<h2>File upload error</h2>" + exc.getLocalizedMessage(), responseHeaders,
				HttpStatus.CREATED);    	
		return errorResponse;
	}
}
