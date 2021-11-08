package com.msd.ledgerservice.util;

import org.springframework.http.HttpStatus;

public class DynamicErrorCodes {

    private final int id;
    private final String msg;
    private final HttpStatus httpCode;

    public DynamicErrorCodes(int id, String msg, HttpStatus httpCode) {
        this.id = id;
        this.msg = msg;
        this.httpCode = httpCode;
    }

    public int getId() {
        return id;
    }

    public String getMsg() {
        return msg;
    }

    public HttpStatus getHttpCode() {
        return httpCode;
    }
}
