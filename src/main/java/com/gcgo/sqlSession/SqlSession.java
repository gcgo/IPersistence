package com.gcgo.sqlSession;

import java.util.List;

public interface SqlSession {
    /**
     *
     * @param statementid 传入对应statementid，好从configuration中找到对应的sql
     * @param <E>
     * @return
     */
    public <E> List<E> selectList(String statementid, Object... params) throws Exception;

    /**
     * 查询单个
     * @param statementid
     * @param params
     * @return
     */
    public <T> T selectOne(String statementid, Object... params) throws Exception;

    public void insert(String statementid, Object... params) throws Exception;

    public void delete(String statementid, Object... params) throws Exception;

    public void update(String statementid, Object... params) throws Exception;

    //为Dao接口生成代理实现类
    public <T> T getMapper(Class<?> mapperClass);
}
