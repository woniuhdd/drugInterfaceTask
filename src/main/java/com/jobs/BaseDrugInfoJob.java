package com.jobs;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.common.config.SystemConfig;
import com.common.config.TokenRestTemplate;
import com.common.entity.IntfRequestBody;
import com.common.entity.IntfResponseBody;
import com.trade.model.BaseDrugInfo;
import com.trade.service.BaseDrugInfoManager;
import lombok.extern.slf4j.Slf4j;
import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Slf4j
public class BaseDrugInfoJob implements BaseJob {

    DateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");

    private BaseDrugInfoManager baseDrugInfoManager = QuartzConfig.getBean(BaseDrugInfoManager.class);

    private TokenRestTemplate tokenRestTemplate = QuartzConfig.getBean(TokenRestTemplate.class);

    @Override
    public void execute(JobExecutionContext context) throws JobExecutionException {
        try {
            log.info("获取商品信息任务开始的时间：" + dateFormat.format(new Date()));
            syncDatas(1);
        } catch (Exception e) {
            e.printStackTrace();
        }
        log.info("获取商品信息任务执行的时间：" + dateFormat.format(new Date()));
    }

    public void  syncDatas(int page) throws Exception{
        IntfRequestBody requestBody=new IntfRequestBody();
        IntfRequestBody.RequestInfo info = new IntfRequestBody.RequestInfo();
        Map<String,JSONObject> input=new HashMap<>();
        requestBody.setInfo(info);
        info.setInfno(SystemConfig.GET_DRUG);
        JSONObject data = new JSONObject();
        data.put("current",page);
        data.put("size",100);
        input.put("data",data);
        info.setInput(input);

        HttpHeaders headers=new HttpHeaders();
        headers.add("content-type","application/json;charset=utf-8");
        HttpEntity<String> reqBody = new HttpEntity<>(JSONObject.toJSONString(requestBody),headers);
        ResponseEntity<IntfResponseBody> responseEntity = tokenRestTemplate.exchange(SystemConfig.url + SystemConfig.COMMON_INTERFACES_URL, HttpMethod.POST, reqBody, IntfResponseBody.class);
        log.info("请求结果==={}",responseEntity);

        //1.解析结果
        JSONObject resultData = responseEntity.getBody().getOutput();
        if(resultData.getInteger("returnCode")==0){
            if(page==1){
                baseDrugInfoManager.deleteAllDatas();
            }
            List<BaseDrugInfo> druginfos = JSONArray.parseArray(resultData.getString("dataList"), BaseDrugInfo.class);
            baseDrugInfoManager.saveBatch(druginfos);
            if(page < resultData.getInteger("totalPageCount")){
                syncDatas(++page);
            }
        }
    }

}
