import com.kuang.dao.StudentMapper;

import com.kuang.pojo.Student;
import com.kuang.utils.Mybatisutil;
import lombok.val;
import org.apache.ibatis.session.SqlSession;
import org.apache.log4j.Logger;
import org.junit.Test;
import java.util.List;


public class test {
    static Logger logger=Logger.getLogger(test.class);
    @Test
    public void test(){
        SqlSession session= Mybatisutil.getSqlSession();
        StudentMapper mapper=session.getMapper(StudentMapper.class);
        List<Student> students=mapper.getStudent(1);
        for (Student student : students) {
            System.out.println(student);
        }
        session.close();
    }
    @Test
    public void test2(){
        SqlSession session= Mybatisutil.getSqlSession();
        StudentMapper mapper=session.getMapper(StudentMapper.class);
        List<Student> students=mapper.getStudent2(100);
        for (Student student : students) {
            System.out.println(student);
        }
        session.close();
    }

    @Test
    public void testLog4j(){
        logger.info("info");
        logger.debug("debug");
        logger.error("error");
    }

}
