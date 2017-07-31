package translateit2.restapi;

import translateit2.lngfileservice.LanguageFileFormat;

public class AvailableFormat {
    private LanguageFileFormat type;

    private long id;

    public LanguageFileFormat getType() {
        return type;
    }

    public void setType(LanguageFileFormat type) {
        this.type = type;
    }

    public AvailableFormat(LanguageFileFormat type) {
        super();
        this.type = type;
    }

}
