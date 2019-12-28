package com.it.shw.aop.controller;

import com.it.shw.aop.dto.Response;
import com.it.shw.aop.entity.User;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;

/**
 * @Copyright: Shanghai Definesys Company.All rights reserved.
 * @Description:
 * @author: thailandking
 * @since: 2019/12/26 16:11
 * @history: 1.2019/12/26 created by thailandking
 */
@RestController
@RequestMapping(value = "/user")
public class UserController {

    @GetMapping(value = "/get")
    public Response queryUserGet(@Valid User user, BindingResult result) {
        return Response.ok().data(user.getName() + ":" + user.getPass());
    }

    @PostMapping(value = "/post")
    public Response queryUserPost(@Valid @RequestBody User user, BindingResult result) {
        return Response.ok().data(user.getName() + ":" + user.getPass());
    }
}
