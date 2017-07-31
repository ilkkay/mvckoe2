package translateit2.lngfileservice;

import translateit2.languagefileservice.xliff.XLIFFStorage;
import translateit2.languagefileservice.xliff.XLIFFStorageImpl;
import translateit2.lngfileservice.iso8859.Iso8859Storage;
import translateit2.lngfileservice.iso8859.Iso8859StorageImpl;

//
//	not in use. Just for testing ...
//

//@Configuration
public class LanguageFileStorageConfiguration {
    //@Bean
    public Iso8859Storage iso8859Service() {
        return new Iso8859StorageImpl();
    }
    
    //@Bean
    public XLIFFStorage xliffService() {
        return new XLIFFStorageImpl();
    }
}
