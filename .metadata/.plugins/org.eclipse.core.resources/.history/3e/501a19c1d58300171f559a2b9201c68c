package translateit2.fileloader;

import java.io.IOException;

public class FileLoaderServiceException extends IOException  /* RuntimeException */  {
    private static final long serialVersionUID = 1L;

    private FileLoadError errorCode;

    public FileLoadError getErrorCode(){
        return this.errorCode;
    }

    public FileLoaderServiceException(FileLoadError errorCode) {
        this.errorCode = errorCode;
    }
    
    public FileLoaderServiceException(FileLoadError errorCode, String message){
        super(message);
        this.errorCode=errorCode;
    }

    public FileLoaderServiceException(FileLoadError errorCode, Throwable cause) {
        super(cause);
        this.errorCode = errorCode;
    }

}
