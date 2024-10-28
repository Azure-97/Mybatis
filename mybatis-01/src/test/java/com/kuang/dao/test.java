package com.kuang.dao;

import com.kuang.Mybatisutil;
import com.kuang.pojo.User;
import org.apache.ibatis.session.SqlSession;
import org.junit.Test;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @author 84642
 * @version 1.0
 * @description: TODO
 * @date 2023/7/27 21:05
 */
public class test {
    @Test
    public void test(){
        //org.apache.ibatis.binding.BindingException: Type interface com.kuang.dao.UserDao is not known to the MapperRegistry.
        //MapperRegistry是什么
        //Could not find resource com/kuang/dao/UserMapper.xml
        SqlSession session=Mybatisutil.getSqlSession();
        UserDao mapper=session.getMapper(UserDao.class);
        //方式一
        List<User> userList=mapper.getUserList();
        for (User user:userList) {
            System.out.println(user.toString());
        }
        //方式二
        //List list=session.selectList("com.kuang.dao.UserDao.getUserList");
        session.close();
    }
    @Test
    public void getUser(){
        SqlSession sqlsession=Mybatisutil.getSqlSession();
        UserDao mapper=sqlsession.getMapper(UserDao.class);
        //方式一
        List<User> userList=mapper.getUser("1");
        for (User user:userList) {
            System.out.println(user.toString());
        }
        //方式二
        sqlsession.close();
    }
    @Test
    public void getUserLike(){
        SqlSession sqlsession=Mybatisutil.getSqlSession();
        UserDao mapper=sqlsession.getMapper(UserDao.class);
        //方式一
        List<User> userList=mapper.getUserLike("%1%");
        for (User user:userList) {
            System.out.println(user.toString());
        }
        //方式二
        sqlsession.close();
    }


    @Test
    public void addUser(){
        SqlSession sqlsession=Mybatisutil.getSqlSession();
        UserDao mapper=sqlsession.getMapper(UserDao.class);
        //方式一
        User user=new User("4","4","4");
        int a=mapper.addUser(user);
        List<User> userList=mapper.getUserList();
        for (User user0:userList) {
            System.out.println(user0.toString());
        }
        //!!增删改需要提交事务，否则不能存进数据库!!
        sqlsession.commit();
        sqlsession.close();
    }
    @Test
    public void addMapUser(){
        SqlSession sqlsession=Mybatisutil.getSqlSession();
        UserDao mapper=sqlsession.getMapper(UserDao.class);
        Map<String, Object> map = new HashMap<String, Object>();
        map.put("userid","6");
        map.put("username","6");
        map.put("userpwd","6");
        mapper.addMapUser(map);
        List<User> userList=mapper.getUserList();
        for (User user0:userList) {
            System.out.println(user0.toString());
        }
        //!!增删改需要提交事务，否则不能存进数据库!!
        sqlsession.commit();
        sqlsession.close();
    }
    @Test
    public void updateUser(){
        SqlSession sqlsession=Mybatisutil.getSqlSession();
        UserDao mapper=sqlsession.getMapper(UserDao.class);
        User user01=new User("4","43","3");
        mapper.updateUser(user01);
        List<User> userList=mapper.getUserList();
        for (User user0:userList) {
            System.out.println(user0.toString());
        }
        //!!增删改需要提交事务，否则不能存进数据库!!
        sqlsession.commit();
        sqlsession.close();
    }
    @Test
    public void deleteUser(){
        SqlSession sqlsession=Mybatisutil.getSqlSession();
        UserDao mapper=sqlsession.getMapper(UserDao.class);
        mapper.deleteUser("4");
        List<User> userList=mapper.getUserList();
        for (User user0:userList) {
            System.out.println(user0.toString());
        }
        //!!增删改需要提交事务，否则不能存进数据库!!
        sqlsession.commit();
        sqlsession.close();
    }
}
