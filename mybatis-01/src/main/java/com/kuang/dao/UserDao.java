package com.kuang.dao;

import com.kuang.pojo.User;

import java.util.List;
import java.util.Map;

public interface UserDao {
    List<User> getUserList();
    List<User> getUserLike(String name);
    List<User> getUser(String id);
    int addUser(User usr);
    int addMapUser(Map map);
    int updateUser(User usr);
    int deleteUser(String id);
}
