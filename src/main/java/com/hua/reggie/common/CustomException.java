package com.hua.reggie.common;

/**
 * @author : hua
 * @date : 2023/2/28 23:19
 * 业务异常
 */
public class CustomException extends RuntimeException{
    public CustomException(String message){
        super(message);
    }
}
