package com.kuang.pojo;

import lombok.Data;

import java.io.Serializable;
import java.util.Date;

/**
 * @author 84642
 * @version 1.0
 * @description: TODO
 * @date 2023/8/2 20:52
 */
@Data
public class Blog implements Serializable {
    private String id;
    private String title;
    private String author;
    private Date createTime;//属性名和字段名不一致
    private int views;
}
