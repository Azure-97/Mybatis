package com.kuang.pojo;

import org.apache.ibatis.type.Alias;

/**
 * @author 84642
 * @version 1.0
 * @description: 实体类
 * @date 2023/7/27 20:29
 */
public class User {
    private String id;
    private String name;
    private String pwd;
    public User() {
    }
    public User(String id, String name, String password) {
        this.id = id;
        this.name = name;
        this.pwd = password;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getPwd() {
        return pwd;
    }

    public void setPwd(String pwd) {
        this.pwd = pwd;
    }

    @Override
    public String toString() {
        return "User{" +
                "id='" + id + '\'' +
                ", name='" + name + '\'' +
                ", pwd='" + pwd + '\'' +
                '}';
    }
}
