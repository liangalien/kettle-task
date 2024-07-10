package com.liangalien.kt.util.response;

import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestControllerAdvice;


@Slf4j
@RestControllerAdvice
@ResponseBody
public class ResponseExceptionHandler {
    /**
     * 处理异常
     */
    @ExceptionHandler(Exception.class)
    public Response exception(Exception e) {
        log.error("请求异常", e);
        return Response.fail(e.getMessage());
    }
}
