package com.dropmap.www.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.http.client.ClientHttpResponse;
import org.springframework.web.client.ResponseErrorHandler;
import org.springframework.web.client.RestTemplate;

import java.io.IOException;
import java.net.URI;

@Configuration
public class RestTemplateConfig {

    @Bean
    public RestTemplate restTemplate() {
        RestTemplate restTemplate = new RestTemplate();

        //400,500 에러처리를 위한 예외를 던지지 않는 ErrorHandler 설정
        restTemplate.setErrorHandler(new ResponseErrorHandler() {
            @Override
            public boolean hasError(ClientHttpResponse response) throws IOException {
                return false;//예외 발생 안함
            }

            @Override
            public void handleError(URI url, HttpMethod method, ClientHttpResponse response) throws IOException {
                //DO NOTHING
            }
        });

        return restTemplate;
    }
}
