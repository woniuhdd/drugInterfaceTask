package com.jobs;

import com.alibaba.fastjson.JSONObject;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.common.config.SystemConfig;
import com.common.entity.IntfResponseBody;
import com.common.service.MiddleRequestService;
import com.common.utils.DateUtil;
import com.trade.model.MiddleOrderDis;
import com.trade.service.MiddleOrderDisService;
import org.quartz.JobExecutionContext;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.*;

public class MiddleOrderDisJob implements BaseJob {
    private static final Logger log = LoggerFactory.getLogger(MiddleOrderDisJob.class);

    private MiddleOrderDisService orderDisService = QuartzConfig.getBean(MiddleOrderDisService.class);
    private MiddleRequestService requestService=QuartzConfig.getBean(MiddleRequestService.class);

    @Override
    public void execute(JobExecutionContext context) {
        try {
            syncDatas();
        } catch (Exception e) {
            e.printStackTrace();
        }
        log.info("药品发货信息上传任务执行的时间：" + DateUtil.dateTimeFormat(new Date()));
    }

    public void  syncDatas(){
        log.info("药品发货信息上传接口查询");

        //查询未交互的配送信息
        LambdaQueryWrapper<MiddleOrderDis> queryWrapper=new LambdaQueryWrapper<MiddleOrderDis>().
                eq(MiddleOrderDis::getResponseState,"0");
        List<MiddleOrderDis> orderDisList = orderDisService.list(queryWrapper);
        orderDisList.forEach(orderDis->{
            JSONObject data=new JSONObject();
            List<Map<String,Object>> dataList = new ArrayList<>();
            Map<String,Object> map = new HashMap<>();
            map.put("shpId",orderDis.getShpId());
            map.put("shpCnt",orderDis.getShpCnt());
            map.put("manuLotnum",orderDis.getManuLotnum());
            map.put("expyEndtime",orderDis.getExpyEndtime());
            map.put("shpMemo",orderDis.getShpMemo());
            dataList.add(map);
            data.put("dataList",dataList);
            String requestBody = requestService.getRequestBody(SystemConfig.SEND_ORDER_DIS, data);

            try {
                //1.解析结果
                IntfResponseBody body = requestService.getDataByUrl(SystemConfig.COMMON_INTERFACES_URL,requestBody);
                if(body.getInfcode()==0){
                    JSONObject outputData = body.getOutput().getJSONObject("data");
                    if("0".equals(outputData.getString("returnCode"))){
                        orderDis.setResponseState("2");
                    }else{
                        orderDis.setResponseState("3");
                    }
                    orderDis.setResponseInfo(outputData.getString("returnMsg"));
                }else {
                    log.info("药品发货信息上传接口失败======"+body.getErr_msg());
                    orderDis.setResponseState("3");
                    orderDis.setResponseInfo(body.getErr_msg());
                }
                orderDis.setResponseTime(new Date());
                orderDisService.updateById(orderDis);
            } catch (Exception e) {
                e.printStackTrace();
                log.info("药品发货信息上传接口异常");
            }
        });

    }
}
