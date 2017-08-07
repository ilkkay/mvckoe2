package translateit2.fileloader;

public class FileLoaderException extends RuntimeException {
    private static final long serialVersionUID = 1L;
    
    public FileLoadError errorCode;

    public FileLoaderException(FileLoadError errorCode) {
        //super("FileLoadError");
        this.errorCode = errorCode;
    }
    
    public FileLoaderException(FileLoadError errorCode, Throwable cause) {
        super(cause);
        this.errorCode = errorCode;
    }

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
