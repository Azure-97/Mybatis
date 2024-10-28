import com.kuang.dao.TeacherMapper;
import com.kuang.pojo.Student;
import com.kuang.pojo.Teacher;
import com.kuang.utils.Mybatisutil;
import org.apache.ibatis.session.SqlSession;
import org.apache.log4j.Logger;
import org.junit.Test;
import java.util.List;


public class test {
    static Logger logger=Logger.getLogger(test.class);
    @Test
    public void test(){
        SqlSession session= Mybatisutil.getSqlSession();
        TeacherMapper mapper=session.getMapper(TeacherMapper.class);

        List<Teacher> students=mapper.getTeacher(1);
        for (Teacher student : students) {
            System.out.println(student);
        }

        session.close();
    }

    @Test
    public void test2(){
        SqlSession session= Mybatisutil.getSqlSession();
        TeacherMapper mapper=session.getMapper(TeacherMapper.class);

        List<Teacher> students=mapper.getTeacher2(1);
        for (Teacher student : students) {
            System.out.println(student);
        }

        session.close();
    }

}
