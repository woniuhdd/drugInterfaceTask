package com.jobs;

import com.alibaba.fastjson.JSONObject;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.common.config.SystemConfig;
import com.common.config.TokenRestTemplate;
import com.common.entity.IntfRequestBody;
import com.common.entity.IntfResponseBody;
import com.common.utils.DateUtil;
import com.trade.model.MiddleOrderRetnResponse;
import com.trade.service.MiddleOrderRetnResponseService;
import org.quartz.JobExecutionContext;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;

import java.util.*;

public class MiddleOrderRetnResponseJob implements BaseJob {
    private static final Logger log = LoggerFactory.getLogger(MiddleOrderRetnResponseJob.class);

    private MiddleOrderRetnResponseService middleOrderRetnResponseService = QuartzConfig.getBean(MiddleOrderRetnResponseService.class);
    private TokenRestTemplate tokenRestTemplate=QuartzConfig.getBean(TokenRestTemplate.class);
    @Override
    public void execute(JobExecutionContext context) {
        try {
            syncDatas();
        } catch (Exception e) {
            e.printStackTrace();
        }
        log.info("药品退货订单响应任务执行的时间：" + DateUtil.dateTimeFormat(new Date()));
    }

    public void  syncDatas(){
        log.info("药品退货订单响应接口查询");
        IntfRequestBody requestBody=new IntfRequestBody();
        IntfRequestBody.RequestInfo info = new IntfRequestBody.RequestInfo();
        Map<String,JSONObject> input=new HashMap<>();
        requestBody.setInfo(info);
        info.setInfno(SystemConfig.SEND_ORDER_RETN);
        //查询未交互的信息
        LambdaQueryWrapper<MiddleOrderRetnResponse> queryWrapper=new LambdaQueryWrapper<MiddleOrderRetnResponse>().
                eq(MiddleOrderRetnResponse::getResponseState,"0");
        List<MiddleOrderRetnResponse> middleOrderRetnResponseList = middleOrderRetnResponseService.list(queryWrapper);
        middleOrderRetnResponseList.forEach(middleOrderRetnResponse->{
            JSONObject data=new JSONObject();
            List<Map<String,Object>> dataList = new ArrayList<>();
            Map<String,Object> map = new HashMap<>();
            map.put("retnId",middleOrderRetnResponse.getRetnId());
            map.put("retnChkStas",middleOrderRetnResponse.getRetnChkStas());
            map.put("delventpFailRea",middleOrderRetnResponse.getDelventpFailRea());
            dataList.add(map);
            data.put("dataList",dataList);
            input.put("data",data);
            info.setInput(input);

            HttpHeaders headers=new HttpHeaders();
            headers.add("content-type","application/json;charset=utf-8");
            HttpEntity<String> reqBody=new HttpEntity<>(JSONObject.toJSONString(requestBody),headers);
            try {
                ResponseEntity<IntfResponseBody> responseEntity = tokenRestTemplate.exchange(SystemConfig.url + SystemConfig.COMMON_INTERFACES_URL,
                        HttpMethod.POST, reqBody, IntfResponseBody.class);
                //1.解析结果
                IntfResponseBody body =responseEntity.getBody();
                if(body.getInfcode()==0){
                    JSONObject outputData = body.getOutput().getJSONObject("data");
                    if("0".equals(outputData.getString("returnCode"))){
                        middleOrderRetnResponse.setResponseState("2");
                    }else{
                        middleOrderRetnResponse.setResponseState("3");
                    }
                    middleOrderRetnResponse.setResponseInfo(outputData.getString("returnMsg"));
                }else {
                    log.info("药品退货订单响应接口失败======"+body.getErr_msg());
                    middleOrderRetnResponse.setResponseState("3");
                    middleOrderRetnResponse.setResponseInfo(body.getErr_msg());
                }
                middleOrderRetnResponse.setResponseTime(new Date());
                middleOrderRetnResponseService.updateById(middleOrderRetnResponse);
            } catch (Exception e) {
                e.printStackTrace();
                log.info("药品退货订单响应接口异常");
            }
        });

    }
}
