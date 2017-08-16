package translateit2.restapi;

import java.util.Arrays;
import java.util.List;

import translateit2.fileloader.FileLoadError;

public class CustomErrorType {

    List<String> errorMessages;

    private String localizedErrorMessage;

    private FileLoadError errorCode;
    
    public CustomErrorType(String localizedErrorMessage, List<String> errorMessages) {
        this.localizedErrorMessage = localizedErrorMessage;
        this.errorMessages = errorMessages;
        this.errorCode = FileLoadError.UNDEFINED_ERROR;
    }
   
    public CustomErrorType(String errorMessage, FileLoadError errorCode) {
        this.errorMessages = Arrays.asList(errorMessage);
        this.errorCode = errorCode;
    }
    
    public CustomErrorType(List<String> errorMessages, FileLoadError errorCode) {
        this.errorMessages = errorMessages;
        this.errorCode = errorCode;
    }

    public CustomErrorType(String localizedErrorMessage, String errorMessage, FileLoadError errorCode) {
        this.localizedErrorMessage = localizedErrorMessage;
        this.errorMessages = Arrays.asList(errorMessage);
        this.errorCode = errorCode;
    }

    public String getLocalizedErrorMessage() {
        return localizedErrorMessage;
    }
    
    public List<String> getErrorMessage() {
        return errorMessages;
    }

    public FileLoadError getErrorCode() {
        return errorCode;
    }
}
