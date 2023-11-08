package com.jobs;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.common.config.SystemConfig;
import com.common.entity.IntfResponseBody;
import com.common.service.MiddleRequestService;
import com.common.utils.DateUtil;
import com.trade.model.MiddlePurchaseOrder;
import com.trade.service.MiddlePurchaseOrderService;
import org.quartz.JobExecutionContext;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.*;

public class MiddlePurchaseOrderJob implements BaseJob {
    private static final Logger log = LoggerFactory.getLogger(MiddlePurchaseOrderJob.class);

    private MiddlePurchaseOrderService orderService = QuartzConfig.getBean(MiddlePurchaseOrderService.class);
    private MiddleRequestService requestService=QuartzConfig.getBean(MiddleRequestService.class);

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
        JSONObject data=new JSONObject();
        Date now=new Date();
        data.put("currentPageNumber", String.valueOf(page));
        //订单发送日期
        Calendar cal = Calendar.getInstance();
        cal.setTime(now);
        cal.add(Calendar.DAY_OF_MONTH, -1);
        data.put("startTime", DateUtil.dateFormat(cal.getTime()));
        data.put("endTime", DateUtil.dateFormat(now));
        String requestBody = requestService.getRequestBody(SystemConfig.GET_ORDER, data);
        try {
            //1.解析结果
            IntfResponseBody body = requestService.getDataByUrl(SystemConfig.COMMON_INTERFACES_URL,requestBody);
            if(body.getInfcode()==0){
                JSONObject outputData = body.getOutput().getJSONObject("data");
                if("1".equals(outputData.getString("returnCode"))){
                    List<MiddlePurchaseOrder> orderList = JSONArray.parseArray(outputData.getString("dataList"), MiddlePurchaseOrder.class);
                    if (orderList.size() > 0){
                        orderService.saveOrUpdateBatch(orderList);
                        if(page<outputData.getInteger("totalPageCount")){
                            syncDatas(++page);
                        }
                    }
                }else {
                    log.info("调用采购订单接口失败======"+body.getErr_msg());
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
