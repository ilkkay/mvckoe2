package com.crud.controller;

import java.io.IOException;
import java.text.DateFormat;
import java.util.Date;
import java.util.Enumeration;
import java.util.List;
import java.util.Locale;
import java.util.ResourceBundle;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.servlet.ModelAndView;

import com.crud.model.Transu;
import com.crud.util.ISO8859Loader;
import com.crud.util.ISO8859LocoDbLoader;

/**
 * Handles requests for the application home page.
 */
@Controller
public class HomeController {
    
	private static final Logger logger = LoggerFactory.getLogger(HomeController.class);
	
	/**
	 * Simply selects the home view to render by returning its name.
	 */

	@RequestMapping(value = "/home", method = RequestMethod.GET)
	public String home(Locale locale, Model model) {
		logger.info("Welcome home! The client locale is {}.", locale);
		
		Date date = new Date();
		DateFormat dateFormat = DateFormat.getDateTimeInstance(DateFormat.LONG, DateFormat.LONG, locale);
		
		String formattedDate = dateFormat.format(date);
		
		model.addAttribute("serverTime", formattedDate );
	    
		ISO8859LocoDbLoader.Load();
		
		return "home";
	}
	
    @RequestMapping("/uc_translatemain")  
    public ModelAndView showUseCaseTransMain(){  
    	String ucImageName="/resources/images/translateMain.PNG";
        return new ModelAndView("uc_translatemain","ucImageName",ucImageName);  
    }
}
