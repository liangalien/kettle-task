package com.liangalien.kt.util.response;

import lombok.AccessLevel;
import lombok.Data;
import lombok.Getter;
import org.springframework.http.HttpStatus;

import java.util.List;


@Data
public class Response<T> {
    private Object body;
    private ResponseResult success;
    private String message;

    @Getter(AccessLevel.NONE)
    private List<Object> rows;

    @Getter(AccessLevel.NONE)
    private Integer total;

    @Getter(AccessLevel.NONE)
    private HttpStatus statusCode;

    @Getter(AccessLevel.NONE)
    private boolean nullBody;

    public Response() {
        this.success = ResponseResult.SUCCESS;
        this.message = "成功";
        this.statusCode = HttpStatus.OK;
        this.nullBody = false;
    }

    public Object getBody() {
        if (total != null && rows != null) {
            PagingBody body = new PagingBody();
            body.setRows(rows);
            body.setTotal(total);
            return body;
        }
        return body;
    }

    public boolean getSuccess() {
        return this.success.getValue();
    }

    public static Response success() {
        return new Response();
    }

    public static Response success(Object body) {
        Response r = new Response();
        r.setBody(body);
        return r;
    }

    public static Response success(Object body, String message) {
        Response r = new Response();
        r.setBody(body);
        r.setMessage(message);
        return r;
    }

    public static Response success(List<Object> rows, int total) {
        Response r = new Response();
        r.setRows(rows);
        r.setTotal(total);
        return r;
    }

    public static Response success(List<Object> rows, int total, String message) {
        Response r = new Response();
        r.setRows(rows);
        r.setTotal(total);
        r.setMessage(message);
        return r;
    }

    public static Response fail(String message) {
        Response r = new Response();
        r.setSuccess(ResponseResult.FAIL);
        r.setMessage(message);
        return r;
    }

    public static Response fail(Object body, String message) {
        Response r = new Response();
        r.setSuccess(ResponseResult.FAIL);
        r.setBody(body);
        r.setMessage(message);
        return r;
    }

    public static Response status(HttpStatus status) {
        Response r = new Response();
        r.setStatusCode(status);
        r.setNullBody(true);
        return r;
    }
}
