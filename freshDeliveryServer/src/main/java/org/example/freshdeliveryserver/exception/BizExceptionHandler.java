package org.example.freshdeliveryserver.exception;

import org.example.freshdeliveryserver.utils.ResultVOUtil;
import org.example.freshdeliveryserver.vo.Result;
import org.springframework.mail.MailSendException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
public class BizExceptionHandler {
    @ExceptionHandler(value = {BizException.class})
    public Result handle(BizException e){
        return ResultVOUtil.error(e.getMessage());
    }


}