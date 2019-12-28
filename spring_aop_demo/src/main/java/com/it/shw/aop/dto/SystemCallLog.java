package com.it.shw.aop.dto;

import lombok.Data;

import java.util.Date;

/**
 * @Copyright: Shanghai Definesys Company.All rights reserved.
 * @Description:
 * @author: thailandking
 * @since: 2019/12/28 20:44
 * @history: 1.2019/12/28 created by thailandking
 */
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
