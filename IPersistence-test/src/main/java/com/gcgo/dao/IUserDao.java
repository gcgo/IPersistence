package com.gcgo.dao;

import com.gcgo.pojo.User;

import java.util.List;

public interface IUserDao {

    public List<User> findAll() throws Exception;

    public User findByCondition(User user) throws Exception;

    //增加
    public void insert(User user);

    //删除
    public void delete(Integer id);

    //更新
    public void update(User user);
}
