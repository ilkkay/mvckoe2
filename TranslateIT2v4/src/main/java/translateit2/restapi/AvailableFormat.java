package translateit2.restapi;

import translateit2.lngfileservice.LanguageFileFormat;

public class AvailableFormat {
    private long id;

    private LanguageFileFormat type;

    public AvailableFormat(LanguageFileFormat type) {
        super();
        this.type = type;
    }

    public LanguageFileFormat getType() {
        return type;
    }

    public void setType(LanguageFileFormat type) {
        this.type = type;
    }

}
