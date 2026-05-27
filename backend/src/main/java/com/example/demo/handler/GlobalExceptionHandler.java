package com.example.demo.handler;


import com.example.demo.entity.Result;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;

@ControllerAdvice
@ResponseBody
public class GlobalExceptionHandler {
    @ExceptionHandler(ArithmeticException.class)
    public Result error(ArithmeticException e){
        e.printStackTrace();
        return Result.error("算数异常");
    }

    @ExceptionHandler(RuntimeException.class)
    public Result error(RuntimeException e){
        e.printStackTrace();
        return Result.error( e.getMessage());
    }

}
