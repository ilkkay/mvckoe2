package translateit2.lngfileservice.factory;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Optional;
import java.util.function.Supplier;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import javax.annotation.PostConstruct;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import translateit2.lngfileservice.LngFileStorage;

@Service
public class LngFileServiceFactoryImpl2 implements LngFileServiceFactory {
	   // In case of a Collection or Map dependency type, the container will autowire 
	   // all beans matching the declared value type. In case of a Map, the keys must 
	   // be declared as type String and will be resolved to the corresponding bean names.	
	   @Autowired
	   private List<LngFileStorage> storageServices;
	   
	   public Optional<LngFileStorage> getService(String type) {	
		   Optional<LngFileStorage> empty = Optional.<LngFileStorage>empty();
		   
		   Optional<LngFileStorage> service =
				   storageServices.stream().filter(s -> type.equals(s.getType()))
				   		.findFirst();
		   if (service.isPresent())
			   return service;
		   else
			   return empty;
		   
		   // type must be an unique field in the corresponding entity
		   //return storageServices.stream().filter(s -> type.equals(s.getType()))
		   //	.findFirst();
		   	//		.orElse(empty);
	   }
	    
	   public List<String> listFormatsSupported() {	    	
	    	List<String> formats = 
	    			storageServices.stream()
	                .map(s -> s.getType())
	                .collect(Collectors.toList());
	    	
	        return formats;
	   }

}

