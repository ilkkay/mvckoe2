package translateit2.filenameresolver;

import java.util.Locale;
import java.util.function.Predicate;

public interface FileNameResolver {
    
    public String getApplicationName(String filename);
        
    Locale getLocaleFromString(String fileName, Predicate<String> p);
    
}
