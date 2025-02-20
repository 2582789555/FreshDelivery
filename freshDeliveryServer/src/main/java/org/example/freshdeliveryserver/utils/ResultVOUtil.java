package org.example.freshdeliveryserver.utils;

import org.example.freshdeliveryserver.vo.Result;

public class ResultVOUtil {
    public static<T> Result success(T data){
        Result<T> result = new Result<>();
        result.setCode("200");
        result.setMessage("成功！");
        result.setData(data);
        return result;
    }

    public static<T> Result error(String msg){
        Result<T> result = new Result<>();
        result.setCode("500");
        result.setMessage(msg);
        result.setData(null);
        return result;
    }
    public static<T> Result error(String code,String msg){
        Result<T> result = new Result<>();
        result.setCode(code);
        result.setMessage(msg);
        result.setData(null);
        return result;
    }
}
