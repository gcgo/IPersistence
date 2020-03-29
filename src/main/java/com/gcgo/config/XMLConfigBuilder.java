package com.gcgo.config;

import com.gcgo.io.Resources;
import com.gcgo.pojo.Configuration;
import com.mchange.v2.c3p0.ComboPooledDataSource;
import org.dom4j.Document;
import org.dom4j.DocumentException;
import org.dom4j.Element;
import org.dom4j.io.SAXReader;

import java.beans.PropertyVetoException;
import java.io.InputStream;
import java.util.List;
import java.util.Properties;

public class XMLConfigBuilder {

    private Configuration configuration;

    public XMLConfigBuilder() {
        this.configuration = new Configuration();
    }

    /**
     * 将配置文件解析，封装成configuration
     * 使用dom4j
     */
    public Configuration parseConfig(InputStream in) throws DocumentException, PropertyVetoException {
        Document document = new SAXReader().read(in);
        //拿到sqlMapConfig.xml中的根标签，<configuration>
        Element rootElement = document.getRootElement();
        //获取所有property标签
        List<Element> list = rootElement.selectNodes("//property");
        //新建一个properties对象用于保存标签属性信息
        Properties properties = new Properties();
        //遍历property标签，封装到properties中
        for (Element element : list) {
            String name = element.attributeValue("name");
            String value = element.attributeValue("value");
            properties.setProperty(name, value);
        }

        /*至此properties中包含了数据库连接的所有信息
         * 下面可以创建数据库连接了,使用c3p0连接池*/
        ComboPooledDataSource comboPooledDataSource = new ComboPooledDataSource();
        comboPooledDataSource.setDriverClass(properties.getProperty("driverClass"));
        comboPooledDataSource.setJdbcUrl(properties.getProperty("jdbcUrl"));
        comboPooledDataSource.setUser(properties.getProperty("username"));
        comboPooledDataSource.setPassword(properties.getProperty("password"));

        //将数据库连接池封装到configuration中
        configuration.setDataSource(comboPooledDataSource);

        /*接着解析mapper.xml*/
        List<Element> mapperList = rootElement.selectNodes("//mapper");
        for (Element element : mapperList) {
            String mapperPath = element.attributeValue("resource");
            InputStream mapperStream = Resources.getResourceAsStream(mapperPath);
            XMLMapperBuilder xmlMapperBuilder = new XMLMapperBuilder(configuration);
            xmlMapperBuilder.parse(mapperStream);
        }
        return configuration;
    }

}
