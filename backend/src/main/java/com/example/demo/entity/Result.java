package com.example.demo.entity;

import java.util.HashMap;

public class Result extends HashMap<String, Object> {
    public Result(Integer code, String msg, Object data){
        this.put("code", code);
        this.put("msg", msg);
        this.put("data", data);
    }
    public static Result success(Object data){
        return new Result(200, "success", data);
    }

    public static Result success(String msg, Object data){
        return new Result(200, msg != null ? msg : "success", data);
    }
    public static Result success(){
        return new Result(200, "success",null);
    }
    public static Result error(String msg){
        return new Result(500, msg, null);
    }
}
