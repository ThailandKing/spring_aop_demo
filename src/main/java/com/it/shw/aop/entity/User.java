package com.it.shw.aop.entity;

import lombok.Data;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

/**
 * @Copyright: Shanghai Definesys Company.All rights reserved.
 * @Description:
 * @author: thailandking
 * @since: 2019/12/26 16:10
 * @history: 1.2019/12/26 created by thailandking
 */
@Data
public class User {
    @NotNull(message = "主键id不能为NULL")
    private Integer id;
    @NotBlank(message = "用户名不能为空")
    private String name;
    private String pass;
}
