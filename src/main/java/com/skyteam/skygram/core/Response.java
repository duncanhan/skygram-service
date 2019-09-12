package com.skyteam.skygram.core;

import com.fasterxml.jackson.annotation.JsonInclude;

@JsonInclude(JsonInclude.Include.NON_NULL)
public class Response<T> {

    private int code;
    private String message;
    private Object errors;
    private T data;

    public Response<T> setCode(ResponseCode resultCode) {
        this.code = resultCode.getCode();
        return this;
    }

    public int getCode() {
        return code;
    }

    public String getMessage() {
        return message;
    }

    public Response setMessage(String message) {
        this.message = message;
        return this;
    }

    public T getData() {
        return data;
    }

    public Response setData(T data) {
        this.data = data;
        return this;
    }

    public Object getErrors() {
        return errors;
    }

    public Response setErrors(Object errors) {
        this.errors = errors;
        return this;
    }
}
