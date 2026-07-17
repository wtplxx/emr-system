package com.erm.backend.exception;

import com.erm.backend.VO.ResultVO;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(RuntimeException.class)
    public ResultVO handleRuntimeException(RuntimeException e) {
        ResultVO result = new ResultVO();
        result.setCode(-99);
        result.setData(e.getMessage());
        return result;
    }

    @ExceptionHandler(Exception.class)
    public ResultVO handleException(Exception e) {
        ResultVO result = new ResultVO();
        result.setCode(-99);
        result.setData("系统错误：" + e.getMessage());
        return result;
    }
}
