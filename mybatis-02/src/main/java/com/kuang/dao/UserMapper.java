package com.kuang.dao;

import com.kuang.pojo.User;

import java.util.List;
import java.util.Map;

public interface UserMapper {
    List<User> getUserList();
    List<User> getUser(String id);
    int addUser(User usr);
    int updateUser(User usr);
    int deleteUser(String id);
}
