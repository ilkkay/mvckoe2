package com.crud.controller;

import java.text.DateFormat;
import java.util.Date;
import java.util.List;
import java.util.Locale;

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

import com.crud.dao.LocoHibDaoImpl;
import com.crud.dao.LocoMemDaoImpl;
import com.crud.model.Transu;

@Controller
public class LocoController {
	
    @Autowired  
    LocoMemDaoImpl locoDao;//will inject locoDao from servlet-context.xml file 
    
    @Autowired  
    LocoHibDaoImpl locoDaoDb;
    
	private static final Logger logger = LoggerFactory.getLogger(HomeController.class);
	private static final boolean USE_DB=true;
	
    @RequestMapping("/")  
    public ModelAndView showindex(){  
        return new ModelAndView("index");  
    }
    /*It displays a form to input data, here "command" is a reserved request attribute 
     *which is used to display object data into form 
     */  
    @RequestMapping("/transuform")  
    public ModelAndView showform(){  
        return new ModelAndView("transuform","command",new Transu());  
    }  
    /*It saves object into database. The @ModelAttribute puts request data 
     *  into model object. You need to mention RequestMethod.POST method  
     *  because default request is GET*/  
    @RequestMapping(value="/save",method = RequestMethod.POST)  
    public ModelAndView save(@ModelAttribute("transu") Transu transu){
    	if (!USE_DB)
    		locoDao.save(transu);
    	else
    		locoDaoDb.save(transu);
        return new ModelAndView("redirect:/viewtransu");//will redirect to viewtransu request mapping  
    }  
    /* It provides list of translation units in model object */  
    @RequestMapping("/viewtransu")  
    public ModelAndView viewtransu(){
    	List<Transu> list = null;
    	if (!USE_DB){
    		list=locoDao.getTransus();
    	}
    	else{
    		list=locoDaoDb.getTransus();
    	}
        return new ModelAndView("viewtransu","list",list);  
    }  
    /* It displays object data into form for the given id.  
     * The @PathVariable puts URL data into variable.*/  
    @RequestMapping(value="/edittransu/{id}")  
    public ModelAndView edit(@PathVariable int id){  
        Transu transu=null;
        if (!USE_DB)
        	transu=locoDao.getTransuById(id);
        else
        	transu=locoDaoDb.getTransuById(id);
        return new ModelAndView("transueditform","command",transu);  
    }  
    /* It updates model object. */  
    @RequestMapping(value="/editsave",method = RequestMethod.POST)  
    public ModelAndView editsave(@ModelAttribute("transu") Transu transu){  
    	if (!USE_DB)
    		locoDao.update(transu);
    	else
    		locoDaoDb.update(transu);
        return new ModelAndView("redirect:/viewtransu");  
    }  
    /* It deletes record for the given id in URL and redirects to /viewtransu */  
    @RequestMapping(value="/deleteemp/{id}",method = RequestMethod.GET)  
    public ModelAndView delete(@PathVariable int id){  
    	if (!USE_DB)
    		locoDao.delete(id);
    	else
    		locoDaoDb.delete(id);
        return new ModelAndView("redirect:/viewtransu");  
    }
	
}

