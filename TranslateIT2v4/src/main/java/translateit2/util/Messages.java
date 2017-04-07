package translateit2.util;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.MessageSource;
import org.springframework.context.NoSuchMessageException;
import org.springframework.context.support.MessageSourceAccessor;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;

import java.util.Locale;

@Component
public class Messages {
    
    private MessageSource messageSource;
    
    @Autowired
    public Messages(MessageSource messageSource) {
		this.messageSource = messageSource;
	}

	private MessageSourceAccessor accessor; 
    
    // what does this MessageSourceAccessor really do
    // except setting locale ????
    @PostConstruct
    private void init() { // default is finnish for gui
        accessor = new MessageSourceAccessor(messageSource, new Locale("fi"));
    }
    
    // tests will be using english
    public void init(Locale locale) {
    	accessor = new MessageSourceAccessor(messageSource, locale);
    }
    
    public String get(String code,String[] args){
    	String msg = null;
    	try {
    		msg = accessor.getMessage(code, args);
    	} catch (NoSuchMessageException e) {
    		return "Text not implemented for " + code;	
    	}

    	return msg;
    }
    
    public String get(String code){
    	String msg = null;
    	try {
    		msg = accessor.getMessage(code); // which one to use ???
    		//msg = messageSource.getMessage(code, null, Locale.ENGLISH);
    	} catch (NoSuchMessageException e) {
    		return "Text not implemented for " + code;	
    	}    

    	return msg;
    }

}
