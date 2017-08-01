package translateit2.fileloader.storage;

public class LoadedFileNotFoundException extends FileLoaderServiceException {

    public LoadedFileNotFoundException(String message) {
        super(message);
    }

    public LoadedFileNotFoundException(String message, Throwable cause) {
        super(message, cause);
    }
}
