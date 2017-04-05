package translateit2.web;
import translateit2.fileloader.storage.StorageFileNotFoundException;
import translateit2.fileloader.storage.StorageService;
import translateit2.persistence.dto.LocoDto;
import translateit2.persistence.dto.TransuDto;
import translateit2.persistence.model.Transu;
import translateit2.service.Loco2Service;
import translateit2.service.Loco2ServiceImpl;
import translateit2.service.LocoServiceImpl;
import translateit2.service.TransuServiceImpl;
import translateit2.util.ISO8859Checker;
import translateit2.util.ISO8859Loader;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.Resource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.mvc.method.annotation.MvcUriComponentsBuilder;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.io.IOException;
import java.nio.file.Path;
import java.util.LinkedHashMap;
import java.util.LinkedHashSet;
import java.util.Locale;
import java.util.Map;
import java.util.function.Predicate;
import java.util.stream.Collectors;

@Controller
public class FileLoaderController {

    private final StorageService storageService;
    private Loco2Service loco2Service;

    @Autowired
    public FileLoaderController(StorageService storageService, 
    		TransuServiceImpl transuService, Loco2Service loco2Service) {
        this.storageService = storageService;
        this.loco2Service = loco2Service;
    }

    @GetMapping("/upload")
    public String listUploadedFiles(Model model) throws IOException {
        return "uploadForm";
    }

    @GetMapping("/download")
    public String downloadFile(Model model) throws IOException {
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
    
    @GetMapping("/files/{filename:.+}")
    @ResponseBody
    public ResponseEntity<Resource> serveFile(@PathVariable String filename) {

        Resource file = storageService.loadAsResource(filename);
        return ResponseEntity
                .ok()
                .header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=\""+file.getFilename()+"\"")
                .body(file);
    }
    
    @PostMapping("/upload")
    public String handleFileUpload(@RequestParam("file") MultipartFile file,
                                   RedirectAttributes redirectAttributes) throws IOException {

        storageService.store(file);
        
        /*
         * client should do this kind of sanity checks
         */
        String propFilename = ISO8859Checker.sanityCheck(file.getOriginalFilename());
        if (propFilename == null)
        	throw new IOException(); // redirect to error page locale missing 
        
        Predicate<String> p = (String ext) -> { return ext.equals("properties"); };
        
        Locale locale = ISO8859Checker.getLocaleFromString(
        		file.getOriginalFilename(), ext -> ext.equals("properties"));
        if (locale == null)
        	throw new IOException(); // redirect to error page locale missing       	

        /*
         * create initial target language file
         */
        ISO8859Loader.initTargetLanguageFile(storageService.load(file.getOriginalFilename()),
        		propFilename, null);
                   
        /*
         * get segments tags (property keys) and segments (property values)
         * 
         */
        
    	Path filePath = storageService.load(file.getOriginalFilename());
        String lngFileLocation=filePath.toAbsolutePath().toString();        
        LinkedHashMap<String,String> map=ISO8859Loader.getPropSegments(lngFileLocation);

		LocoDto locoDto = new LocoDto();
		locoDto.setProjectName("Translate IT 2");
		locoDto.setName("Ilkka");
		locoDto=loco2Service.createLocoDto(locoDto);
        
        // testing ...
        int i=0;
        TransuDto transuDto = null;
        for (Map.Entry<String,String> entry : map.entrySet())
        {
        	if (++i>=20) break;
        	
        	transuDto = new TransuDto();        	
        	transuDto.setSourceSegm(entry.getKey());
        	transuDto.setTargetSegm(entry.getValue());
        	transuDto.setRowId(i);
        	locoDto=loco2Service.createTransuDto(transuDto, locoDto.getId());
        	
        }                        
            	
        redirectAttributes.addFlashAttribute("message",
                "You successfully uploaded " + file.getOriginalFilename() + "!");

        return "redirect:/upload";
    }

    @ExceptionHandler(StorageFileNotFoundException.class)
    public ResponseEntity handleStorageFileNotFound(StorageFileNotFoundException exc) {
        return ResponseEntity.notFound().build();
    }

}
