package com.jobs;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.common.config.SystemConfig;
import com.common.entity.IntfResponseBody;
import com.common.service.MiddleRequestService;
import com.common.utils.DateUtil;
import com.trade.model.MiddleOrderShp;
import com.trade.service.MiddleOrderShpService;
import org.quartz.JobExecutionContext;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Calendar;
import java.util.Date;
import java.util.List;

public class MiddleOrderShpJob implements BaseJob {
    private static final Logger log = LoggerFactory.getLogger(MiddleOrderShpJob.class);

    private MiddleOrderShpService middleOrderShpService = QuartzConfig.getBean(MiddleOrderShpService.class);
    private MiddleRequestService requestService=QuartzConfig.getBean(MiddleRequestService.class);

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
        log.info("获取收货信息接口查询");
        JSONObject data=new JSONObject();
        data.put("currentPageNumber", String.valueOf(page));
        Date now=new Date();
        Calendar cal = Calendar.getInstance();
        cal.setTime(now);
        cal.add(Calendar.DATE, -1);
        data.put("startTime", DateUtil.dateFormat(cal.getTime()) + " 00:00:00");
        data.put("endTime", DateUtil.dateFormat(now) + " 23:59:59");
        String requestBody = requestService.getRequestBody(SystemConfig.GET_ORDER_SHP, data);
        try {
            //1.解析结果
            IntfResponseBody body = requestService.getDataByUrl(SystemConfig.COMMON_INTERFACES_URL,requestBody);
            if(body.getInfcode()==0){
                JSONObject outputData = body.getOutput().getJSONObject("data");
                if("1".equals(outputData.getString("returnCode"))){
                    List<MiddleOrderShp> middleOrderShpList = JSONArray.parseArray(outputData.getString("dataList"), MiddleOrderShp.class);
                    if (middleOrderShpList.size() > 0){
                        middleOrderShpService.saveOrUpdateBatch(middleOrderShpList);
                        if(page<outputData.getInteger("totalPageCount")){
                            syncDatas(++page);
                        }
                    }
                }else {
                    log.info("调用收货信息接口失败======"+body.getErr_msg());
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
