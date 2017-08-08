package translateit2.fileloader;

import java.io.IOException;

public class FileLoaderException extends IOException  /* RuntimeException */  {
    private static final long serialVersionUID = 1L;

    private FileLoadError errorCode;

    public FileLoadError getErrorCode(){
        return this.errorCode;
    }

    public FileLoaderException(FileLoadError errorCode) {
        this.errorCode = errorCode;
    }
    
    public FileLoaderException(FileLoadError errorCode, String message){
        super(message);
        this.errorCode=errorCode;
    }

    public FileLoaderException(FileLoadError errorCode, Throwable cause) {
        super(cause);
        this.errorCode = errorCode;
    }

    //
    //  *********************************************    
    //

    public FileLoaderException(String message) {
        super(message);
    }

    public FileLoaderException(String message, Throwable cause) {
        super(message, cause);
    }

    public FileLoaderException(Throwable cause) {
        super(cause);
    }

}
