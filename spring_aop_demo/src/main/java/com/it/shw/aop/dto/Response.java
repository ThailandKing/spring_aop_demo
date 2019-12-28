package com.it.shw.aop.dto;

import lombok.Data;

import java.io.Serializable;
import java.util.UUID;

/**
 *@Author thailandking
 *@Date 2019/12/1 11:58
 *@LastEditors thailandking
 *@LastEditTime 2019/12/1 11:58
 *@Description 统一返回对象
 */
@Data
public class Response implements Serializable {
    private String code;
    private String message;
    private Object data;
    private String requestId;

    public Response(String code, String message) {
        this.code = "ok";
        this.code = code;
        this.message = message;
        this.requestId = UUID.randomUUID().toString().replaceAll("-", "");
    }

    public static Response error(String message) {
        return new Response("error", message);
    }

    public static Response ok(){
        return new Response("ok",null);
    }

    public Response data(Object data) {
        this.data = data;
        return this;
    }
}