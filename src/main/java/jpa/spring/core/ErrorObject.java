package jpa.spring.core;

import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
public class ErrorObject {
    public enum ErrorCode {
        UNKNOW,
        DATA_INVALID, ACCOUNT_EXIST
    }

    private ErrorCode code;
    private String message;

    public ErrorObject(ErrorCode code, String message) {
        this.code = code;
        this.message = message;
    }
}
