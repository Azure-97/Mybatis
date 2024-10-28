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
    public void getUserLimitList(){
        SqlSession session= Mybatisutil.getSqlSession();
        UserMapper mapper=session.getMapper(UserMapper.class);
        HashMap<String, Integer> stringObjectHashMap = new HashMap<>();
        stringObjectHashMap.put("startIndex",0);
        stringObjectHashMap.put("pageSize",2);
        List<User> userList=mapper.getUserLimitList(stringObjectHashMap);
        for (User user : userList) {
            logger.info(user);
        }
        session.close();
    }
    @Test
    public void getUserRowBoundsList(){
        SqlSession sqlSession = Mybatisutil.getSqlSession();
        RowBounds rowBounds=new RowBounds(0,2);
        List<User> userList=sqlSession.selectList("com.kuang.dao.UserMapper.getUserRowBoundsList",null,rowBounds);
        for (User user : userList) {
            logger.info(user);
        }
        sqlSession.close();
    }
    @Test
    public void testLog4j(){
        logger.info("info");
        logger.debug("debug");
        logger.error("error");
    }

}
