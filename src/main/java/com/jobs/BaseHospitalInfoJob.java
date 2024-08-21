package com.jobs;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.common.config.SystemConfig;
import com.common.entity.IntfResponseBody;
import com.common.service.MiddleRequestService;
import com.trade.model.BaseHospitalInfo;
import com.trade.service.BaseHospitalInfoService;
import org.quartz.JobExecutionContext;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

public class BaseHospitalInfoJob implements BaseJob {
    private static final Logger log = LoggerFactory.getLogger(BaseHospitalInfoJob.class);

    DateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");

    private BaseHospitalInfoService baseHospitalInfoService = QuartzConfig.getBean(BaseHospitalInfoService.class);
     private MiddleRequestService requestService=QuartzConfig.getBean(MiddleRequestService.class);

    @Override
    public void execute(JobExecutionContext context) {
        try {
            syncDatas("1");
        } catch (Exception e) {
            e.printStackTrace();
        }
        log.info("获取医疗机构信息任务执行的时间：" + dateFormat.format(new Date()));
    }

    public void  syncDatas( String page){
        log.info("医疗机构接口查询");
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
        JSONObject data = new JSONObject();
        data.put("currentPageNumber", page);
        Date date = new Date();
        Calendar cal = Calendar.getInstance();
        cal.setTime(date);
        cal.add(Calendar.DAY_OF_MONTH, -15);
        data.put("strUpTime",sdf.format(cal.getTime()));
        data.put("endUpTime",sdf.format(date));

        String requestBody = requestService.getRequestBody(SystemConfig.GET_HOSPITAL, data);
        try {
            //1.解析结果
            IntfResponseBody body = requestService.getDataByUrl(SystemConfig.COMMON_INTERFACES_URL,requestBody);
            if(body.getInfcode()==0){
                JSONObject outputData = body.getOutput().getJSONObject("data");
                if("1".equals(outputData.getString("returnCode"))){
                    List<BaseHospitalInfo> hospitalInfos = JSONArray.parseArray(outputData.getString("dataList"), BaseHospitalInfo.class);
                    if (hospitalInfos.size() > 0){
                        baseHospitalInfoService.saveOrUpdateBatch(hospitalInfos);
                        int pageTemp = Integer.valueOf(page);
                        if(pageTemp<outputData.getInteger("totalPageCount")){
                            syncDatas(String.valueOf(++pageTemp));
                        }
                    }
                }else {
                    log.info("调用医疗机构接口失败======"+body.getErr_msg());
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
