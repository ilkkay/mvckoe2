package translateit2.lngfileservice;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class LngFileStorageConfiguration {
    @Bean
    public ISO8859Storage iso8859Service() {
        return new ISO8859StorageImpl();
    }
    
    @Bean
    public XLIFFStorage xliffService() {
        return new XLIFFStorageImpl();
    }
}
