package com.gcgo.sqlSession;

import com.gcgo.pojo.Configuration;
import com.gcgo.pojo.MappedStatement;

import java.util.List;

public interface Executor {

    public <E> List<E> query(Configuration configuration, MappedStatement mappedStatement, Object... params) throws Exception;

    public void insert(Configuration configuration, MappedStatement mappedStatement, Object... params) throws Exception;

    public void delete(Configuration configuration, MappedStatement mappedStatement, Object... params) throws Exception;

    public void update(Configuration configuration, MappedStatement mappedStatement, Object... params) throws Exception;
}
