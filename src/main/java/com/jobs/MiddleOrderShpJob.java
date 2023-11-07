package com.jobs;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.common.config.SystemConfig;
import com.common.config.TokenRestTemplate;
import com.common.entity.IntfRequestBody;
import com.common.entity.IntfResponseBody;
import com.common.utils.DateUtil;
import com.trade.model.MiddleOrderShp;
import com.trade.model.MiddlePurchaseOrder;
import com.trade.service.MiddleOrderShpService;
import com.trade.service.MiddlePurchaseOrderService;
import org.quartz.JobExecutionContext;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;

import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class MiddleOrderShpJob implements BaseJob {
    private static final Logger log = LoggerFactory.getLogger(MiddleOrderShpJob.class);

    private MiddleOrderShpService middleOrderShpService = QuartzConfig.getBean(MiddleOrderShpService.class);
    private TokenRestTemplate tokenRestTemplate=QuartzConfig.getBean(TokenRestTemplate.class);
    @Override
    public void execute(JobExecutionContext context) {
        try {
            syncDatas(1);
        } catch (Exception e) {
            e.printStackTrace();
        }
        log.info("获取收货信息任务执行的时间：" + DateUtil.dateTimeFormat(new Date()));
    }

    public void  syncDatas(int page){
        log.info("采购收货信息接口查询");
        IntfRequestBody requestBody=new IntfRequestBody();
        IntfRequestBody.RequestInfo info = new IntfRequestBody.RequestInfo();
        Map<String,JSONObject> input=new HashMap<>();
        requestBody.setInfo(info);
        info.setInfno(SystemConfig.GET_ORDER_SHP);

        JSONObject data=new JSONObject();
        data.put("currentPageNumber", String.valueOf(page));
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
                List<MiddleOrderShp> middleOrderShpList = JSONArray.parseArray(outputData.getString("dataList"), MiddleOrderShp.class);
                middleOrderShpService.saveOrUpdateBatch(middleOrderShpList);
                if(page<outputData.getInteger("totalPageCount")){
                    syncDatas(++page);
                }
            }else {
                log.info("调用收货信息接口失败======"+body.getErr_msg());
            }
        } catch (Exception e) {
            e.printStackTrace();
            log.info("调用收货信息接口异常");
        }
    }
}
