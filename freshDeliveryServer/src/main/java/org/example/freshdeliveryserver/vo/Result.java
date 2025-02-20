package org.example.freshdeliveryserver.vo;

import lombok.Data;

@Data
public class Result<T> {
    private String code;
    private String message;
    private T data;
}
