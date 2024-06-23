package jpa.spring.core;

import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
public class ErrorObject {
    public enum FurnitureErrorCode {
        UNKNOW,
        DATA_INVALID, ACCOUNT_EXIST
    }

    private String code;
    private String message;

    public ErrorObject(String code, String message) {
        this.code = code;
        this.message = message;
    }
}
