package com.lagou.sqlSession;

import com.lagou.pojo.Configuration;
import com.lagou.pojo.MappedStatement;

import java.beans.IntrospectionException;
import java.lang.reflect.InvocationTargetException;
import java.sql.SQLException;
import java.util.List;

public interface Executor {

    public <E> List<E> query(Configuration configuration, MappedStatement mappedStatement, Object ...params) throws SQLException, ClassNotFoundException, NoSuchFieldException, IllegalAccessException, IntrospectionException, InstantiationException, InvocationTargetException;

    public Boolean addDeleteUpdate(Configuration configuration, MappedStatement mappedStatement, Object ...params) throws SQLException, Exception;

   /* public Boolean delete(Configuration configuration, MappedStatement mappedStatement, Object ...params) throws SQLException, Exception;

    public Boolean add(Configuration configuration, MappedStatement mappedStatement, Object ...params) throws SQLException, ClassNotFoundException, NoSuchFieldException, IllegalAccessException;
*/
}
