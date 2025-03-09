package com.hometask.dkp.hsbctransactionmanagement.exception;

import com.hometask.dkp.hsbctransactionmanagement.controller.ResponseResult;
import lombok.extern.slf4j.Slf4j;
import org.springframework.core.annotation.Order;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;

/**
 * @author: dkp
 * @date: 2025-03-09
 */

@Slf4j
@Order(10000)
@RestControllerAdvice
public class GlobalBizExceptionHandler {
    /**
     * 全局异常处理
     */
    @ExceptionHandler(Exception.class)
    @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
    public ResponseResult handleGlobalException(Exception e) {
        log.error("Exception Info ex={}", e.getMessage(), e);
        return ResponseResult.fail(e.getLocalizedMessage());
    }
}
