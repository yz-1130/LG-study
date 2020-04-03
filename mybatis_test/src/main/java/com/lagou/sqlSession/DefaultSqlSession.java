package com.lagou.sqlSession;

import com.lagou.pojo.Configuration;
import com.lagou.pojo.MappedStatement;

import java.beans.IntrospectionException;
import java.lang.reflect.*;
import java.sql.SQLException;
import java.util.List;

public class DefaultSqlSession implements SqlSession {

    private Configuration configuration;

    public DefaultSqlSession(Configuration configuration) {
        this.configuration = configuration;
    }

    @Override
    public <E> List<E> selectList(String statementId, Object... params) throws IllegalAccessException, IntrospectionException, InstantiationException, NoSuchFieldException, SQLException, InvocationTargetException, ClassNotFoundException {
        //simpleExcutor里query方法的调用
        SimpleExecutor simpleExecutor = new SimpleExecutor();
        MappedStatement mappedStatement = configuration.getMappedStatementMap().get(statementId);
        List<Object> lists = simpleExecutor.query(configuration, mappedStatement, params);
        return (List<E>) lists;
    }

    @Override
    public <T> T selectOne(String statementId, Object... params) throws IllegalAccessException, ClassNotFoundException, IntrospectionException, InstantiationException, SQLException, InvocationTargetException, NoSuchFieldException {
        List<Object> objects = selectList(statementId, params);
        if(objects.size() == 1){
            return (T) objects.get(0);
        }else {
            throw new RuntimeException("查询结果为空或返回结果过多");
        }
    }

    @Override
    public void add(String statementId, Object... params) throws ClassNotFoundException, SQLException, IllegalAccessException, NoSuchFieldException {
        //simpleExcutor里query方法的调用
        SimpleExecutor simpleExecutor = new SimpleExecutor();
        MappedStatement mappedStatement = configuration.getMappedStatementMap().get(statementId);

        simpleExecutor.addDeleteUpdate(configuration, mappedStatement, params);
    }

    @Override
    public void delete(String statementId, Object... params) throws ClassNotFoundException, SQLException, IllegalAccessException, NoSuchFieldException {
        //simpleExcutor里query方法的调用
        SimpleExecutor simpleExecutor = new SimpleExecutor();
        MappedStatement mappedStatement = configuration.getMappedStatementMap().get(statementId);

        simpleExecutor.addDeleteUpdate(configuration, mappedStatement, params);
    }

    @Override
    public void update(String statementId, Object... params) throws ClassNotFoundException, SQLException, IllegalAccessException, NoSuchFieldException {
        //simpleExcutor里query方法的调用
        SimpleExecutor simpleExecutor = new SimpleExecutor();
        MappedStatement mappedStatement = configuration.getMappedStatementMap().get(statementId);

        simpleExecutor.addDeleteUpdate(configuration, mappedStatement, params);
    }

    @Override
    public <T> T getMapper(Class<?> mapperClass) {
        //使用JDK动态代理来为Dao接口生成代理对象，并返回
        Object proxyInstance = Proxy.newProxyInstance(DefaultSqlSession.class.getClassLoader(), new Class[]{mapperClass}, new InvocationHandler() {
            @Override
            public Object invoke(Object proxy, Method method, Object[] args) throws Throwable {
                //准备参数 1：statementId
                String methodName = method.getName();
                String className = method.getDeclaringClass().getName();
                String statementId = className + "." +methodName;

                //准备参数2：params：args
                //获取被调用方法的返回值类型
                Type genericReturnType = method.getGenericReturnType();
                //判断是否进行了泛型类型参数化
                /*if(genericReturnType instanceof ParameterizedType){
                    List<Object> objects = selectList(statementId, args);
                    return objects;
                }
                return selectOne(statementId,args);*/
                MappedStatement mappedStatement = configuration.getMappedStatementMap().get(statementId);

                int type = mappedStatement.getSqlType();
                if(type == 1){
                    if(genericReturnType instanceof ParameterizedType){
                        List<Object> objects = selectList(statementId, args);
                        return objects;
                    }
                    return selectOne(statementId,args);
                }else if(type == 2){
                    add(statementId,args);
                }else if(type == 3){
                    delete(statementId,args);
                }else if(type == 4){
                    update(statementId,args);
                }else {
                    throw new Exception(methodName+"方法xml标签解析异常");
                }
                return null;
            }
        });

        return (T) proxyInstance;
    }
}
