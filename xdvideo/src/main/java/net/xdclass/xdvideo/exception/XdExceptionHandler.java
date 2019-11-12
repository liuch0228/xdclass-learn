package net.xdclass.xdvideo.exception;

import net.xdclass.xdvideo.domain.JsonData;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;

/**
 * 异常处理控制器
 */
@ControllerAdvice
public class XdExceptionHandler {

    @ExceptionHandler(value =  Exception.class)
    @ResponseBody
    public JsonData handler(Exception e){
        if(e instanceof  XdException){
            XdException xdException = new XdException();
            return JsonData.buildError(xdException.getMsg(), xdException.getCode());
        } else {
            return JsonData.buildError("Unknow error");
        }
    }
}
