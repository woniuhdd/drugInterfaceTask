package com.common.interceptor;

import com.alibaba.fastjson.JSONObject;
import com.common.config.SystemConfig;
import com.common.entity.IntfRequestBody;
import com.common.entity.IntfResponseBody;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.io.Charsets;
import org.springframework.http.*;
import org.springframework.http.client.ClientHttpRequestExecution;
import org.springframework.http.client.ClientHttpRequestInterceptor;
import org.springframework.http.client.ClientHttpResponse;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.RestClientException;
import org.springframework.web.client.RestTemplate;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.nio.charset.StandardCharsets;
import java.util.HashMap;
import java.util.Map;

@Slf4j
public class IntfRestTemplateInterceptor implements ClientHttpRequestInterceptor {
    //应用码
    private String appCode;
    //授权码
    private String authCode;
    //请求接口路径
    private String url;
    //接口调用凭证
    private String token=null;

    private RestTemplate restTemplate=new RestTemplate();

    public IntfRestTemplateInterceptor(String appCode, String authCode, String url) {
        this.appCode = appCode;
        this.authCode = authCode;
        this.url = url;
    }

    @Override
    public ClientHttpResponse intercept(HttpRequest request, byte[] body, ClientHttpRequestExecution execution) throws IOException {
        //TODO 加一下token的时效判断
        if(token==null){
            getToken();
        }
//        request.getHeaders().add("Access-Token",token);
        //token改为放在交易输入请求体中
        IntfRequestBody requestBody = JSONObject.parseObject(new String(body, Charsets.UTF_8), IntfRequestBody.class);
        requestBody.getInfo().getInput().get("data").put("accessToken",token);

        ClientHttpResponse response = execution.execute(request, JSONObject.toJSONString(requestBody).getBytes(StandardCharsets.UTF_8));
        int rawStatusCode = response.getRawStatusCode();
        if(rawStatusCode!=200){
            log.info("请求接口异常======>{}：{}",rawStatusCode,response.getStatusText());
        }
//        String responseBody = getBody(response);
//        IntfResponseBody intfResponseBody = JSONObject.parseObject(responseBody, IntfResponseBody.class);

        return response;
    }

    private String getBody(ClientHttpResponse response){
        InputStream inputStream = null ;
        BufferedReader bufferedReader = null ;
        StringBuilder stringBuilder=new StringBuilder();
        try {
            inputStream=response.getBody();
            bufferedReader=new BufferedReader(new InputStreamReader(inputStream));
            char[] chars=new char[128];
            int bytesRead=-1;
            while((bytesRead=bufferedReader.read(chars))>0){
                stringBuilder.append(chars,0,bytesRead);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }finally {
            if(inputStream!=null){
                try {
                    inputStream.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
            if(bufferedReader!=null){
                try {
                    bufferedReader.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
        return stringBuilder.toString();
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
            responseEntity = restTemplate.exchange(url + SystemConfig.TOKEN_URL,
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
