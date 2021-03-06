package com.it.shw.aop.config;

import com.alibaba.fastjson.JSON;
import com.it.shw.aop.dto.SystemCallLog;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.AfterReturning;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;
import org.aspectj.lang.reflect.MethodSignature;
import org.springframework.stereotype.Component;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.lang.reflect.Method;
import java.text.SimpleDateFormat;
import java.util.Arrays;
import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;

/**
 * @Copyright: Shanghai Definesys Company.All rights reserved.
 * @Description:
 * @author: thailandking
 * @since: 2019/12/28 20:38
 * @history: 1.2019/12/28 created by thailandking
 */
@Aspect
@Component
public class LogInsertAspect {

    @Pointcut("@annotation(MyLog)")
    public void logInsert() {

    }

    @AfterReturning("logInsert()")
    public void doAfter(JoinPoint joinPoint) {
        SystemCallLog callLog = new SystemCallLog();
        // 从切面织入点处通过反射机制获取织入点处的方法
        MethodSignature signature = (MethodSignature) joinPoint.getSignature();
        Method method = signature.getMethod();
        // 1、获取注解内容
        MyLog myLog = method.getAnnotation(MyLog.class);
        if (myLog != null) {
            callLog.setValue(myLog.value());
        }
        // 2、获取方法名
        callLog.setMethodName(method.getName());
        // 3、获取参数列表
        Object[] args = joinPoint.getArgs();
        // 过滤HttpServletRequest和HttpServletResponse类型的参数
        if (args != null && args.length > 0) {
            List<Object> argList = Arrays.asList(args).stream()
                    .filter(arg -> (!(arg instanceof HttpServletRequest) && !(arg instanceof HttpServletResponse)))
                    .collect(Collectors.toList());
            String params = JSON.toJSONString(argList);
            callLog.setParams(params);
        }
        // 4、获取用户名
        ServletRequestAttributes requestAttributes = (ServletRequestAttributes) RequestContextHolder.getRequestAttributes();
        HttpServletRequest request = requestAttributes.getRequest();
        String userName= (String) request.getSession().getAttribute("userName");
        if(userName != null && !userName.isEmpty()){
            callLog.setUserName(userName);
        }else {
            callLog.setUserName("admin");
        }
        // 5、获取时间
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd hh:mm:ss");
        String callTime = sdf.format(new Date());
        callLog.setCallTime(callTime);
        // 插入到数据库
        System.out.println(callLog);
    }
}
