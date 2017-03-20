package translateit2;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

import translateit2.fileloader.storage.StorageService;
import translateit2.persistence.model.Loco;
import translateit2.persistence.model.Transu;
import translateit2.service.LocoServiceImpl;
import translateit2.service.TransuServiceImpl;

@Controller
public class TranslateMainController {	
    
	private Loco loco; 

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
    
    private LocoServiceImpl locoService;    
    @Autowired
    public void setLocoService(LocoServiceImpl locoService) {
        this.locoService = locoService;
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
    	if (loco==null)	
    		loco = locoService.getLocoByProjectName("Translate IT 2");
    	
    	model.addAttribute("transus",transuService.getTransusByLocoId(loco.getId()));	
    	
    	// doesn't work at all
    	//model.addAttribute("transus",transuService.getTransusInOrderByLocoId(locoId));	
    	
    	//loco.getTransus() is not in order <= Set <Transu> ...
    	//model.addAttribute("transus",loco.getTransus());	
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
    public String updateTransList(Model model, @ModelAttribute Transu transu) {
    	if (transu.getId() != 0){
    		//transuService.update(transu);
    		transu.setLoco(loco);
    		locoService.updateLoco(loco);
    	}
    	else{
    		//transuService.create(transu);
    		transu.setLoco(loco);
    		locoService.createLoco(loco);
    	}

    	    	
    	// doesn't work at all
    	// model.addAttribute("transus",loco.getTransus());
    	
    	// update won't work
    	//model.addAttribute("transus",transuService.getTransusByLocoId(loco.getId()));
    	
    	//this does show all anyhow
    	model.addAttribute("transus",transuService.getAllTransus());
        return "viewtransu";
    }
    
    @GetMapping("/edittransu")
    public String editTransu(@RequestParam(value="transuId", required=false, 
    		defaultValue="1") int id, Model model) {
    	Transu t = transuService.getTransuById((long)(id));
    	model.addAttribute("transu",t);	
        return "transueditform";
    }
    
    
    // Spring MVC: Beginner's Guide Google books
    
    @GetMapping("/deletetransu/{transuId}")
    public String deleteTransuById(@PathVariable("transuId") int id, Model model) {
    	Transu t = transuService.getTransuById((long)(id));
    	t.setLoco(null);
    	loco.removeTransu(t);
    	locoService.updateLoco(loco);
    	//model.addAttribute("transus",loco.getTransus());
    	
    	//this will hide updates, but they show up after a new update/save
    	//transuService.deleteTransu((long)(id));    	
    	model.addAttribute("transus",transuService.getTransusByLocoId(loco.getId()));	

    	//this does show all anyhow
    	//model.addAttribute("transus",transuService.getAllTransus());
    	return "viewtransu";
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
