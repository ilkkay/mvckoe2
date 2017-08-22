package translateit2.fileloader;

import org.springframework.boot.context.properties.ConfigurationProperties;

@ConfigurationProperties(prefix = "translateit2.fileloader")
public class FileLoaderProperties {

    private String uploadLocation;
    private String downloadLocation;
    private String permanentDirectory; 
    
    public String getUploadLocation() {
        return uploadLocation;
    }

    public void setUploadLocation(String location) {
        this.uploadLocation = location;
    }
    
    public String getDownloadLocation() {
        return downloadLocation;
    }

    public void setDownloadLocation(String location) {
        this.downloadLocation = location;
    }
    
    public void setPermanentDirectory(String permanentDirectory) {
        this.permanentDirectory = permanentDirectory;
    }

    public String getPermanentDirectory() {
        return this.permanentDirectory ;
    }
}