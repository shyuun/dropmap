package com.dropmap.www.template;

import com.fasterxml.jackson.core.JsonProcessingException;

public abstract class DataInitializer {
    //데이터 INSERT 추상클래스
    public void init() throws JsonProcessingException, InterruptedException {
        clearDatabase();//DB초기화
        insertOpenApiData();//OPEN API 데이터 insert
        insertFileApiData();//지자체 FileData insert
        insertUnstructuredData();//API 미제공 비정형 데이터 insert
    }

    protected abstract void clearDatabase();
    protected abstract void insertOpenApiData() throws JsonProcessingException, InterruptedException;
    protected abstract void insertFileApiData() throws JsonProcessingException;
    protected abstract void insertUnstructuredData() throws JsonProcessingException;
}
