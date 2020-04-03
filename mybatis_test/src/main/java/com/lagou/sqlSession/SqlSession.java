package com.lagou.sqlSession;

import java.beans.IntrospectionException;
import java.lang.reflect.InvocationTargetException;
import java.sql.SQLException;
import java.util.List;

public interface SqlSession {

    //查询所有
    public <E> List<E> selectList(String statementId,Object... params) throws IllegalAccessException, IntrospectionException, InstantiationException, NoSuchFieldException, SQLException, InvocationTargetException, ClassNotFoundException;

    //查询单个
    public <T> T selectOne(String statementId,Object... params) throws IllegalAccessException, ClassNotFoundException, IntrospectionException, InstantiationException, SQLException, InvocationTargetException, NoSuchFieldException;

    public void add(String statementId, Object... params) throws ClassNotFoundException, SQLException, IllegalAccessException, NoSuchFieldException;

    public void delete(String statementId, Object... params) throws ClassNotFoundException, SQLException, IllegalAccessException, NoSuchFieldException;

    public void update(String statementId, Object... params) throws ClassNotFoundException, SQLException, IllegalAccessException, NoSuchFieldException;

    //为Dao方法生成接口实现类
    public <T> T getMapper(Class<?> mapperClass);
}
