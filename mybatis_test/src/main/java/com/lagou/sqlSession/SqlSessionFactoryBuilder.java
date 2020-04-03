package com.lagou.sqlSession;

import com.lagou.config.XMLConfigBuilder;
import com.lagou.pojo.Configuration;
import org.dom4j.DocumentException;

import java.beans.PropertyVetoException;
import java.io.InputStream;

public class SqlSessionFactoryBuilder {

    public SqlSessionFactory build(InputStream in) throws DocumentException, PropertyVetoException {
        //1.使用dom4j解析配置文件，将解析出来的内容封装到Configuration中
        XMLConfigBuilder xmlConfigBuilder = new XMLConfigBuilder();
        Configuration configuration = xmlConfigBuilder.parseConfig(in);

        //2.创建SqlSessionFactory对象:工厂类：生产sqlSession
        DefaultSqlSessionFactory defaultSqlSessionFactory = new DefaultSqlSessionFactory(configuration);
        return defaultSqlSessionFactory;
    }

}
