package com.kuang.dao;

import com.kuang.pojo.Blog;

import java.util.Hashtable;
import java.util.List;
import java.util.Map;

/**
 * @author 84642
 * @version 1.0
 * @description: TODO
 * @date 2023/8/3 23:54
 */
public interface BlogMapper {
    int addBlog(Blog blog);
    List<Blog> qureyBlog(Map map);
    List<Blog> qureyBlogChoose(Map map);
    List<Blog> qureyBlogWhere(Map map);
    int qureyBlogSet();
    List<Blog> qureyBlogInclud(Map map);
    List<Blog> qureyBlogForeach(Map map);
}
