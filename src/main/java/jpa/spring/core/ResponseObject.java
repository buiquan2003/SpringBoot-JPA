package jpa.spring.core;

import java.util.List;

import com.fasterxml.jackson.annotation.JsonInclude;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@RequiredArgsConstructor
@JsonInclude(JsonInclude.Include.NON_NULL)
public class ResponseObject<T> {
  
    private String message;
    private List<ErrorObject> errors;
    private ErrorObject error;
    private T data;
    private List<T> daList;
}
