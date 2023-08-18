package com.jobs;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.common.config.SystemConfig;
import com.common.config.TokenRestTemplate;
import com.common.entity.IntfRequestBody;
import com.common.entity.IntfResponseBody;
import com.trade.model.BaseHospitalInfo;
import com.trade.service.BaseHospitalInfoManager;
import org.quartz.JobExecutionContext;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

public class BaseHospitalInfoJob implements BaseJob {
    private static final Logger log = LoggerFactory.getLogger(BaseHospitalInfoJob.class);

    DateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");


    private BaseHospitalInfoManager baseHospitalInfoManager = QuartzConfig.getBean(BaseHospitalInfoManager.class);
    private TokenRestTemplate tokenRestTemplate=QuartzConfig.getBean(TokenRestTemplate.class);
    @Override
    public void execute(JobExecutionContext context) {
        try {
            syncDatas(1);
        } catch (Exception e) {
            e.printStackTrace();
        }
        log.info("获取医疗机构信息任务执行的时间：" + dateFormat.format(new Date()));
    }

    public void  syncDatas( int page){
        log.info("医疗机构接口查询");
        IntfRequestBody requestBody=new IntfRequestBody();
        requestBody.setInfno(SystemConfig.GET_HOSPITAL);

        JSONObject input=new JSONObject();
        JSONObject data=new JSONObject();
        data.put("current",page);
        data.put("size",100);
        input.put("data",data);
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
                if(page==1){
                    baseHospitalInfoManager.deleteAllDatas();
                }
                JSONObject outputData = JSONObject.parseObject(body.getOutput()).getJSONObject("data");
                List<BaseHospitalInfo>  hospitalInfos= JSONArray.parseArray(outputData.getString("dataList"), BaseHospitalInfo.class);
                baseHospitalInfoManager.saveBatch(hospitalInfos);
                if(page<outputData.getInteger("totalPageCount")){
                    syncDatas( ++page);
                }
            }else {
                log.info("调用医疗机构接口失败======"+body.getErr_msg());
            }
        } catch (Exception e) {
            e.printStackTrace();
            log.info("调用医疗机构接口异常");
        }
    }
}
