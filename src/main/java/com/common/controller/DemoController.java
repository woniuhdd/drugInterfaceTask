package com.common.controller;

import com.alibaba.fastjson.JSONObject;
import com.common.config.SystemConfig;
import com.common.config.TokenRestTemplate;
import com.common.entity.IntfRequestBody;
import com.common.entity.IntfResponseBody;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;


@RestController
@Slf4j
public class DemoController {

    @Value("${intf.url}")
    private String url;

    @Resource
    private TokenRestTemplate tokenRestTemplate;

    @GetMapping ("/getOrder")
    public void getOrder(){
        IntfRequestBody requestBody=new IntfRequestBody();
        requestBody.setInfno(SystemConfig.GET_ORDER);

        JSONObject input=new JSONObject();
        JSONObject data=new JSONObject();
        input.put("data",data);
        requestBody.setInput(input);

        HttpHeaders headers=new HttpHeaders();
        headers.add("content-type","application/json;charset=utf-8");
        HttpEntity<String> reqBody=new HttpEntity<>(JSONObject.toJSONString(requestBody),headers);
        ResponseEntity<IntfResponseBody> responseEntity = tokenRestTemplate.exchange(url + SystemConfig.COMMON_INTERFACES_URL, HttpMethod.POST, reqBody, IntfResponseBody.class);
        log.info("请求结果==={}",responseEntity);
    }
}
