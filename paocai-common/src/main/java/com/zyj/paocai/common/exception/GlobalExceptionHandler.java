package com.zyj.paocai.common.exception;

import com.zyj.paocai.common.utils.R;
import com.zyj.paocai.common.utils.RRException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

/**
 * 全局异常处理
 *
 * @author lulx
 * @date 2022-04-29 22:53
 **/
@Slf4j
@RestControllerAdvice(basePackages = {"com.zyj.paocai"})
public class GlobalExceptionHandler {

    /**
     * 处理自定义全局异常
     *
     * @param e
     * @return
     */
    @ExceptionHandler(value = RRException.class)
    public R RRExceptionHandler(RRException e) {
        log.error("发生业务异常，原因是:{}",e.getMsg());
        return R.error(e.getCode(), e.getMsg());
    }

    /**
     * 处理空指针的异常
     *
     * @param e
     * @return
     */
    @ExceptionHandler(value = NullPointerException.class)
    public R nullPointerExceptionHandler(NullPointerException e) {
        log.error("发生空指针异常，原因是:{}",e);
        return R.error(BizCodeEnum.BODY_NOT_MATCH.getCode(),BizCodeEnum.BODY_NOT_MATCH.getMsg());
    }

    /**
     * 处理其他异常
     * @param e
     * @return
     */
    @ExceptionHandler(value = Throwable.class)
    public R throwableHandler(Throwable e) {
        log.error("未知异常！原因是:{}", e);
        return R.error(BizCodeEnum.UNKNOWN_EXCEPTION.getCode(),BizCodeEnum.UNKNOWN_EXCEPTION.getMsg());
    }
}
