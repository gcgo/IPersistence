package com.gcgo.config;

import com.gcgo.pojo.Configuration;
import com.gcgo.pojo.MappedStatement;
import org.dom4j.Document;
import org.dom4j.DocumentException;
import org.dom4j.Element;
import org.dom4j.io.SAXReader;

import java.io.InputStream;
import java.util.List;

public class XMLMapperBuilder {
    private Configuration configuration;

    public XMLMapperBuilder(Configuration configuration) {
        this.configuration = configuration;
    }

    public void parse(InputStream in) throws DocumentException {
        Document document = new SAXReader().read(in);
        Element rootElement = document.getRootElement();
        //获取mapper的名称
        String namespace = rootElement.attributeValue("namespace");
        //获取所有select标签
        List<Element> selectNodes = rootElement.selectNodes("//select");
        scanTags(selectNodes, namespace);

        //获取所有insert标签
        List<Element> insertNodes = rootElement.selectNodes("//insert");
        scanTags(insertNodes, namespace);

        //获取所有insert标签
        List<Element> deleteNodes = rootElement.selectNodes("//delete");
        scanTags(deleteNodes, namespace);

        //获取所有update标签
        List<Element> updateNodes = rootElement.selectNodes("//update");
        scanTags(updateNodes, namespace);
    }

    private void scanTags(List<Element> selectNodes, String namespace) {
        //用于组装key
        StringBuilder sb_key = new StringBuilder();

        for (Element element : selectNodes) {
            String id = element.attributeValue("id");
            String parameterType = element.attributeValue("parameterType");
            String resultType = element.attributeValue("resultType");
            String sql = element.getTextTrim();
            //封装到mappedStatement中
            MappedStatement mappedStatement = new MappedStatement();
            mappedStatement.setId(id);
            mappedStatement.setParameterType(parameterType);
            mappedStatement.setResultType(resultType);
            mappedStatement.setSql(sql);
            //key为mapper的namespace+id
            sb_key.append(namespace).append(".").append(id);
            //将mappedStatement存入configuration的map里
            configuration.getMappedStatementMap().put(sb_key.toString(), mappedStatement);
            sb_key.delete(0, sb_key.length());
        }
    }
}
