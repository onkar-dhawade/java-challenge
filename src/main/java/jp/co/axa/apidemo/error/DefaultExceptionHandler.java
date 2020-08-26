package jp.co.axa.apidemo.error;

import java.time.LocalDateTime;

import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.web.HttpMediaTypeNotSupportedException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

import lombok.Data;
import lombok.extern.slf4j.Slf4j;

/**
 * Controller Advice class which takes care of exception handling across
 * application
 */

@Slf4j
@ControllerAdvice
public class DefaultExceptionHandler extends ResponseEntityExceptionHandler {

    /**
     * This method will handle custom exception - EmployeeNotFound
     * 
     * @param employeeNotFoundException
     * @return ResponseEntity<ErrorMessage>
     */
    @ExceptionHandler(EmployeeNotFoundException.class)
    public final ResponseEntity<ErrorMessage> employeeNotFound(EmployeeNotFoundException employeeNotFoundException) {
        log.info("Inside employeeNotFound()");
        ErrorMessage errorMessage = new ErrorMessage(employeeNotFoundException.getMessage(), HttpStatus.NOT_FOUND);
        log.error(errorMessage.getErrorMessage());
        return new ResponseEntity<ErrorMessage>(errorMessage, errorMessage.getStatus());
    }

    /**
     * This method will handle JSON format exception
     * 
     * @param ex
     * @param headers
     * @param status
     * @param request
     * @return ResponseEntity<Object>
     */
    @Override
    protected ResponseEntity<Object> handleHttpMessageNotReadable(HttpMessageNotReadableException ex,
            HttpHeaders headers, HttpStatus status, WebRequest request) {
        log.info("Inside handleHttpMessageNotReadable()");
        ErrorMessage errorMessage = new ErrorMessage("JSON Parse exception. Details: ".concat(ex.getMessage()),
                HttpStatus.BAD_REQUEST);
        log.error(errorMessage.getErrorMessage(), ex);
        return new ResponseEntity<>(errorMessage, errorMessage.getStatus());
    }

    /**
     * This method will handle validation related exception
     * 
     * @param ex
     * @param headers
     * @param status
     * @param request
     * @return ResponseEntity<Object>
     */
    @Override
    protected ResponseEntity<Object> handleMethodArgumentNotValid(MethodArgumentNotValidException ex,
            HttpHeaders headers, HttpStatus status, WebRequest request) {
        log.info("Inside handleMethodArgumentNotValid()");
        String errMsg = ex.getBindingResult().getFieldError().getField().concat("- ")
                .concat(ex.getBindingResult().getFieldError().getDefaultMessage());
        ErrorMessage errorMessage = new ErrorMessage("Invalid Parameter value. Details: ".concat(errMsg),
                HttpStatus.NOT_ACCEPTABLE);
        log.error(errorMessage.getErrorMessage(), ex);
        return new ResponseEntity<>(errorMessage, errorMessage.getStatus());
    }

    /**
     * This method will handle MediaType not supported exception
     * 
     * @param ex
     * @param headers
     * @param status
     * @param request
     * @return ResponseEntity<Object>
     */
    @Override
    protected ResponseEntity<Object> handleHttpMediaTypeNotSupported(HttpMediaTypeNotSupportedException ex,
            HttpHeaders headers, HttpStatus status, WebRequest request) {
        log.info("Inside handleHttpMediaTypeNotSupported()");
        ErrorMessage errorMessage = new ErrorMessage("Invalid MediaTyape. Details: ".concat(ex.getMessage()),
                HttpStatus.NOT_ACCEPTABLE);
        log.error(errorMessage.getErrorMessage(), ex);
        return new ResponseEntity<>(errorMessage, errorMessage.getStatus());
    }

    /**
     * This method will catch all the exceptions other than Custom Exceptions,
     * MediaType not supported, JSON format exceptions
     * 
     * @param ex
     * @return ResponseEntity<ErrorMessage>
     */
    @ExceptionHandler(Exception.class)
    public final ResponseEntity<ErrorMessage> somethingWentWrong(Exception ex) {
        log.info("Inside somethingWentWrong()");
        ErrorMessage errorMessage = new ErrorMessage(ex.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
        log.error(errorMessage.getErrorMessage(), ex);
        return new ResponseEntity<ErrorMessage>(errorMessage, errorMessage.getStatus());
    }
}

/**
 * Custom Error Response class
 */
@Data
class ErrorMessage {
    // errorMessage to the end user
    private String errorMessage;
    // http status
    private HttpStatus status;
    // timestamp
    private LocalDateTime timestamp;

    private ErrorMessage() {
        this.timestamp = LocalDateTime.now();
    }

    ErrorMessage(String theErrorMessage, HttpStatus theStatus) {
        this();
        this.errorMessage = theErrorMessage;
        this.status = theStatus;
    }
}