package jpa.spring.config.exception;


import java.util.ArrayList;
import java.util.List;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

import jakarta.servlet.http.HttpServletRequest;
import jpa.spring.core.ErrorObject;
import jpa.spring.core.ResponseObject;

@SuppressWarnings({ "rawtypes", "unchecked" })
@ControllerAdvice
public class SpringExceptionHandler {

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<?> invalid(MethodArgumentNotValidException ex, HttpServletRequest request) {
        List<ErrorObject> errors = new ArrayList<>();
        ex.getAllErrors().forEach(err -> {
            ErrorObject error = new ErrorObject(ErrorObject.ErrorCode.DATA_INVALID,
                    err.getDefaultMessage());
            errors.add(error);
        });
        ResponseObject response = new ResponseObject();
        response.setMessage("Data is invalid.");
        response.setErrors(errors);
        return new ResponseEntity<ResponseObject>(response, HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(value = { UserAccountExistingException.class, UserAccountNotFoundException.class })
    public ResponseEntity<?> isExist(UserAccountExistingException ex, HttpServletRequest request) {
        ResponseObject response = new ResponseObject();
        response.setMessage("Data is invalid.");
        response.setError(new ErrorObject(ErrorObject.ErrorCode.ACCOUNT_EXIST, ex.getMessage()));
        return new ResponseEntity<ResponseObject>(response, HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(value = { UnknowException.class })
    public ResponseEntity<?> unknow(Exception ex, HttpServletRequest request) {
        ResponseObject response = new ResponseObject();
        response.setMessage("Something went wrong!");
        response.setError(new ErrorObject(ErrorObject.ErrorCode.UNKNOW, ex.getMessage()));
        return new ResponseEntity<ResponseObject>(response, HttpStatus.INTERNAL_SERVER_ERROR);
    }
}
