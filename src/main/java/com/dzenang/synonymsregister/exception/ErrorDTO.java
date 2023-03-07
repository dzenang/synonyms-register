package com.dzenang.synonymsregister.exception;


import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.http.HttpStatus;

import java.io.Serializable;
import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class ErrorDTO implements Serializable {
    private HttpStatus status;
    private String message;
    List<String> errors;

    @JsonInclude(Include.NON_NULL)
    private Object metadata;
}
