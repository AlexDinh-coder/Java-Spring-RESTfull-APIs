package vn.tuantrung.jobhunter.util.error;

import java.util.List;
import java.util.stream.Collectors;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.servlet.resource.NoResourceFoundException;

import vn.tuantrung.jobhunter.domain.response.RestRespone;

@RestControllerAdvice
public class GlobalException {
    @ExceptionHandler(value = {

            UsernameNotFoundException.class,
            BadCredentialsException.class,
            IdInvalidException.class

    })
    public ResponseEntity<RestRespone<Object>> handleIdException(Exception ex) {
        RestRespone<Object> res = new RestRespone<Object>();
        res.setStatusCode(HttpStatus.BAD_REQUEST.value());
        res.setError(ex.getMessage());
        res.setMessage("Exception occurred");

        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(res);
    }

    @ExceptionHandler(value = { NoResourceFoundException.class,
    })
    public ResponseEntity<RestRespone<Object>> handleNotFoundException(Exception ex) {
        RestRespone<Object> respone = new RestRespone<Object>();
        respone.setStatusCode(HttpStatus.NOT_FOUND.value());
        respone.setError(ex.getMessage());
        respone.setMessage("404 Not Found. URL may be not existed ...");
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(respone);
    }

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<RestRespone<Object>> validationError(MethodArgumentNotValidException ex) {
        BindingResult result = ex.getBindingResult();
        final List<FieldError> fieldErrors = result.getFieldErrors();

        RestRespone<Object> res = new RestRespone<Object>();
        res.setStatusCode(HttpStatus.BAD_REQUEST.value());
        res.setError(ex.getBody().getDetail());

        List<String> errors = fieldErrors.stream().map(f -> f.getDefaultMessage()).collect(Collectors.toList());
        res.setMessage(errors.size() > 1 ? errors : errors.get(0));

        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(res);
    }

    @ExceptionHandler(value = {
            StorageException.class

    })
    public ResponseEntity<RestRespone<Object>> handleFileUploadException(Exception ex) {
        RestRespone<Object> res = new RestRespone<Object>();
        res.setStatusCode(HttpStatus.BAD_REQUEST.value());
        res.setError(ex.getMessage());
        res.setMessage("Exception upload file....");

        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(res);
    }

}
