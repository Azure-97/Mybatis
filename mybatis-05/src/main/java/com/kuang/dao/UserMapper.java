package com.kuang.dao;

import com.kuang.pojo.User;
import org.apache.ibatis.annotations.Delete;
import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public interface UserMapper {
    @Select("select * from user")
    List<User> getUserList();

    /**
     *当添加注解后（@Param("userId") int id），也就是说外部想要取出传入的id值，只需要取它的参数名userId就可以了。
     *不使用@Param注解时，参数只能有一个，并且是Javabean。必须使用的是#{}来取参数。使用${}方式取值会报错。
     *@Param注解来声明参数的时候，SQL语句取值使用#{}，${}取值都可以。
     * @param id
     * @return
     */
    @Select("select * from user where id = #{userId}")
    List<User> getUserByid(@Param("userId") int id);

    @Insert("insert into user(`id`,`name`,`password`) value(#{id},#{name},#{password})")
    int addUser(User user);

    @Delete("delete from user where id=#{uid}")
    int deleteUser(@Param("uid") String id);
}
