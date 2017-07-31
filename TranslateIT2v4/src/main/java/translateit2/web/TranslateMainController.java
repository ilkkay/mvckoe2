package translateit2.web;

import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

import translateit2.fileloader.storage.StorageService;

@Controller
public class TranslateMainController {

    @Autowired
    public void setStorageService(StorageService storageService) {
    }

    @GetMapping("/")
    public String homePage(Model model) {
        return "index";
    }

    @GetMapping("/translatemain")
    public String showMainUseCase(Model model) {
        return "translatemain";
    }

    @GetMapping("/viewtransu")
    public String viewTransList(Model model) {
        // model.addAttribute("transus",workService.listAllTranveTransuDtos(tranveDto.getId()));
        return "viewtransu";
    }

    @GetMapping("/transuform")
    public String createTransu(Model model) {
        // model.addAttribute("transudto",dto);
        return "transueditform";
    }

    // add new item or create a new one !!!
    /*
     * @PostMapping("/viewtransu") public String updateTransList(Model
     * model, @Valid @ModelAttribute("transudto") TransuDto transuDto,
     * BindingResult bindingResult) {
     * 
     * if (bindingResult.hasErrors()) { return "transueditform"; }
     * 
     * //model.addAttribute("transus",loco2Service.listAllTransuDtos(locoDto));
     * return "viewtransu"; }
     */
    @GetMapping("/edittransu")
    public String editTransu(@RequestParam(value = "transuId", required = false, defaultValue = "1") int id,
            Model model) {

        // model.addAttribute("transudto",transuDto);
        return "transueditform";
    }

    @GetMapping("/deletetransu/{transuId}")
    public String deleteTransuById(@PathVariable("transuId") int id, Model model) {

        return "redirect:/viewtransu";
    }

}