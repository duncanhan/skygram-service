package com.skyteam.skygram.core;

import java.util.Arrays;
import java.util.Collections;

public class ResponseBuilder {
    private static final String DEFAULT_SUCCESS_MESSAGE = "success";

    public static Response buildSuccess() {
        return new Response()
                .setCode(ResponseCode.SUCCESS)
                .setMessage(DEFAULT_SUCCESS_MESSAGE);
    }

    public static <T> Response<T> buildSuccess(T data) {
        return new Response()
                .setCode(ResponseCode.SUCCESS)
                .setMessage(DEFAULT_SUCCESS_MESSAGE)
                .setData(data);
    }

    public static <T> Response<T> buildSuccess(String message, T data) {
        return new Response()
                .setCode(ResponseCode.SUCCESS)
                .setMessage(message)
                .setData(data);
    }

    public static Response buildFail(String message) {
        return new Response()
                .setCode(ResponseCode.BAD_REQUEST)
                .setMessage(message);
    }

    public static Response buildFail(ResponseCode code, String message) {
        return new Response()
                .setCode(code)
                .setMessage(message);
    }

    public static Response buildFail(ResponseCode code, String message, Object errors) {
        return new Response()
                .setCode(code)
                .setMessage(message)
                .setErrors(errors);
    }

    public static Response buildFail(ResponseCode code, String message, String error) {
        return new Response()
                .setCode(code)
                .setMessage(message)
                .setErrors(Collections.singletonList(error));
    }
}
