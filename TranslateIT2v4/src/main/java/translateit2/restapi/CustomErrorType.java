package translateit2.restapi;

import java.util.Arrays;
import java.util.List;

import translateit2.exception.TranslateIt2Error;

public class CustomErrorType {

    List<String> errorMessages;

    private String localizedErrorMessage;

    private TranslateIt2Error errorCode;
    
    public CustomErrorType(String errorMessage) {
        this.errorMessages = Arrays.asList(errorMessage);
        this.errorCode = TranslateIt2Error.UNDEFINED_ERROR;
    }
    
    public CustomErrorType(String localizedErrorMessage, List<String> errorMessages) {
        this.localizedErrorMessage = localizedErrorMessage;
        this.errorMessages = errorMessages;
        this.errorCode = TranslateIt2Error.UNDEFINED_ERROR;
    }
   
    public CustomErrorType(String errorMessage, TranslateIt2Error errorCode) {
        this.errorMessages = Arrays.asList(errorMessage);
        this.errorCode = errorCode;
    }
    
    public CustomErrorType(List<String> errorMessages, TranslateIt2Error errorCode) {
        this.errorMessages = errorMessages;
        this.errorCode = errorCode;
    }

    public CustomErrorType(String localizedErrorMessage, String errorMessage, TranslateIt2Error errorCode) {
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

    public TranslateIt2Error getErrorCode() {
        return errorCode;
    }
}
