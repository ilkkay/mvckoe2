package translateit2.fileloader;

public class CannotReadFileException extends RuntimeException {
    private static final long serialVersionUID = 1L;
    
    public CannotReadFileException(String message, Throwable cause) {
        super(message, cause);
        // TODO Auto-generated constructor stub
    }

    public CannotReadFileException(Throwable throwable) {
        super(throwable);
        // TODO Auto-generated constructor stub
    }

}
