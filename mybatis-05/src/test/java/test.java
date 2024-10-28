import com.kuang.dao.UserMapper;
import com.kuang.pojo.User;
import com.kuang.utils.Mybatisutil;
import org.apache.ibatis.session.RowBounds;
import org.apache.ibatis.session.SqlSession;
import org.apache.log4j.Logger;
import org.junit.Test;

import java.util.HashMap;
import java.util.List;


public class test {
    static Logger logger=Logger.getLogger(test.class);
    @Test
    public void test(){
        SqlSession session= Mybatisutil.getSqlSession();
        UserMapper mapper=session.getMapper(UserMapper.class);
        List<User> userList=mapper.getUserList();
        for (User user:userList) {
            System.out.println(user);
        }
        session.close();
    }
    @Test
    public void getUserByid(){
        //注解查询
        SqlSession session= Mybatisutil.getSqlSession();
        UserMapper mapper=session.getMapper(UserMapper.class);
        List<User> user1=mapper.getUserByid(1);
        System.out.println(user1);
        session.close();
    }
    @Test
    public void addUser(){
        //注解添加User
        SqlSession session= Mybatisutil.getSqlSession();
        UserMapper mapper=session.getMapper(UserMapper.class);
        int a=mapper.addUser(new User("5","5","5"));
        System.out.println(a);
        session.close();
    }
    @Test
    public void deleteUser(){
        //注解删除User
        SqlSession session= Mybatisutil.getSqlSession();
        UserMapper mapper=session.getMapper(UserMapper.class);
        int a=mapper.deleteUser("6");
        System.out.println(a);
        session.close();
    }


}
