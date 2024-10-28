package com.kuang.utils;
import org.apache.ibatis.io.Resources;
import org.apache.ibatis.session.SqlSession;
import org.apache.ibatis.session.SqlSessionFactory;
import org.apache.ibatis.session.SqlSessionFactoryBuilder;

import java.io.IOException;
import java.io.InputStream;

/**
 * @author 84642
 * @version 1.0
 * @description: TODO
 * @date 2023/7/27 20:03
 */
public class Mybatisutil {
    private static SqlSessionFactory sqlSessionFactory;

    /**
     * 从 XML 中构建 SqlSessionFactory
     */
    static {
        try {
            String resource = "mybatis-config.xml";
            InputStream inputStream = Resources.getResourceAsStream(resource);
            sqlSessionFactory = new SqlSessionFactoryBuilder().build(inputStream);
        }catch (IOException e){
            e.printStackTrace();
        }

    }

    /**
     * 从 SqlSessionFactory 中获取 SqlSession
     * sqlSessionFactory.openSession(true)自动提交事务
     * @return SqlSession
     */
    public static SqlSession getSqlSession(){
        return sqlSessionFactory.openSession(true);
    }
}
