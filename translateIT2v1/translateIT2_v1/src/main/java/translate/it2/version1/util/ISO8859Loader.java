package translate.it2.version1.util;


import java.io.IOException;
import java.util.ArrayList;
import java.util.Enumeration;
import java.util.List;
import java.util.ResourceBundle;

import translate.it2.version1.model.Transu;

public class ISO8859Loader {
	public static void init() throws IOException {
	    try {
	        ResourceBundle rb = ResourceBundle.getBundle("com.crud.dotcms");
	        Enumeration<String> keys = rb.getKeys();
	        while(keys.hasMoreElements()) {
	            String key = keys.nextElement();
	            //System.out.println(key + " - " + rb.getObject(key));
	            System.out.println(rb.getObject(key));
	        }
	    } catch (Exception e) {
	        e.printStackTrace();
	        throw new IOException("Error loading config.", e);
	    }       
	}
	
	public static List<String> getSourceSegments() throws IOException {
		List <String> srcSegmnts =new ArrayList<String>();
		
	    try {
	        ResourceBundle rb = ResourceBundle.getBundle("com.crud.dotcms");
	        Enumeration<String> keys = rb.getKeys();
	        while(keys.hasMoreElements()) {
	            String key = keys.nextElement();
	            //System.out.println(key + " - " + rb.getObject(key));
	            //System.out.println(rb.getObject(key));
	            srcSegmnts.add(rb.getObject(key).toString());
	        }
	    } catch (Exception e) {
	        e.printStackTrace();
	        throw new IOException("Error loading config.", e);
	    }       
	    
	    return srcSegmnts;
	}
	
	public static List<Transu> getTransus() throws IOException {
		List <Transu> transus =new ArrayList<Transu>();
		
		// tämä com.crud.dotcms_en tunnistus onnistuu vain serverillä? ei javan main:illä
	    try {
	    	int id=1;
	        ResourceBundle rb = ResourceBundle.getBundle("com.crud.dotcms_en");
	        ResourceBundle rb_fi = ResourceBundle.getBundle("com.crud.dotcms_fi");
	        Enumeration<String> keys = rb.getKeys();
	        while(keys.hasMoreElements()) {
	            String key = keys.nextElement();
	    		Transu t=new Transu();
	    		t.setId(id++); // tätä ei pitäisi tehdä, koska hibernate inkrementoi tämän automaattisesti
	    		t.setSourceSegm(rb.getObject(key).toString());
	    		if (true)
	    			t.setTargetSegm(rb_fi.getObject(key).toString());
	    		else
	    			t.setTargetSegm("");
	    		transus.add(t);
	        }
	    } catch (Exception e) {
	        e.printStackTrace();
	        throw new IOException("Error loading config.", e);
	    }       
	    
	    return transus;
	}
}
