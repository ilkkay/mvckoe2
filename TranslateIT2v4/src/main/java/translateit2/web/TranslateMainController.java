package translateit2.web;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

import translateit2.fileloader.storage.StorageService;
import translateit2.persistence.dto.LocoDto;
import translateit2.persistence.dto.TransuDto;
import translateit2.persistence.model.Loco;
import translateit2.persistence.model.Transu;
import translateit2.service.Loco2ServiceImpl;
import translateit2.service.LocoServiceImpl;
import translateit2.service.TransuServiceImpl;

@Controller
public class TranslateMainController {	
    
	private LocoDto locoDto; 

    private StorageService storageService;
    @Autowired
    public void setStorageService(StorageService storageService) {
        this.storageService = storageService;
    }
    
    private TransuServiceImpl transuService;
    @Autowired
    public void setTransuService(TransuServiceImpl transuService) {
        this.transuService = transuService;
    }
    
    private Loco2ServiceImpl loco2Service;    
    @Autowired
    public void setLoco2Service(Loco2ServiceImpl loco2Service) {
        this.loco2Service = loco2Service;
    }
    
    @GetMapping("/")
    public String homePage(Model model) {
        return "index";
    }

    @GetMapping("/translatemain")
    public String showMainUseCase( Model model) {
        return "translatemain";
    }
    
    @GetMapping("/viewtransu")
    public String viewTransList( Model model) {
    	
    	// http://stackoverflow.com/questions/271526/avoiding-null-statements
    	// http://softwaregarden.io/avoiding-null-checks-in-java/
    	// https://www.tutorialspoint.com/design_pattern/null_object_pattern.htm
    	if (locoDto==null)	
    		locoDto = loco2Service.getLocoDtoByProjectName("Translate IT 2");
   
    	model.addAttribute("transus",loco2Service.listAllTransuDtos(locoDto));	
        return "viewtransu";
    }
    
    @GetMapping("/transuform")
    public String createTransu( Model model) {
    	Transu t = new Transu();
    	model.addAttribute("transu",t);	
        return "transueditform";
    }
    
    // add new item or create a new one !!!
    @PostMapping("/viewtransu")
    public String updateTransList(Model model, @ModelAttribute TransuDto transuDto) {
    	if (transuDto.getId() != 0){
        	locoDto = loco2Service.updateTransuDto(transuDto);
    	}
    	else{
    		locoDto = loco2Service.createTransuDto(transuDto,locoDto.getId());
    	}

    	model.addAttribute("transus",loco2Service.listAllTransuDtos(locoDto));
        return "viewtransu";
    }
    
    @GetMapping("/edittransu")
    public String editTransu(@RequestParam(value="transuId", required=false, 
    		defaultValue="1") int id, Model model) {
    	TransuDto transuDto=loco2Service.getTransuDtoByRowId(id,locoDto.getId());
    	model.addAttribute("transu",transuDto);	
        return "transueditform";
    }
    
    
    // Spring MVC: Beginner's Guide Google books
    
    @GetMapping("/deletetransu/{transuId}")
    public String deleteTransuById(@PathVariable("transuId") int id, Model model) {
    	TransuDto transuDto=loco2Service.getTransuDtoByRowId(id,locoDto.getId());
    	locoDto = loco2Service.removeTransuDto(transuDto);
    	
    	return "redirect:/viewtransu";
    }
    
    /*
    @GetMapping("/deletetransu")
    public String deleteTransu(@RequestParam(value="transuId", required=false, 
    		defaultValue="1") int id, Model model) {
    	transuService.deleteTransu((long)(id));
    	//model.addAttribute("transus",loco.getTransus());
    	//model.addAttribute("transus",transuService.getTransusByLocoId(locoId));
    	model.addAttribute("transus",transuService.getTransusByLocoId(loco.getId()));
        return "redirect:/viewtransu";
    }
    */

}