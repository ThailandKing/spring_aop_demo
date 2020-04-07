package com.it.shw.aop.config;

import com.it.shw.aop.exception.ParamException;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.aspectj.lang.annotation.Pointcut;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;

/**
 * @Copyright: Shanghai Definesys Company.All rights reserved.
 * @Description:
 * @author: thailandking
 * @since: 2019/12/26 16:33
 * @history: 1.2019/12/26 created by thailandking
 */
@Aspect
@Component
public class WebParamAspect {
    private Logger logger = LoggerFactory.getLogger(WebParamAspect.class);

    @Pointcut("execution(* com.it.shw.aop.controller..*(..))")
    public void webParam() {

    }

    @Before("webParam() &&args(..,bindingResult)")
    public void doBefore(JoinPoint joinPoint, BindingResult bindingResult) {
        if (bindingResult.hasErrors()) {
            logger.error("param error.....");
            FieldError error = bindingResult.getFieldError();
            throw new ParamException(error.getDefaultMessage());
        }
    }
}
