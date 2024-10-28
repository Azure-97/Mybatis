package com.kuang.dao;

import com.kuang.pojo.Student;
import org.apache.ibatis.annotations.Param;
import java.util.List;

public interface StudentMapper {
    List<Student> getStudent(@Param("id") int id);
    List<Student> getStudent2(@Param("id") int id);
}
