package com.lagou.pojo;

public class MappedStatement {
    //id
    private String id;
    //返回值类型
    private String resultType;
    //参数值类型
    private String parameterType;
    //sql语句
    private String sql;
    //sql类型 1.query 2.insert 3.delete 4.update
    private int sqlType;

    public int getSqlType() {
        return sqlType;
    }

    public void setSqlType(int sqlType) {
        this.sqlType = sqlType;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getResultType() {
        return resultType;
    }

    public void setResultType(String resultType) {
        this.resultType = resultType;
    }

    public String getParameterType() {
        return parameterType;
    }

    public void setParameterType(String parameterType) {
        this.parameterType = parameterType;
    }

    public String getSql() {
        return sql;
    }

    public void setSql(String sql) {
        this.sql = sql;
    }
}
