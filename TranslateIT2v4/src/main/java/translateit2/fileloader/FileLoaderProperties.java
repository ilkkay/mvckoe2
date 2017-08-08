package translateit2.fileloader;

import org.springframework.boot.context.properties.ConfigurationProperties;

@ConfigurationProperties("translateit2.fileloader")
public class FileLoaderProperties {

    private String location;

    /**
     * Folder location for uploading files
     */
    public String getLocation() {
        return location;
    }

    public void setLocation(String location) {
        this.location = location;
    }

}