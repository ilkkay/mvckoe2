package translateit2.languagefileservice.factory;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Objects;
import java.util.Optional;
import java.util.stream.Collectors;

import javax.annotation.PostConstruct;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import translateit2.fileloader.storage.StorageException;
import translateit2.lngfileservice.LanguageFileFormat;
import translateit2.lngfileservice.LanguageFileStorage;

@Service
public class LanguageFileServiceFactoryImpl implements LanguageFileServiceFactory {
    // In case of a Collection or Map dependency type, the container will
    // autowire
    // all beans matching the declared value type. In case of a Map, the keys
    // must
    // be declared as type String and will be resolved to the corresponding bean
    // names.
    @Autowired
    private List<LanguageFileStorage> storageServices;

    private final Map<LanguageFileFormat, LanguageFileStorage> lngStorageServiceCache = new HashMap<>();

    @PostConstruct
    public void initLngServiceCache() {
        storageServices.forEach(s -> lngStorageServiceCache.put(s.getFileFormat(), s));
    }

    public Optional<LanguageFileStorage> getService(LanguageFileFormat type) {
        Map<LanguageFileFormat, LanguageFileStorage> services = lngStorageServiceCache.entrySet().stream()
                .filter(p -> p.getValue().getFileFormat().equals(type))
                .collect(Collectors.toMap(p -> p.getKey(), p -> p.getValue()));

        if (services.size() == 1)
            return Optional.of((LanguageFileStorage) services.get(type));
        else
            return Optional.<LanguageFileStorage>empty();
        // throw new StorageException("Did not find exactly one service for type
        // " + type);
    }

    public List<LanguageFileFormat> listFormatsSupported() {
        List<LanguageFileFormat> formats = lngStorageServiceCache.entrySet().stream().map(x -> x.getKey())
                .collect(Collectors.toList());
        return formats;
    }
}
