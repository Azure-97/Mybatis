package com.kuang.pojo;

import org.apache.ibatis.type.Alias;

/**
 * @author 84642
 * @version 1.0
 * @description: 实体类
 * @date 2023/7/27 20:29
 */
@Alias("user")
public class User {
    private String id;
    private String name;
    private String password;
    public User() {
    }
    public User(String id, String name, String password) {
        this.id = id;
        this.name = name;
        this.password = password;
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

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    @Override
    public String toString() {
        return "User{" +
                "id='" + id + '\'' +
                ", name='" + name + '\'' +
                ", password='" + password + '\'' +
                '}';
    }
}
