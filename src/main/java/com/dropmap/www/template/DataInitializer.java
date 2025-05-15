package com.dropmap.www.template;

import com.fasterxml.jackson.core.JsonProcessingException;

import java.sql.SQLException;

public abstract class DataInitializer {
    //데이터 INSERT 추상클래스
    public void init() throws JsonProcessingException, InterruptedException, SQLException {
        insertOpenApiData();//OPEN API 데이터 insert
        insertFileApiData();//지자체 FileData insert
        insertUnstructuredData();//API 미제공 비정형 데이터 insert
    }

    protected abstract void insertOpenApiData() throws JsonProcessingException, InterruptedException, SQLException;
    protected abstract void insertFileApiData() throws JsonProcessingException, SQLException;
    protected abstract void insertUnstructuredData() throws JsonProcessingException, SQLException;
}
