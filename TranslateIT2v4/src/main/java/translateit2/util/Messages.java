package translateit2.util;

import org.junit.Test;
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
    		msg = accessor.getMessage(code); 
    	} catch (NoSuchMessageException e) {
    		return "Text not implemented for " + code;	
    	}    

    	return msg;
    }

    // Segment size must be between {0} and {1} characters
    public String getPart(String code){
    	String msg = null;
    	try {
    		msg = accessor.getMessage(code); 
    	} catch (NoSuchMessageException e) {
    		return "Text not implemented for " + code;	
    	}  
    	
    	String parts[] = msg.split("[{]");    	
    	if (parts.length == 1)
    		return msg;
    	
    	int maxLen = -1;
    	String maxStr = "";
    	for (String s : parts) {
    		if (s.length() > maxLen) {
    			maxStr = s;
    			maxLen = s.length(); 
    		}
    	}
    	
    	parts = maxStr.split("[}]");
    	if (parts.length == 1)
    		return maxStr;
    	else
    		return parts[1];
    }
    
}
