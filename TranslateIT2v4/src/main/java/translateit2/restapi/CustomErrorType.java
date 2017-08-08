package translateit2.restapi;

import translateit2.fileloader.FileLoadError;

public class CustomErrorType {

    private String errorMessage;

    private FileLoadError errorCode;

    public CustomErrorType(String errorMessage) {
        this.errorMessage = errorMessage;
        this.errorCode = FileLoadError.UNDEFINED_ERROR;
    }
    
    public CustomErrorType(String errorMessage, FileLoadError errorCode) {
        this.errorMessage = errorMessage;
        this.errorCode = errorCode;
    }

    public String getErrorMessage() {
        return errorMessage;
    }

    public FileLoadError getErrorCode() {
        return errorCode;
    }
}
