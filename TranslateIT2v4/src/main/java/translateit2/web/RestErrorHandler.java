package translateit2.web;

import static org.hamcrest.CoreMatchers.equalTo;
import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.assertThat;

import java.io.IOException;
import java.util.List;
import java.util.Locale;

import javax.servlet.http.HttpServletRequest;
import javax.validation.ConstraintViolation;
import javax.validation.ConstraintViolationException;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.context.properties.ConfigurationProperties;
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
@ConfigurationProperties(prefix = "translateit2.validator")
public class RestErrorHandler {

    @Autowired
    Messages messages;

 // Constraint violation exceptions
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

    // Constraint violation exceptions
    @ExceptionHandler(ConstraintViolationException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    @ResponseBody
    public CustomErrorType handleOtherExceptions(HttpServletRequest request,ConstraintViolationException ex){
        
        StringBuilder violationMsgs = new StringBuilder(""); 
        for (ConstraintViolation<?> violation : ex.getConstraintViolations()) {
            String key = "";
            if (violation.getPropertyPath() != null) {
                key = violation.getPropertyPath().toString();
            }
            violationMsgs.append("Invalid field is: " + key + " msg: " + violation.getMessage());
        }
        
        CustomErrorType errorMsg = new CustomErrorType(violationMsgs.toString());    
        
        return errorMsg ;
    }

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
                
        CustomErrorType customError = new CustomErrorType(
                messages.get(ex.getErrorCode().getDescription()),
                messages.get(ex.getErrorCode().getDescription(),Locale.ENGLISH),
                ex.getErrorCode());

        return customError;
    }

    // all the other exceptions
    @ExceptionHandler(RuntimeException.class)
    @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
    @ResponseBody
    public CustomErrorType handleOtherExceptions(HttpServletRequest request, Exception ex){
        CustomErrorType errorMsg = new CustomErrorType(ex.getMessage());        
        return errorMsg ;
    }
}
