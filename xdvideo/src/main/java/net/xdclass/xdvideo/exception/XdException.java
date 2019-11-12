package net.xdclass.xdvideo.exception;

import jdk.nashorn.internal.objects.annotations.Getter;
import jdk.nashorn.internal.objects.annotations.Setter;

/**
 * 自定义异常类,全局异常处理
 */

public class XdException extends RuntimeException {
    private  Integer code;
    private String msg;

    public XdException() {
    }

    public XdException(Integer code, String msg) {
        super(msg);
        this.code = code;
    }

    public Integer getCode() {
        return code;
    }

    public void setCode(Integer code) {
        this.code = code;
    }

    public String getMsg() {
        return msg;
    }

    public void setMsg(String msg) {
        this.msg = msg;
    }
}
