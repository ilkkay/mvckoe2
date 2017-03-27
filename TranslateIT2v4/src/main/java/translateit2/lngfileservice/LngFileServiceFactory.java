package translateit2.lngfileservice;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.PostConstruct;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class LngFileServiceFactory {
	   // In case of a Collection or Map dependency type, the container will autowire 
	   // all beans matching the declared value type. In case of a Map, the keys must 
	   // be declared as type String and will be resolved to the corresponding bean names.	
	   @Autowired
	   private List<LngFileStorage> storageServices;
	   
	   private static final Map<String, LngFileStorage> lngStorageServiceCache = 
			   new HashMap<>();
	   
	    @PostConstruct
	    public void initLngServiceCache() {
	    	storageServices.forEach(s->lngStorageServiceCache.put(s.getType(), s));
	    }

	    public static LngFileStorage getService(String type) {
	    	LngFileStorage service = lngStorageServiceCache.get(type);
	        if(service == null) 
	        	throw new RuntimeException("Unknown service type: " + type);
	        return service;
	    }	    
	    
}
