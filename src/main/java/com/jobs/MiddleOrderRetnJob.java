package com.jobs;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.common.config.SystemConfig;
import com.common.entity.IntfResponseBody;
import com.common.service.MiddleRequestService;
import com.common.utils.DateUtil;
import com.trade.model.MiddleOrderRetn;
import com.trade.service.MiddleOrderRetnService;
import org.quartz.JobExecutionContext;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Calendar;
import java.util.Date;
import java.util.List;

public class MiddleOrderRetnJob implements BaseJob {
    private static final Logger log = LoggerFactory.getLogger(MiddleOrderRetnJob.class);

    private MiddleOrderRetnService middleOrderRetnService = QuartzConfig.getBean(MiddleOrderRetnService.class);
    private MiddleRequestService requestService=QuartzConfig.getBean(MiddleRequestService.class);

    @Override
    public void execute(JobExecutionContext context) {
        try {
            syncDatas("1");
        } catch (Exception e) {
            e.printStackTrace();
        }
        log.info("获取退货订单信息任务执行的时间：" + DateUtil.dateTimeFormat(new Date()));
    }

    public void  syncDatas(String page){
        log.info("退货订单接口查询");

        JSONObject data=new JSONObject();
        Date now=new Date();
        data.put("currentPageNumber", String.valueOf(page));
        //订单发送日期
        Calendar cal = Calendar.getInstance();
        cal.setTime(now);
        cal.add(Calendar.DATE, -1);
        data.put("strUpTime", DateUtil.dateTimeFormat(cal.getTime()));
        data.put("endUpTime", DateUtil.dateTimeFormat(now));
        String requestBody = requestService.getRequestBody(SystemConfig.GET_ORDER_RETN, data);

        try {
            //1.解析结果
            IntfResponseBody body = requestService.getDataByUrl(SystemConfig.COMMON_INTERFACES_URL,requestBody);
            if(body.getInfcode()==0){
                JSONObject outputData = body.getOutput().getJSONObject("data");
                if("1".equals(outputData.getString("returnCode"))){
                    List<MiddleOrderRetn> middleOrderRetnList = JSONArray.parseArray(outputData.getString("dataList"), MiddleOrderRetn.class);
                    if (middleOrderRetnList.size() > 0){
                        middleOrderRetnService.saveOrUpdateBatch(middleOrderRetnList);
                        int pageTemp = Integer.valueOf(page);
                        if(pageTemp<outputData.getInteger("totalPageCount")){
                            syncDatas( String.valueOf(++pageTemp));
                        }
                    }
                }else {
                    log.info("调用退货订单接口失败======"+body.getErr_msg());
                }
            }else {
                log.info("调用退货订单接口失败======"+body.getErr_msg());
            }
        } catch (Exception e) {
            e.printStackTrace();
            log.info("调用退货订单接口异常");
        }
    }
}
