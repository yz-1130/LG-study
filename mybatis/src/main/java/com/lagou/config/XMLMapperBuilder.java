package com.lagou.config;

import com.lagou.pojo.Configuration;
import com.lagou.pojo.MappedStatement;
import org.dom4j.Document;
import org.dom4j.DocumentException;
import org.dom4j.Element;
import org.dom4j.io.SAXReader;

import java.io.InputStream;
import java.util.List;

public class XMLMapperBuilder {
    private Configuration configuration;

    public XMLMapperBuilder(Configuration configuration) {
        this.configuration = configuration;
    }

    public void parse(InputStream inputStream) throws DocumentException {

        Document document = new SAXReader().read(inputStream);
        Element rootElement = document.getRootElement();

        String namespace = rootElement.attributeValue("namespace");
        List<Element> query = rootElement.selectNodes("//select");
        setMappedStatement(query,1,namespace);

        List<Element> insert = rootElement.selectNodes("//insert");
        setMappedStatement(insert,2,namespace);

        List<Element> delete = rootElement.selectNodes("//delete");
        setMappedStatement(delete,3,namespace);

        List<Element> update = rootElement.selectNodes("//update");
        setMappedStatement(update,4,namespace);

    }

    private void setMappedStatement(List<Element> list, int type,String namespace) {
        for (Element element : list) {
            String id = element.attributeValue("id");
            String resultType = element.attributeValue("resultType");
            String parameterType = element.attributeValue("parameterType");
            String sqlText = element.getTextTrim();
            MappedStatement mappedStatement = new MappedStatement();
            mappedStatement.setId(id);
            mappedStatement.setSqlType(type);
            mappedStatement.setParameterType(parameterType);
            mappedStatement.setResultType(resultType);
            mappedStatement.setSql(sqlText);
            String key = namespace+"."+id;
            configuration.getMappedStatementMap().put(key,mappedStatement);
        }
    }


}
