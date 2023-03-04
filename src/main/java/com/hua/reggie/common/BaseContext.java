package com.hua.reggie.common;

/**
 * @author : hua
 * @date : 2023/2/28 14:49
 */
public class BaseContext {
    //基于ThreadLocal封装的工具类，用户保存和获取当前登录用户的id；
    private static ThreadLocal<Long> threadLocal =new ThreadLocal<>();

    public static void setCurrentId(Long id){
        threadLocal.set(id);
    }

    public static Long getCurrentId(){
        return threadLocal.get();
    }

}
