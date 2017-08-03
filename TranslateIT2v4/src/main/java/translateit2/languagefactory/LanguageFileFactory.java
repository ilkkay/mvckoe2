package translateit2.languagefactory;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;

import javax.annotation.PostConstruct;

import org.springframework.beans.factory.annotation.Autowired;

import translateit2.languagefileservice.factory.AbstractLanguageFile;

public class LanguageFileFactory <T extends AbstractLanguageFile <FORMAT>, FORMAT> 
    implements AbstractFactory <T, FORMAT> {

    private final Map<FORMAT, T> serviceCache = new HashMap<>();

    @Autowired
    private List<T> services;

    @PostConstruct
    public void initLngServiceCache() {
        services.forEach(s -> serviceCache.put(s.getFileFormat(), s));
    }
    
    @Override
    public Optional<T> getService(FORMAT type) {
        Map<FORMAT, T> services = serviceCache.entrySet().stream()
                .filter(p -> p.getValue().getFileFormat().equals(type))
                .collect(Collectors.toMap(p -> p.getKey(), p -> p.getValue()));
        if (services.size() == 1)
            return Optional.of((T) services.get(type));
        else
            return Optional.<T>empty();
    }
    
    @Override
    public List<FORMAT> listFormatsSupported() {
        List<FORMAT> formats = serviceCache.entrySet().stream().map(x -> x.getKey())
                .collect(Collectors.toList());
        return formats;
    }

}
