package translateit2.fileloader;

public class LoadedFileNotFoundException extends FileLoaderException {
    private static final long serialVersionUID = 1L;

    public LoadedFileNotFoundException(String message) {
        super(message);
    }

    public LoadedFileNotFoundException(String message, Throwable cause) {
        super(message, cause);
    }
}
