package com.it.shw.aop.exception;

import com.it.shw.aop.dto.Response;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
/**
 * @Copyright: Shanghai Definesys Company.All rights reserved.
 * @Description:
 * @author: thailandking
 * @since: 2019/12/26 16:47
 * @history: 1.2019/12/26 created by thailandking
 */
@RestControllerAdvice
public class ExceptionHandlers {

    @ExceptionHandler
    public Response paramValid(ParamException e) {
        return Response.error(e.getMessage());
    }
}
