package team.jit.config.exception;

import lombok.Getter;
import org.springframework.validation.FieldError;

@Getter
public class ValidationError {
    private String field;
    private String errorMessage;
    private Object rejectedValue;

    public static ValidationError of(FieldError fieldError) {
        ValidationError error = new ValidationError();
        error.field = fieldError.getField();
        error.errorMessage = fieldError.getDefaultMessage();
        error.rejectedValue = fieldError.getRejectedValue();
        return error;
    }
}
