package com.kuang.dao;

import com.kuang.pojo.Student;
import com.kuang.pojo.Teacher;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * @author 84642
 * @version 1.0
 * @description: TODO
 * @date 2023/8/3 20:57
 */
public interface TeacherMapper {
    List<Teacher> getTeacher(@Param("tid")int id);
    List<Teacher> getTeacher2(@Param("tid")int id);
}
