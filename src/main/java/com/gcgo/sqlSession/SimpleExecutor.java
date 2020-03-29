package com.gcgo.sqlSession;

import com.gcgo.pojo.Configuration;
import com.gcgo.pojo.MappedStatement;
import com.gcgo.utils.GenericTokenParser;
import com.gcgo.utils.ParameterMapping;
import com.gcgo.utils.ParameterMappingTokenHandler;

import java.beans.PropertyDescriptor;
import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class SimpleExecutor implements Executor {
    @Override
    public <E> List<E> query(Configuration configuration, MappedStatement mappedStatement, Object... params) throws Exception {
        //1获取数据连接
        Connection connection = configuration.getDataSource().getConnection();
        //2获取sql语句
        String sql = mappedStatement.getSql();//包含占位符#{}
        //2.5转换sql
        BoundSql boundSql = getBoundSql(sql);
        //3获取预处理对象：preparedStatement
        PreparedStatement preparedStatement = connection.prepareStatement(boundSql.getSqlText());
        //4设置参数
        //输入参数的类名
        String parameterType = mappedStatement.getParameterType();
        //得到Class对象
        Class<?> parameterClass = getClassType(parameterType);
        List<ParameterMapping> parameterMappingList = boundSql.getParameterMappingList();
        for (int i = 0; i < parameterMappingList.size(); i++) {
            ParameterMapping parameterMapping = parameterMappingList.get(i);
            String content = parameterMapping.getContent();//content就是属性名
            //反射
            Field declaredField = parameterClass.getDeclaredField(content);
            //暴力访问
            declaredField.setAccessible(true);
            //读取输入参数的字段，user的字段
            Object o = declaredField.get(params[0]);
            //设置sql的？对应的参数
            preparedStatement.setObject(i + 1, o);
        }
        //5执行sql
        ResultSet resultSet = preparedStatement.executeQuery();
        String resultType = mappedStatement.getResultType();
        Class<?> resultTypeClass = getClassType(resultType);

        List<Object> objects = new ArrayList<>();

        //6封装结果集
        while (resultSet.next()) {
            Object o = resultTypeClass.newInstance();
            //元数据
            ResultSetMetaData metaData = resultSet.getMetaData();
            for (int i = 1; i <= metaData.getColumnCount(); i++) {
                //字段名
                String columnName = metaData.getColumnName(i);
                //字段值
                Object value = resultSet.getObject(columnName);
                //使用反射封装结果集
                //内省库中的类
                PropertyDescriptor propertyDescriptor = new PropertyDescriptor(columnName, resultTypeClass);
                Method writeMethod = propertyDescriptor.getWriteMethod();
                writeMethod.invoke(o, value);
            }
            objects.add(o);
        }
        return (List<E>) objects;
    }

    @Override
    public void insert(Configuration configuration, MappedStatement mappedStatement, Object... params) throws Exception {
        //1获取数据连接
        Connection connection = configuration.getDataSource().getConnection();
        //2获取sql语句
        String sql = mappedStatement.getSql();//包含占位符#{}
        //2.5转换sql
        BoundSql boundSql = getBoundSql(sql);
        //3获取预处理对象：preparedStatement
        PreparedStatement preparedStatement = connection.prepareStatement(boundSql.getSqlText());
        //4设置参数
        //输入参数的类名
        String parameterType = mappedStatement.getParameterType();
        //得到Class对象
        Class<?> parameterClass = getClassType(parameterType);
        List<ParameterMapping> parameterMappingList = boundSql.getParameterMappingList();
        for (int i = 0; i < parameterMappingList.size(); i++) {
            ParameterMapping parameterMapping = parameterMappingList.get(i);
            String content = parameterMapping.getContent();//content就是属性名
            //反射
            Field declaredField = parameterClass.getDeclaredField(content);
            //暴力访问
            declaredField.setAccessible(true);
            //读取输入参数的字段，user的字段
            Object o = declaredField.get(params[0]);
            //设置sql的？对应的参数
            preparedStatement.setObject(i + 1, o);
        }
        //5执行sql
        preparedStatement.execute();
    }

    @Override
    public void delete(Configuration configuration, MappedStatement mappedStatement, Object... params) throws Exception {
        //1获取数据连接
        Connection connection = configuration.getDataSource().getConnection();
        //2获取sql语句
        String sql = mappedStatement.getSql();//包含占位符#{}
        //2.5转换sql
        BoundSql boundSql = getBoundSql(sql);
        //3获取预处理对象：preparedStatement
        PreparedStatement preparedStatement = connection.prepareStatement(boundSql.getSqlText());
        //4设置参数
        preparedStatement.setInt(1, (int) params[0]);
        //5执行sql
        preparedStatement.execute();
    }

    @Override
    public void update(Configuration configuration, MappedStatement mappedStatement, Object... params) throws Exception {
        //1获取数据连接
        Connection connection = configuration.getDataSource().getConnection();
        //2获取sql语句
        String sql = mappedStatement.getSql();//包含占位符#{}
        //2.5转换sql
        BoundSql boundSql = getBoundSql(sql);
        //3获取预处理对象：preparedStatement
        PreparedStatement preparedStatement = connection.prepareStatement(boundSql.getSqlText());
        //4设置参数
        //输入参数的类名
        String parameterType = mappedStatement.getParameterType();
        //得到Class对象
        Class<?> parameterClass = getClassType(parameterType);
        List<ParameterMapping> parameterMappingList = boundSql.getParameterMappingList();
        for (int i = 0; i < parameterMappingList.size(); i++) {
            ParameterMapping parameterMapping = parameterMappingList.get(i);
            String content = parameterMapping.getContent();//content就是属性名
            //反射
            Field declaredField = parameterClass.getDeclaredField(content);
            //暴力访问
            declaredField.setAccessible(true);
            //读取输入参数的字段，user的字段
            Object o = declaredField.get(params[0]);
            //设置sql的？对应的参数
            preparedStatement.setObject(i + 1, o);
        }
        //5执行sql
        preparedStatement.execute();
    }

    private Class<?> getClassType(String parameterType) throws ClassNotFoundException {
        if (parameterType != null) {
            Class<?> aClass = Class.forName(parameterType);
            return aClass;
        }
        return null;
    }

    /**
     * 完成对#{}的解析工作：使用？替换#{}、解析出#{}中的字段
     * @param sql 原始sql
     * @return 转换后的sql
     */
    private BoundSql getBoundSql(String sql) {
        //标记处理类：处理占位符
        ParameterMappingTokenHandler parameterMappingTokenHandler = new ParameterMappingTokenHandler();
        GenericTokenParser genericTokenParser = new GenericTokenParser("#{", "}", parameterMappingTokenHandler);
        //解析出来的sql，主要是将#{}替换为？
        String parseSql = genericTokenParser.parse(sql);
        //参数名称，将#{}内的参数名称打包返回
        List<ParameterMapping> parameterMappings = parameterMappingTokenHandler.getParameterMappings();

        BoundSql boundSql = new BoundSql(parseSql, parameterMappings);

        return boundSql;
    }
}
