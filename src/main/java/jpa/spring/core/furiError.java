package jpa.spring.core;

import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
public class furiError {
    public enum FurnitureErrorCode {
        UNKNOW,
        DATA_INVALID, ACCOUNT_EXIST
    }

    private String code;
    private String message;

    public furiError(String code, String message) {
        this.code = code;
        this.message = message;
    }
}
