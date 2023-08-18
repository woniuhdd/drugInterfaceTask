package com.jobs;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.common.config.SystemConfig;
import com.common.config.TokenRestTemplate;
import com.common.entity.IntfRequestBody;
import com.common.entity.IntfResponseBody;
import com.common.utils.DateUtil;
import com.trade.model.MiddlePurchaseOrder;
import com.trade.service.MiddlePurchaseOrderService;
import org.quartz.JobExecutionContext;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;

import java.util.*;

public class MiddlePurchaseOrderJob implements BaseJob {
    private static final Logger log = LoggerFactory.getLogger(MiddlePurchaseOrderJob.class);

    private MiddlePurchaseOrderService orderService = QuartzConfig.getBean(MiddlePurchaseOrderService.class);
    private TokenRestTemplate tokenRestTemplate=QuartzConfig.getBean(TokenRestTemplate.class);
    @Override
    public void execute(JobExecutionContext context) {
        try {
            syncDatas(1);
        } catch (Exception e) {
            e.printStackTrace();
        }
        log.info("获取采购订单信息任务执行的时间：" + DateUtil.dateTimeFormat(new Date()));
    }

    public void  syncDatas( int page){
        log.info("采购订单接口查询");
        IntfRequestBody requestBody=new IntfRequestBody();
        requestBody.setInfno(SystemConfig.GET_ORDER);

        JSONObject input=new JSONObject();
        Date now=new Date();
        input.put("currentPageNumber", String.valueOf(page));
        //订单发送日期
        input.put("startTime", DateUtil.dateFormat(now));
        input.put("endTime", DateUtil.dateFormat(now));
        requestBody.setInput(input);

        HttpHeaders headers=new HttpHeaders();
        headers.add("content-type","application/json;charset=utf-8");
        HttpEntity<String> reqBody=new HttpEntity<>(JSONObject.toJSONString(requestBody),headers);
        try {
            ResponseEntity<IntfResponseBody> responseEntity = tokenRestTemplate.exchange(SystemConfig.url + SystemConfig.COMMON_INTERFACES_URL,
                    HttpMethod.POST, reqBody, IntfResponseBody.class);
            //1.解析结果
            IntfResponseBody body =responseEntity.getBody();
            if(body.getInfcode()==0){
                JSONObject outputData = JSONObject.parseObject(body.getOutput()).getJSONObject("data");
                List<MiddlePurchaseOrder>  orderList= JSONArray.parseArray(outputData.getString("dataList"), MiddlePurchaseOrder.class);
                orderService.saveOrUpdateBatch(orderList);
                if(page<outputData.getInteger("totalPageCount")){
                    syncDatas( ++page);
                }
            }else {
                log.info("调用采购订单接口失败======"+body.getErr_msg());
            }
        } catch (Exception e) {
            e.printStackTrace();
            log.info("调用采购订单接口异常");
        }
    }
}
