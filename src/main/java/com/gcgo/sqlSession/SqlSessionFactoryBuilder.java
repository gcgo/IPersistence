package com.gcgo.sqlSession;

import com.gcgo.config.XMLConfigBuilder;
import com.gcgo.pojo.Configuration;
import org.dom4j.DocumentException;

import java.beans.PropertyVetoException;
import java.io.InputStream;

public class SqlSessionFactoryBuilder {

    public SqlSessionFactory build(InputStream in) throws DocumentException, PropertyVetoException {
        //使用dom4j解析输入流，封装到configuration中
        XMLConfigBuilder xmlConfigBuilder = new XMLConfigBuilder();
        Configuration configuration = xmlConfigBuilder.parseConfig(in);
        //第二,创建sqlSessionFactory对象
        SqlSessionFactory defaultSqlSessionFactory = new DefaultSqlSessionFactory(configuration);

        return defaultSqlSessionFactory;
    }
}
