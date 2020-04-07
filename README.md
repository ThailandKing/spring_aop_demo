---
title: Spring AOP的简单应用
date: 2019-12-28 21:37:18
tags: [spring]
---

## 一、SpringAOP介绍

- SpringAOP是Spring包含的一大特性，俗称面向切面编程，实现定制化功能和开发
- 切入点：在哪些类、哪些方法上切入
- 通知：在方法执行的什么时机做什么
- 切面：切入点+通知
- 织入：把切面加入到对象，并创建出代理对象的过程 
- 本篇文章记录一下，AOP思想在实际项目开发过程中的简单实践

```xml
<!--aop-->
<dependency>
    <groupId>org.springframework.boot</groupId>
    <artifactId>spring-boot-starter-aop</artifactId>
</dependency>
```

## 二、注解式参数校验

- 前后端分离项目中，后端需要校验前端传入参数，每次都判断NULL、判断空，显得格外麻烦
- 借助AOP和全局异常处理，就会很好的完成参数校验

### 1、实体类注解

```java
@Data
public class User {
    @NotNull(message = "主键id不能为NULL")
    private Integer id;
    @NotBlank(message = "用户名不能为空")
    private String name;
    private String pass;
}
```

### 2、参数注解

```java
@GetMapping(value = "/get")
public Response queryUserGet(@Valid User user, BindingResult result) {
    return Response.ok().data(user.getName() + ":" + user.getPass());
}

@PostMapping(value = "/post")
public Response queryUserPost(@Valid @RequestBody User user, BindingResult result) {
    return Response.ok().data(user.getName() + ":" + user.getPass());
}
```

### 3、切面配置

```java
@Aspect
@Component
public class WebParamAspect {
    private Logger logger = LoggerFactory.getLogger(WebParamAspect.class);

    // 配置扫描路径为controller包下所有
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
```

## 三、接口调用日志记录

- 当第三方调用我们提供的接口时，我们需要将调用信息日志做保存，以便后续的数据统计和问题排查
- 借助AOP和自定义注解，就会很好的实现接口调用日志的记录

### 1、声明日志实体类

```java
@Data
public class SystemCallLog {
    // 注解内容
    private String value;
    // 方法名
    private String methodName;
    // 参数列表 json
    private String params;
    // 用户名
    private String userName;
    // 时间
    private String callTime;
}
```

### 2、声明注解

```java
@Target(ElementType.METHOD)
@Retention(RetentionPolicy.RUNTIME)
@Documented
public @interface MyLog {
    String value() default "";
}
```

### 3、添加注解

```java
@MyLog(value = "测试get方法日志记录")
@GetMapping(value = "/get")
public Response myLogGet(User user, HttpServletRequest request) {
    return Response.ok().data("测试get方法日志记录");
}

@MyLog(value = "测试post方法日志记录")
@PostMapping(value = "/post")
public Response myLogPost(@RequestBody User user, HttpServletResponse response) {
    return Response.ok().data("测试post方法日志记录");
}
```

### 4、切面配置

```java
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
```

## 源码链接

- https://github.com/ThailandKing/spring_aop_demo.git

## 扩展阅读

> 拦截器和过滤器的区别？

- 使用范围不同：Filter是基于Servlet规范规定的，只能用于web程序中；Interceptor是基于Spring框架实现的
- 使用资源不同：Interceptor能使用Spring IOC里的任何资源、对象；Filter不能
- 深度不同：Filter只在Servlet前后起作用；Interceptor可在方法前后、异常前后弹性使用
- 所以在Spring框架程序中，优先使用Interceptor

- [Spring面向切面编程](https://www.jianshu.com/p/994027425b44)

