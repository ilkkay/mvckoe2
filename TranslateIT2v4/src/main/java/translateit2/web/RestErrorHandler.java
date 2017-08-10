package translateit2.web;

import java.io.IOException;
import java.util.List;

import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import translateit2.fileloader.FileLoaderException;
import translateit2.fileloader.contextexceptions.FileNotFoundException;
import translateit2.restapi.CustomErrorType;
import translateit2.util.Messages;

@RestControllerAdvice
public class RestErrorHandler {

    @Autowired
    Messages messages;

    @ExceptionHandler(MethodArgumentNotValidException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    @ResponseBody
    public CustomErrorType processValidationError(MethodArgumentNotValidException ex) {
        BindingResult result = ex.getBindingResult();
        List<FieldError> fieldErrors = result.getFieldErrors();
        
        StringBuilder errorMessages = new StringBuilder("");
        for (FieldError error : fieldErrors) 
            errorMessages.append(error.getField()).append(": ")
                .append(error.getDefaultMessage()).append(". ");
        
        CustomErrorType validationError = new CustomErrorType(errorMessages.toString());

        return validationError;

    }
    
    // https://www.javacodegeeks.com/2016/01/exception-handling-spring-restful-web-service.html
    @ExceptionHandler(FileNotFoundException.class)
    @ResponseBody
    public CustomErrorType handleFileNotFoundException(HttpServletRequest request, Exception ex){
        
        CustomErrorType errorMsg = new CustomErrorType("Error message");        
        return errorMsg ;
    }
    
    @ExceptionHandler(FileLoaderException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    @ResponseBody
    public CustomErrorType handleLoadingExceptions(FileLoaderException ex) {
        
        StringBuilder errorMessages = new StringBuilder("test message");
        
        switch (ex.getErrorCode()) {
        case CANNOT_CREATE_PERMANENT_DIRECTORY: // must contact admin
            break;
        case CANNOT_CREATE_UPLOAD_DIRECTORY:    // must contact admin
            break;
        case CANNOT_MOVE_FILE:                  // must contact admin
            break;
        case CANNOT_READ_APPLICATION_NAME_FROM_FILE_NAME:   // check the file in use
            break;
        case CANNOT_READ_FILE:                              // check the file in use
            break;
        case CANNOT_READ_LANGUAGE_FROM_FILE_NAME:           // check the file in use
            break;
        case CANNOT_UPLOAD_FILE:                            // must contact admin
            break;
        case FILE_NOT_FOUND:                                // must contact admin
            break;
        case FILE_TOBELOADED_IS_EMPTY:                      // check the file in use
            break;
        case IMPROPER_CHARACTERSET_IN_FILE:                 // check the file in use
            break;
        case IMPROPER_EXTENSION_IN_FILE_NAME:               // check the file in use
            break;
        case UNDEFINED_ERROR:                               // must contact admin
            break;
        default:
            break;
        }
                
        CustomErrorType customError = new CustomErrorType(errorMessages.toString(),
               ex.getErrorCode());

        return customError;
    }
    
    // all the other exceptions
    @ExceptionHandler(RuntimeException.class)
    @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
    @ResponseBody
    public CustomErrorType handleOtherExceptions(HttpServletRequest request, Exception ex){
        CustomErrorType errorMsg = new CustomErrorType("Error message");        
        return errorMsg ;
    }
}
