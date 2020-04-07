package com.it.shw.aop.config;

import java.lang.annotation.*;

/**
 * @Copyright: Shanghai Definesys Company.All rights reserved.
 * @Description:
 * @author: thailandking
 * @since: 2019/12/28 20:31
 * @history: 1.2019/12/28 created by thailandking
 */
@Target(ElementType.METHOD)
@Retention(RetentionPolicy.RUNTIME)
@Documented
public @interface MyLog {
    String value() default "";
}
