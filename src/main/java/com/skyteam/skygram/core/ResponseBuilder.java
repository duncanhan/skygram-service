package com.skyteam.skygram.core;

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
                .setCode(ResponseCode.FAIL)
                .setMessage(message);
    }

    public static Response buildFail(ResponseCode code, String message) {
        return new Response()
                .setCode(code)
                .setMessage(message);
    }
}
