package translateit2.fileloader;

public class CannotUploadFileException extends FileLoaderException {
    private static final long serialVersionUID = 1L;
    
    public CannotUploadFileException(String message) {
        super(message);
        // TODO Auto-generated constructor stub
    }

    public CannotUploadFileException(String message, Throwable cause) {
        super(message, cause);
        // TODO Auto-generated constructor stub
    }

}
