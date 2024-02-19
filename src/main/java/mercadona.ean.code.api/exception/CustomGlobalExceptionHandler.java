package mercadona.ean.code.api.exception;

import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.validation.FieldError;
import org.springframework.validation.ObjectError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

import java.time.LocalDateTime;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.NoSuchElementException;

@ControllerAdvice
public class CustomGlobalExceptionHandler extends ResponseEntityExceptionHandler {
    @Override
    protected ResponseEntity<Object> handleMethodArgumentNotValid(
            MethodArgumentNotValidException ex,
            HttpHeaders headers,
            HttpStatusCode status,
            WebRequest request
    ) {
        Map<String, Object> body = new LinkedHashMap<>();
        body.put("timestamp", LocalDateTime.now());
        body.put("status", HttpStatus.BAD_REQUEST);
        List<String> errorsList = ex.getBindingResult().getAllErrors().stream()
                .map(this::getErrorMessage)
                .toList();
        body.put("errors", errorsList);
        return new ResponseEntity<>(body, headers, status);
    }

    @ExceptionHandler(value = {RegistrationException.class })
    protected ResponseEntity<Object> registrationException(RuntimeException exc) {
        RegistrationException registrationException = (RegistrationException) exc;
        HttpStatusCode statusCode = HttpStatus.CONFLICT;
        Map<String, Object> body = new LinkedHashMap<>();
        body.put("timestamp", LocalDateTime.now());
        body.put("status", statusCode);
        body.put("errors", registrationException.getMessage());
        HttpHeaders headers = new HttpHeaders();
        return new ResponseEntity<>(body, headers, statusCode);
    }

    @ExceptionHandler(value = {UsernameNotFoundException.class })
    protected ResponseEntity<Object> usernameNotFoundException(RuntimeException exc) {
        UsernameNotFoundException usernameNotFoundException = (UsernameNotFoundException) exc;
        HttpStatusCode statusCode = HttpStatus.NOT_FOUND;
        Map<String, Object> body = new LinkedHashMap<>();
        body.put("timestamp", LocalDateTime.now());
        body.put("status", statusCode);
        body.put("errors", usernameNotFoundException.getMessage());
        HttpHeaders headers = new HttpHeaders();
        return new ResponseEntity<>(body, headers, statusCode);
    }

    @ExceptionHandler(value = {UnsupportedOperationException.class })
    protected ResponseEntity<Object> unsupportedOperationException(RuntimeException exc) {
        UnsupportedOperationException unsupportedOperationException =
                (UnsupportedOperationException) exc;
        HttpStatusCode statusCode = HttpStatus.NOT_ACCEPTABLE;
        Map<String, Object> body = new LinkedHashMap<>();
        body.put("timestamp", LocalDateTime.now());
        body.put("status", statusCode);
        body.put("errors", unsupportedOperationException.getMessage());
        HttpHeaders headers = new HttpHeaders();
        return new ResponseEntity<>(body, headers, statusCode);
    }

    @ExceptionHandler(value = {IllegalArgumentException.class })
    protected ResponseEntity<Object> IllegalArgumentException(RuntimeException exc) {
        IllegalArgumentException IllegalArgumentException =
                (IllegalArgumentException) exc;
        HttpStatusCode statusCode = HttpStatus.NOT_ACCEPTABLE;
        Map<String, Object> body = new LinkedHashMap<>();
        body.put("timestamp", LocalDateTime.now());
        body.put("status", statusCode);
        body.put("errors", IllegalArgumentException.getMessage());
        HttpHeaders headers = new HttpHeaders();
        return new ResponseEntity<>(body, headers, statusCode);
    }

    @ExceptionHandler(value = {NoSuchElementException.class })
    protected ResponseEntity<Object> NoSuchElementException(RuntimeException exc) {
        NoSuchElementException NoSuchElementException =
                (NoSuchElementException) exc;
        HttpStatusCode statusCode = HttpStatus.NOT_FOUND;
        Map<String, Object> body = new LinkedHashMap<>();
        body.put("timestamp", LocalDateTime.now());
        body.put("status", statusCode);
        body.put("errors", NoSuchElementException.getMessage());
        HttpHeaders headers = new HttpHeaders();
        return new ResponseEntity<>(body, headers, statusCode);
    }

    private String getErrorMessage(ObjectError e) {
        if (e instanceof FieldError) {
            String field = ((FieldError) e).getField();
            String defaultMessage = e.getDefaultMessage();
            return field + " " + defaultMessage;
        }
        return e.getDefaultMessage();
    }
}
