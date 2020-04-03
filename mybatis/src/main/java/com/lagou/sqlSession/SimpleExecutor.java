package com.lagou.sqlSession;

import com.lagou.config.BoundSql;
import com.lagou.pojo.Configuration;
import com.lagou.pojo.MappedStatement;
import com.lagou.utils.GenericTokenParser;
import com.lagou.utils.ParameterMapping;
import com.lagou.utils.ParameterMappingTokenHandler;

import java.beans.IntrospectionException;
import java.beans.PropertyDescriptor;
import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class SimpleExecutor implements Executor{

    @Override
    public <E> List<E> query(Configuration configuration, MappedStatement mappedStatement, Object... params) throws SQLException, ClassNotFoundException, NoSuchFieldException, IllegalAccessException, IntrospectionException, InstantiationException, InvocationTargetException {
        //System.out.println(configuration.getDataSource().toString());
        //1.注册驱动，获取连接
        Connection connection = configuration.getDataSource().getConnection();
        //2.获取sql语句——转换sql语句
        String sql = mappedStatement.getSql();
        BoundSql boundSql = getBoundSql(sql);
        //3.获取预处理对象:preparedStatement
        PreparedStatement preparedStatement = connection.prepareStatement(boundSql.getSqlText());
        //4.设置参数
        String parameterType = mappedStatement.getParameterType();
        Class<?> parameterTypeClass = getClassType(parameterType);
        List<ParameterMapping> parameterMappingList = boundSql.getParameterMappingList();
        for (int i = 0; i < parameterMappingList.size(); i++) {
            ParameterMapping parameterMapping = parameterMappingList.get(i);
            String content = parameterMapping.getContent();

            //反射
            Field declaredField = parameterTypeClass.getDeclaredField(content);
            //暴力访问
            declaredField.setAccessible(true);
            Object o = declaredField.get(params[0]);

            preparedStatement.setObject(i+1,o);

        }
        //5.执行sql
        ResultSet resultSet = preparedStatement.executeQuery();
        String resultType = mappedStatement.getResultType();
        Class<?> resultTypeClass = getClassType(resultType);

        ArrayList<Object> objects = new ArrayList<>();
        //6.封装返回结果集
        while(resultSet.next()){
            String id = resultSet.getString("id");
            String username = resultSet.getString("username");

            Object o = resultTypeClass.newInstance();
            //元数据
            ResultSetMetaData metaData = resultSet.getMetaData();
            for (int i = 1; i <= metaData.getColumnCount(); i++) {
                //字段名
                String columnName = metaData.getColumnName(i);
                //字段的值
                Object value = resultSet.getObject(columnName);
                PropertyDescriptor propertyDescriptor = new PropertyDescriptor(columnName,resultTypeClass);
                Method writeMethod = propertyDescriptor.getWriteMethod();
                writeMethod.invoke(o,value);
            }
            objects.add(o);
        }
        return (List<E>) objects;
    }


    @Override
    public Boolean addDeleteUpdate(Configuration configuration, MappedStatement mappedStatement, Object... params) throws SQLException, ClassNotFoundException, NoSuchFieldException, IllegalAccessException {
        Connection connection = configuration.getDataSource().getConnection();
        //2.获取sql语句——转换sql语句
        String sql = mappedStatement.getSql();
        BoundSql boundSql = getBoundSql(sql);
        //3.获取预处理对象:preparedStatement
        PreparedStatement preparedStatement = connection.prepareStatement(boundSql.getSqlText());
        //4.设置参数
        String parameterType = mappedStatement.getParameterType();
        Class<?> parameterTypeClass = getClassType(parameterType);
        List<ParameterMapping> parameterMappingList = boundSql.getParameterMappingList();
        for (int i = 0; i < parameterMappingList.size(); i++) {
            ParameterMapping parameterMapping = parameterMappingList.get(i);
            String content = parameterMapping.getContent();

            //反射
            Field declaredField = parameterTypeClass.getDeclaredField(content);
            //暴力访问
            declaredField.setAccessible(true);
            Object o = declaredField.get(params[0]);
            preparedStatement.setObject(i+1,o);
        }
        //5.执行sql
        preparedStatement.execute();

        return null;
    }

/*

    @Override
    public Boolean update(Configuration configuration, MappedStatement mappedStatement, Object... params) throws Exception {
        Connection connection = configuration.getDataSource().getConnection();
        //2.获取sql语句——转换sql语句
        String sql = mappedStatement.getSql();
        BoundSql boundSql = getBoundSql(sql);
        //3.获取预处理对象:preparedStatement
        PreparedStatement preparedStatement = connection.prepareStatement(boundSql.getSqlText());
        //4.设置参数
        String parameterType = mappedStatement.getParameterType();
        Class<?> parameterTypeClass = getClassType(parameterType);
        List<ParameterMapping> parameterMappingList = boundSql.getParameterMappingList();
        for (int i = 0; i < parameterMappingList.size(); i++) {
            ParameterMapping parameterMapping = parameterMappingList.get(i);
            String content = parameterMapping.getContent();

            //反射
            Field declaredField = parameterTypeClass.getDeclaredField(content);
            //暴力访问
            declaredField.setAccessible(true);
            Object o = declaredField.get(params[0]);
            preparedStatement.setObject(i+1,o);
        }
        //5.执行sql
        boolean resultSet = preparedStatement.execute();

        return resultSet;
    }

    @Override
    public Boolean delete(Configuration configuration, MappedStatement mappedStatement, Object... params) throws Exception {
        Connection connection = configuration.getDataSource().getConnection();
        //2.获取sql语句——转换sql语句
        String sql = mappedStatement.getSql();
        BoundSql boundSql = getBoundSql(sql);
        //3.获取预处理对象:preparedStatement
        PreparedStatement preparedStatement = connection.prepareStatement(boundSql.getSqlText());
        //4.设置参数
        String parameterType = mappedStatement.getParameterType();
        Class<?> parameterTypeClass = getClassType(parameterType);
        List<ParameterMapping> parameterMappingList = boundSql.getParameterMappingList();
        for (int i = 0; i < parameterMappingList.size(); i++) {
            ParameterMapping parameterMapping = parameterMappingList.get(i);
            String content = parameterMapping.getContent();

            //反射
            Field declaredField = parameterTypeClass.getDeclaredField(content);
            //暴力访问
            declaredField.setAccessible(true);
            Object o = declaredField.get(params[0]);
            preparedStatement.setObject(i+1,o);
        }
        //5.执行sql
        boolean resultSet = preparedStatement.execute();

        return resultSet;
    }
*/



    private Class<?> getClassType(String parameterType) throws ClassNotFoundException {
        if(parameterType!=null){
            Class<?> aClass = Class.forName(parameterType);
            return  aClass;
        }
        return null;
    }


    /**
     * 1.完成sql里的#{}参数替换 2.解析#{}参数值存储
     * @param sql
     * @return
     */
    private BoundSql getBoundSql(String sql) {
        //标记处理类：配置标记解析器来完成对占位符的解析处理工作
        ParameterMappingTokenHandler parameterMappingTokenHandler = new ParameterMappingTokenHandler();
        GenericTokenParser genericTokenParser = new GenericTokenParser("#{","}",parameterMappingTokenHandler);
        //解析出来的sql
        String parseSql = genericTokenParser.parse(sql);
        List<ParameterMapping> parameterMappings = parameterMappingTokenHandler.getParameterMappings();

        BoundSql boundSql = new BoundSql(parseSql,parameterMappings);
        return boundSql;
    }
}
