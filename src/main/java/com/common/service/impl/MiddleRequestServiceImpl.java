package com.common.service.impl;

import com.alibaba.fastjson.JSONObject;
import com.common.config.SystemConfig;
import com.common.entity.IntfRequestBody;
import com.common.entity.IntfResponseBody;
import com.common.service.MiddleRequestService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.*;
import org.springframework.stereotype.Service;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.RestClientException;
import org.springframework.web.client.RestTemplate;

import java.util.HashMap;
import java.util.Map;

@Slf4j
@Service
public class MiddleRequestServiceImpl implements MiddleRequestService {
    private RestTemplate restTemplate=new RestTemplate();
    @Value("${intf.url}")
    private String intfUrl;
    @Value("${intf.appCode}")
    private String appCode;
    @Value("${intf.authCode}")
    private String authCode;
    //接口调用凭证
    private String token=null;
    @Override
    public String getRequestBody(String infoNo, JSONObject data) {
        if(token==null){
            getToken();
        }
        data.put("accessToken",token);
        IntfRequestBody requestBody=new IntfRequestBody();
        IntfRequestBody.RequestInfo info = new IntfRequestBody.RequestInfo();
        Map<String,JSONObject> input=new HashMap<>();
        requestBody.setInfo(info);
        input.put("data",data);
        info.setInput(input);
        info.setInfno(infoNo);
        return JSONObject.toJSONString(requestBody);
    }

    @Override
    public IntfResponseBody getDataByUrl(String url, String requestParam) {
        IntfResponseBody body=new IntfResponseBody();
        HttpHeaders headers=new HttpHeaders();
        headers.add("content-type","application/json;charset=utf-8");
        HttpEntity<String> reqBody=new HttpEntity<>(requestParam,headers);
        try {

            ResponseEntity<IntfResponseBody> responseEntity = restTemplate.exchange(intfUrl+url,
                    HttpMethod.POST, reqBody, IntfResponseBody.class);
            //1.解析结果
           body =responseEntity.getBody();
            if("令牌无效，请重新获取令牌".equals(body.getErr_msg())){
                log.info("令牌无效，请重新获取令牌");
                getToken();
                IntfRequestBody requestBody = JSONObject.parseObject(requestParam, IntfRequestBody.class);
                requestBody.getInfo().getInput().get("data").put("accessToken",token);
                return getDataByUrl(url,JSONObject.toJSONString(requestBody));
            }
            if("该接口访问间隔为5秒！".equals(body.getErr_msg())){
                log.info("该接口访问间隔为5秒！");
                Thread.sleep(5000);
                return getDataByUrl(url,requestParam);
            }
        } catch (Exception e) {
            log.error("调用药品接口异常:{}",e.getMessage());
        }
        return body;
    }

    public void getToken(){
        HttpHeaders headers=new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_FORM_URLENCODED);
        MultiValueMap<String,String> params=new LinkedMultiValueMap<>();
        params.add("appCode",appCode);
        params.add("authCode",authCode);
        HttpEntity<MultiValueMap<String,String>> reqBody=new HttpEntity<>(params,headers);

        ResponseEntity<IntfResponseBody> responseEntity = null;
        try {
            responseEntity = restTemplate.exchange(intfUrl + SystemConfig.TOKEN_URL,
                    HttpMethod.POST, reqBody, IntfResponseBody.class);
            HttpStatus statusCode = responseEntity.getStatusCode();
            if(statusCode.value()==200){
                IntfResponseBody body = responseEntity.getBody();
                if(body.getInfcode()==0||"令牌有效期超过5分钟，不能再次获取令牌".equals(body.getErr_msg())){
                    JSONObject data = body.getOutput().getJSONObject("data");
                    token=data.getString("accessToken");
                }else{
                    log.info("获取token异常：{}",body.getErr_msg());
                }
            }else {
                log.info("请求token服务异常======>{}:{}",statusCode.value(),statusCode.getReasonPhrase());
            }
        } catch (RestClientException e) {
            e.printStackTrace();
        }

    }
}
