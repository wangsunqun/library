package com.wsq.library.gateway.starter.common;

import lombok.Data;

@Data
public class Result<T> {
    private Integer status;
    private String message;
    private T body;
}
