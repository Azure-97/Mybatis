package dao;

import com.kuang.utils.Mybatisutil;
import com.kuang.dao.UserMapper;
import com.kuang.pojo.User;
import org.apache.ibatis.session.SqlSession;
import org.junit.Test;

import java.util.List;

/**
 * @author 84642
 * @version 1.0
 * @description: TODO
 * @date 2023/7/27 21:05
 */
public class test {
    @Test
    public void test(){
        SqlSession session= Mybatisutil.getSqlSession();
        UserMapper mapper=session.getMapper(UserMapper.class);
        List<User> userList=mapper.getUserList();
        for (User user:userList) {
            System.out.println(user.toString());
        }
        session.close();
    }

}
