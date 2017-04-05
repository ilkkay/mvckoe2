package translateit2.util;


import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.io.UnsupportedEncodingException;
import java.io.Writer;
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
	
    @SuppressWarnings("unused")
	public static void copyISO8859toUTF8(String srcFileLocationStr, String dstFileLocationStr) {
        InputStream stream = null;
        InputStreamReader isr = null;
        OutputStream outputStream = null;
        Writer outputStreamWriter = null;
        
        try {
			outputStream= new FileOutputStream(dstFileLocationStr);
		} catch (FileNotFoundException e3) {
			// TODO Auto-generated catch block
			e3.printStackTrace();
		}
        
        try {
			outputStreamWriter = new OutputStreamWriter(outputStream, "UTF-8");
		} catch (UnsupportedEncodingException e2) {
			// TODO Auto-generated catch block
			e2.printStackTrace();
		}
        
        try {
			stream = new FileInputStream(srcFileLocationStr);
		} catch (FileNotFoundException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}
        
        try {
			isr = new InputStreamReader(stream,"ISO-8859-1");
		} catch (UnsupportedEncodingException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} 
		
        BufferedReader bufRdr  = new BufferedReader(isr);
        String line;
        try {
			while ((line = bufRdr.readLine()) != null) {
			    //System.out.println(line);
			    outputStreamWriter.write(line+"\n");
			}
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} finally {
			try {
				isr.close();
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			try {
				stream.close();
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			try {
				outputStreamWriter.close();
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			try {
				outputStream.close();
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
    }
    
	public static String initTargetLanguageFile(Path srcPath, 
    		String propFilename, Locale tgtLocale) throws IOException{
    	
    	if (tgtLocale == null) tgtLocale = Locale.getDefault();
    	
    	String tgtFilenameStr=propFilename+"_"+tgtLocale.toString()+".properties"; 
    	Path dir = srcPath.getParent();        
        Path fn = srcPath.getFileSystem().getPath(tgtFilenameStr);
        Path target = (dir == null) ? fn : dir.resolve(fn);
        String tgtFileLocationStr = target.toString();
        String srcFileLocationStr = srcPath.toString();
        
        File tgtFile = new File(tgtFileLocationStr);        
        InputStream stream = null;
        InputStreamReader isr = null;
        
        OrderedProperties dstProp = new OrderedProperties();
        OrderedProperties srcProp = new OrderedProperties();
        
        try {
        	stream = new FileInputStream(srcFileLocationStr);
        } catch (Exception e) {
        	e.printStackTrace();
        }

        try {
            //isr = new InputStreamReader(stream,"ISO-8859-1");
            isr = new InputStreamReader(stream);
        } catch (Exception e) {
        	e.printStackTrace();
        }
        
        try {
	        srcProp.load(isr); 
			
			// nullifies property values	
			Set<String> keys = srcProp.stringPropertyNames();
			keys.stream().forEach(key->dstProp.setProperty(key.toString(), ""));

			FileOutputStream fileOut = new FileOutputStream(tgtFile);
			dstProp.store(fileOut, "Translate IT 2");
			fileOut.close();
			
		} catch (Exception e) {
			System.out.println("Exception: " + e);
		} finally {
			isr.close();
			stream.close();
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
