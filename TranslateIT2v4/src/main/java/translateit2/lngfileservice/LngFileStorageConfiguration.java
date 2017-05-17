package translateit2.lngfileservice;

import translateit2.lngfileservice.iso8859.ISO8859Storage;
import translateit2.lngfileservice.iso8859.ISO8859StorageImpl;
import translateit2.lngfileservice.xliff.XLIFFStorage;
import translateit2.lngfileservice.xliff.XLIFFStorageImpl;

//
//	not in use. Just for testing ...
//

//@Configuration
public class LngFileStorageConfiguration {
    //@Bean
    public ISO8859Storage iso8859Service() {
        return new ISO8859StorageImpl();
    }
    
    //@Bean
    public XLIFFStorage xliffService() {
        return new XLIFFStorageImpl();
    }
}
