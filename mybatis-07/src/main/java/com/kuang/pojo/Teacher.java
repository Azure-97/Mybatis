package com.kuang.pojo;

import lombok.Data;

import java.util.List;

/**
 * @author 84642
 * @version 1.0
 * @description: TODO
 * @date 2023/8/2 20:52
 */
@Data
    public class Teacher {
    private int id;
    private String name;
    private List<Student> students;
}
