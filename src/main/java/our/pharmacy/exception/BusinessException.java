package our.pharmacy.exception;

import org.springframework.http.HttpStatus;
import org.springframework.http.HttpStatusCode;

public class BusinessException extends RuntimeException{
    private final HttpStatus code;

    public BusinessException(HttpStatus code) {
        this.code = code;
    }

    public BusinessException(HttpStatus code, Throwable cause) {
        super(cause);
        this.code = code;
    }

    public BusinessException(HttpStatus code, String message) {
        super(message);
        this.code = code;
    }
    public BusinessException(HttpStatus code, String message, Throwable cause) {
        super(message, cause);
        this.code = code;
    }

    public HttpStatusCode getCode() {
        return code;
    }
}
