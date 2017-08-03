package translateit2.languagefactory;

import java.util.List;
import java.util.Optional;

public interface AbstractFactory <T, FORMAT> {
    Optional<T> getService(FORMAT format);
    
    List<FORMAT> listFormatsSupported();
}
