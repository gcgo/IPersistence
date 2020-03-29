package com.gcgo.test;

import com.gcgo.dao.IUserDao;
import com.gcgo.io.Resources;
import com.gcgo.pojo.User;
import com.gcgo.sqlSession.SqlSession;
import com.gcgo.sqlSession.SqlSessionFactory;
import com.gcgo.sqlSession.SqlSessionFactoryBuilder;
import org.dom4j.DocumentException;
import org.junit.Before;
import org.junit.Test;

import java.beans.PropertyVetoException;
import java.io.InputStream;
import java.util.List;

public class IPersistenceTest {
    private IUserDao userMapper;

    @Before
    public void before() throws PropertyVetoException, DocumentException {
        InputStream resourceAsStream = Resources.getResourceAsStream("sqlMapConfig.xml");
        SqlSessionFactory sqlSessionFactory = new SqlSessionFactoryBuilder().build(resourceAsStream);
        SqlSession sqlSession = sqlSessionFactory.openSession();
        userMapper = sqlSession.getMapper(IUserDao.class);
    }

    @Test
    public void test() throws Exception {

        List<User> users = userMapper.findAll();
        for (User user1 : users) {
            System.out.println(user1);
        }
    }

    @Test
    public void test2() throws Exception {

        User user = new User();
        user.setId(3);
        user.setUsername("麦迪");
        user.setBirthday("1990-11-11");
        user.setPassword("1234");

        userMapper.insert(user);
    }

    @Test
    public void test3() throws Exception {

        userMapper.delete(3);
    }

    @Test
    public void test4() throws Exception {

        User user = new User();
        user.setId(3);
        user.setUsername("科比");
        user.setBirthday("1996-11-11");
        user.setPassword("4321");

        userMapper.update(user);
    }
}
