package translateit2.fileloader;

public class FileLoaderException extends RuntimeException {
    private static final long serialVersionUID = 1L;
    
    public FileLoaderException(String message) {
        super(message);
    }

    public FileLoaderException(String message, Throwable cause) {
        super(message, cause);
    }
    
    FileLoadError errorCode;

    public FileLoaderException(FileLoadError errorCode) {
        this.errorCode = errorCode;
    }

    public FileLoaderException(FileLoadError errorCode, Throwable cause) {
        super(cause);
        this.errorCode = errorCode;
    }

    public FileLoaderException(Throwable cause) {
        super(cause);
    }
    
}
