package com.gcgo.sqlSession;

import com.gcgo.pojo.Configuration;

import java.lang.reflect.Proxy;
import java.util.List;

public class DefaultSqlSession implements SqlSession {

    private Configuration configuration;

    public DefaultSqlSession(Configuration configuration) {
        this.configuration = configuration;
    }

    @Override
    public <E> List<E> selectList(String statementid, Object... params) throws Exception {
        SimpleExecutor simpleExecutor = new SimpleExecutor();
        List<Object> list = simpleExecutor.query(configuration,
                configuration.getMappedStatementMap().get(statementid), params);

        return (List<E>) list;
    }

    @Override
    public <T> T selectOne(String statementid, Object... params) throws Exception {
        //调用查询所有方法即可
        List<Object> objects = selectList(statementid, params);
        if (objects.size() == 1) return (T) objects.get(0);
        else throw new RuntimeException("查询结果为空或者返回结果过多");
    }

    @Override
    public void insert(String statementid, Object... params) throws Exception {
        SimpleExecutor simpleExecutor = new SimpleExecutor();
        simpleExecutor.insert(configuration, configuration.getMappedStatementMap().get(statementid), params);
    }

    @Override
    public void delete(String statementid, Object... params) throws Exception {
        SimpleExecutor simpleExecutor = new SimpleExecutor();
        simpleExecutor.delete(configuration, configuration.getMappedStatementMap().get(statementid), params);
    }

    @Override
    public void update(String statementid, Object... params) throws Exception {
        SimpleExecutor simpleExecutor = new SimpleExecutor();
        simpleExecutor.update(configuration, configuration.getMappedStatementMap().get(statementid), params);
    }

    @Override
    public <T> T getMapper(Class<?> mapperClass) {
        Object proxyInstance = Proxy.newProxyInstance(
                DefaultSqlSession.class.getClassLoader(),
                new Class[]{mapperClass},
                (proxy, method, args) -> {
                    String methodName = method.getName();
                    String className = method.getDeclaringClass().getName();
                    String statementId = className + "." + methodName;

                    //                    //通过被调用方法返回值类型来判断调用哪个方法查询
                    //                    Type genericReturnType = method.getGenericReturnType();
                    //                    //判断是否有泛型
                    //                    if (genericReturnType instanceof ParameterizedType) {
                    //                        List<Object> objects = selectList(statementId, args);
                    //                        return objects;
                    //                    } else {
                    //                        return selectOne(statementId, args);
                    //                    }

                    //通过方法名判断
                    switch (methodName) {
                        case "findByCondition":
                            return selectOne(statementId, args);
                        case "findAll":
                            return selectList(statementId, args);
                        case "insert":
                            insert(statementId, args);
                            break;
                        case "delete":
                            delete(statementId, args);
                            break;
                        case "update":
                            update(statementId, args);
                            break;
                    }

                    return null;
                });
        return (T) proxyInstance;
    }
}
