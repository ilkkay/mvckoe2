package translateit2.util;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.MessageSource;
import org.springframework.context.NoSuchMessageException;
import org.springframework.context.support.MessageSourceAccessor;
import org.springframework.stereotype.Component;

import translateit2.lngfileservice.LngFileStorage;

import javax.annotation.PostConstruct;

import java.io.UnsupportedEncodingException;
import java.util.Locale;

@Component
public class Messages {
    
    private MessageSource messageSource;
    
    @Autowired
    public Messages(MessageSource messageSource) {
		this.messageSource = messageSource;
	}

	private MessageSourceAccessor accessor; 
    
    @PostConstruct
    private void init() {
        accessor = new MessageSourceAccessor(messageSource, new Locale("fi"));
    }
    
    // what does this MessageSourceAccessor really do??
    public void init(Locale locale) {
    	accessor = new MessageSourceAccessor(messageSource, new Locale("fi"));
    }
    
    public String get(String code,String[] args){
    	String msg = null;
    	try {
    		msg = accessor.getMessage(code);
    	} catch (NoSuchMessageException e) {
    		return "Text not implemented for " + code;	
    	}

    	if (args.length>0){
        	msg = String.format(msg, (Object[])args);        	
    	}
    	return msg;
    }
    
    // http://stackoverflow.com/questions/4659929/how-to-use-utf-8-in-resource-properties-with-resourcebundle
    public String get(String code){
    	String msg = null;
    	try {
    		msg = accessor.getMessage(code);
    	} catch (NoSuchMessageException e) {
    		return "Text not implemented for " + code;	
    	}
    	
    	/*
    	byte b1[] = accessor.getMessage(code).getBytes();
    	byte b2[] = messageSource.getMessage(code, null, new Locale("fi")).getBytes();

		try {
			msg = new String(accessor.getMessage(code).getBytes("iso-8859-1"),"utf-8");
		} catch (UnsupportedEncodingException e) {
			e.printStackTrace();
		}
		*/

    	return msg;
    }

}
