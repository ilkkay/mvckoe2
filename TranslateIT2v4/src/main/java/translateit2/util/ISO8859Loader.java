package translateit2.util;


import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.IOException;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.Enumeration;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Locale;
import java.util.ResourceBundle;
import java.util.Set;
import java.util.stream.Collectors;

import translateit2.persistence.model.Transu;

public class ISO8859Loader {
	
	public static String initTargetLanguageFile(Path tgtPath, 
    		String propFilename, Locale tgtLocale) throws IOException{
    	
    	if (tgtLocale == null) tgtLocale = Locale.getDefault();
    	
    	String tgtFilenameStr=propFilename+"_"+tgtLocale.toString()+".properties";        
    	Path dir = tgtPath.getParent();        
        Path fn = tgtPath.getFileSystem().getPath(tgtFilenameStr);
        Path target = (dir == null) ? fn : dir.resolve(fn);
        String tgtFileLocationStr = target.toString();
        
        File tgtFile = new File(tgtFileLocationStr);
             
        FileReader fr = null;
        try {
	        OrderedProperties dstProp = new OrderedProperties();
	        OrderedProperties srcProp = new OrderedProperties();
	        fr = new FileReader(tgtFile );
 
			srcProp.load(fr); 
			
			// nullifies property values	
			Set<String> keys = srcProp.stringPropertyNames();
			keys.stream().forEach(key->dstProp.setProperty(key.toString(), ""));
	/*		
			for (Object key : keys){
				dstProp.setProperty(key.toString(), "");
				System.out.println(srcProp.getProperty(key.toString()));
			}
 	*/
			FileOutputStream fileOut = new FileOutputStream(tgtFile);
			dstProp.store(fileOut, "Translate IT 2");
			fileOut.close();
			
		} catch (Exception e) {
			System.out.println("Exception: " + e);
		} finally {
			fr.close();
		}
        
        return tgtFileLocationStr;
    }

	public static LinkedHashMap<String, String> getPropSegments(String lngFileLocation)
    		throws IOException{
    	
    	String inputFileStr=lngFileLocation;
    	FileReader fr = null;
    	LinkedHashMap<String, String> map = null;
		LinkedHashMap<String, String> map2 = null;
    	try {
			fr = new FileReader(inputFileStr);
			OrderedProperties srcProp = new OrderedProperties();
			srcProp.load(fr); 
			Set<String> keys = srcProp.stringPropertyNames();
			map = new LinkedHashMap<String, String>();
			for (Object key : keys){
				String value = srcProp.getProperty(key.toString());
				map.put(key.toString(), value);
				//System.out.println(value);
			}
			
			/*
			 * Lets do it Java 8 style
			 */
			map2 = keys.stream ().collect (Collectors.toMap (
					k -> k.toString(),k -> srcProp.getProperty(k),
	                       (v1,v2)->v1,LinkedHashMap::new));
			map2.forEach((k,v)->System.out.println(k + "\n" + v));
			
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			throw new IOException("Error loading reading property file.", e);
		} 
    	finally {
			fr.close();
		}
    	
		return map;
    }
    
	public static List<Transu> getTransus() throws IOException {
		List <Transu> transus =new ArrayList<Transu>();
		
		try {
	    	int id=1;
	    	
	    	ResourceBundle rb = ResourceBundle.getBundle("translateit2.myresr.dotcms_en");
	        ResourceBundle rb_fi = ResourceBundle.getBundle("translateit2.myresr.dotcms_fi");
	        
	        Enumeration<String> keys = rb.getKeys();
	        while(keys.hasMoreElements()) {
	            String key = keys.nextElement();
	    		Transu t=new Transu();
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
