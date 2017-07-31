package translateit2.web;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import translateit2.restapi.CustomErrorType;
import translateit2.util.Messages;

@RestControllerAdvice
public class RestErrorHandler {

    @Autowired
    Messages messages;

    @Autowired
    public RestErrorHandler(Messages messages) {
        this.messages = messages;
    }

    @ExceptionHandler(MethodArgumentNotValidException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    @ResponseBody
    public CustomErrorType processValidationError(MethodArgumentNotValidException ex) {
        BindingResult result = ex.getBindingResult();
        List<FieldError> fieldErrors = result.getFieldErrors();
        String errorMessage = "";
        for (FieldError error : fieldErrors) {
            errorMessage += error.getDefaultMessage() + " ";
        }

        CustomErrorType validationError = new CustomErrorType(errorMessage);

        return validationError;

    }
}
