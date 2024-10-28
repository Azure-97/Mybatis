package com.kuang.dao;

import com.kuang.pojo.User;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public interface UserMapper {
    List<User> getUserList();
    List<User> getUserLimitList(Map map);
    List<User>getUserRowBoundsList();
}
