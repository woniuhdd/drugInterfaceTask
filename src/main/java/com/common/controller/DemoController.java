package com.common.controller;

import com.alibaba.fastjson.JSONObject;
import com.common.config.SystemConfig;
import com.common.config.TokenRestTemplate;
import com.common.entity.IntfRequestBody;
import com.common.entity.IntfResponseBody;
import com.common.utils.DateUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;


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
        IntfRequestBody.RequestInfo info = new IntfRequestBody.RequestInfo();
        JSONObject data=new JSONObject();
        Map<String,JSONObject> input=new HashMap<>();
        Date now=new Date();
        requestBody.setInfo(info);
        info.setInfno(SystemConfig.GET_ORDER);
        data.put("currentPageNumber", String.valueOf(1));
        //订单发送日期
        data.put("startTime", DateUtil.dateFormat(now));
        data.put("endTime", DateUtil.dateFormat(now));
        input.put("data",data);
        info.setInput(input);
        HttpHeaders headers=new HttpHeaders();
        headers.add("content-type","application/json;charset=utf-8");
        HttpEntity<String> reqBody=new HttpEntity<>(JSONObject.toJSONString(requestBody),headers);
        ResponseEntity<IntfResponseBody> responseEntity = tokenRestTemplate.exchange(url + SystemConfig.COMMON_INTERFACES_URL, HttpMethod.POST, reqBody, IntfResponseBody.class);
        log.info("请求结果==={}",responseEntity);
    }
}
