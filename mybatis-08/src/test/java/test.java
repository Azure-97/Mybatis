import com.kuang.dao.BlogMapper;
import com.kuang.pojo.Blog;
import com.kuang.utils.IDutils;
import com.kuang.utils.Mybatisutil;
import lombok.val;
import org.apache.ibatis.session.SqlSession;
import org.apache.log4j.Logger;
import org.junit.Test;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.*;


public class test {
    static Logger logger=Logger.getLogger(test.class);
    @Test
    public void test(){
        SqlSession session= Mybatisutil.getSqlSession();
        BlogMapper mapper=session.getMapper(BlogMapper.class);
        Blog blog=new Blog();
        blog.setId(IDutils.getId());
        blog.setTitle("title");
        blog.setAuthor("wang");
        blog.setCreateTime(new Date());
        blog.setViews(1);
        int a=mapper.addBlog(blog);
        session.close();
    }
    @Test
    public void testQueryBlogIf(){
        SqlSession session= Mybatisutil.getSqlSession();
        BlogMapper mapper=session.getMapper(BlogMapper.class);
        Map map= new HashMap<>();
        map.put("title","title");
        List<Blog> list=mapper.qureyBlog(map);
        for (Blog blog : list) {
            System.out.println(blog);
        }
        session.close();
    }
    @Test
    public void testQueryBlogChoose(){
        SqlSession session= Mybatisutil.getSqlSession();
        BlogMapper mapper=session.getMapper(BlogMapper.class);
        Map map= new HashMap<>();
        List<Blog> list=mapper.qureyBlogChoose(map);
        for (Blog blog : list) {
            System.out.println(blog);
        }
        session.close();
    }

    @Test
    public void testQueryBlogWhere(){
        SqlSession session= Mybatisutil.getSqlSession();
        BlogMapper mapper=session.getMapper(BlogMapper.class);
        Map map= new HashMap<>();
        map.put("title","title");
        List<Blog> list=mapper.qureyBlogWhere(map);
        for (Blog blog : list) {
            System.out.println(blog);
        }
        session.close();
    }

    @Test
    public void testQueryBlogSet(){
        SqlSession session= Mybatisutil.getSqlSession();
        BlogMapper mapper=session.getMapper(BlogMapper.class);
        int list=   mapper.qureyBlogSet();

        session.close();
    }

    @Test
    public void testQueryBlogInclud(){
        SqlSession session= Mybatisutil.getSqlSession();
        BlogMapper mapper=session.getMapper(BlogMapper.class);
        Map map= new HashMap<>();
        map.put("title","title");
        List<Blog> list=mapper.qureyBlogInclud(map);
        for (Blog blog : list) {
            System.out.println(blog);
        }
        session.close();
    }

    @Test
    public void testQueryBlogForeach(){
        SqlSession session= Mybatisutil.getSqlSession();
        BlogMapper mapper=session.getMapper(BlogMapper.class);
        ArrayList<Integer> ids=new ArrayList<>();
        ids.add(1);
        ids.add(2);
        Map map= new HashMap<>();
        map.put("ids",ids);
        List<Blog> list=mapper.qureyBlogForeach(map);
        for (Blog blog : list) {
            System.out.println(blog);
        }
        session.close();
    }

    @Test
    public void testQueryBlogInclud2(){
        SqlSession session= Mybatisutil.getSqlSession();
        BlogMapper mapper=session.getMapper(BlogMapper.class);
        Map map= new HashMap<>();
        map.put("title","title");
        List<Blog> list=mapper.qureyBlogInclud(map);
        session.close();


        SqlSession session2= Mybatisutil.getSqlSession();
        BlogMapper mapper2=session2.getMapper(BlogMapper.class);
        Map map2= new HashMap<>();
        map2.put("title","title");
        List<Blog> list2=mapper2.qureyBlogInclud(map2);
        session2.close();



    }

}
