package com.it.shw.aop.controller;

import com.it.shw.aop.config.MyLog;
import com.it.shw.aop.dto.Response;
import com.it.shw.aop.entity.User;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * @Copyright: Shanghai Definesys Company.All rights reserved.
 * @Description:
 * @author: thailandking
 * @since: 2019/12/28 20:34
 * @history: 1.2019/12/28 created by thailandking
 */
@RestController
@RequestMapping(value = "/log")
public class LogController {

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
}
