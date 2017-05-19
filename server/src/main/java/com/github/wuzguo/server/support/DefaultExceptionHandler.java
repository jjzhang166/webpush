
package com.github.wuzguo.server.support;


import com.github.wuzguo.webpush.common.base.Result;
import com.github.wuzguo.webpush.common.base.ReturnMsg;
import com.github.wuzguo.webpush.common.exception.FrameException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;


/**
 * 例外处理。
 *
 * @author elvis.huang at 2017/03/28
 */
@ControllerAdvice
public class DefaultExceptionHandler {
    /**
     * 日志
     */
    protected static final Logger LOGGER = LoggerFactory.getLogger(DefaultExceptionHandler.class);


    @ExceptionHandler(RuntimeException.class)
    public Result onRuntimeExceptionOccuring(final RuntimeException exception) {
        LOGGER.debug("onRuntimeExceptionOccuring: " + exception.getMessage());

        Result result = new Result("1", System.currentTimeMillis() + "");
        result.setReturnMsg(new ReturnMsg("0001", "服务端发生异常"));
        return result;
    }

    @ExceptionHandler(Exception.class)
    public Result onExceptionOccuring(final Exception exception) {
        LOGGER.debug("onExceptionOccuring: " + exception.getMessage());

        Result result = new Result("1", System.currentTimeMillis() + "");
        result.setReturnMsg(new ReturnMsg("0001", "服务端发生异常"));
        return result;
    }

    @ExceptionHandler(FrameException.class)
    public Result onFrameExceptionOccuring(final FrameException exception) {
        LOGGER.debug("onFrameExceptionOccuring: " + exception.getMessage());

        Result result = new Result("1", System.currentTimeMillis() + "");
        result.setReturnMsg(new ReturnMsg("0001", "服务端发生异常"));
        return result;
    }
}
