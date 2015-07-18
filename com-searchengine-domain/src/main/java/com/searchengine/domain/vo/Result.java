package com.searchengine.domain.vo;

import java.util.Date;

/**
 * 返回给客户端的实体定义
 * User: xuxp
 * Date: 14-11-7
 * Time: 下午7:38
 * To change this template use File | Settings | File Templates.
 */
public class Result<T> {
    private int code;        //请求结果编码
    private String msg;        //请求结果文字说明
    private Date timestamp;    //服务器时间戳
    T data;                    //请求结果数据

    public Result() {

    }

    public Result(int code, String msg) {
        this.code = code;
        this.msg = msg;
    }

    public Result(int code, String msg, Date timestamp) {
        this.code = code;
        this.msg = msg;
        this.timestamp = timestamp;
    }

    public int getCode() {
        return code;
    }

    public void setCode(int code) {
        this.code = code;
    }

    public String getMsg() {
        return msg;
    }

    public void setMsg(String msg) {
        this.msg = msg;
    }

    public Date getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(Date timestamp) {
        this.timestamp = timestamp;
    }

    public T getData() {
        return data;
    }

    public void setData(T data) {
        this.data = data;
    }
}
