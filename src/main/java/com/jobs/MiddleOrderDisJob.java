package com.jobs;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.common.config.SystemConfig;
import com.common.entity.IntfResponseBody;
import com.common.service.MiddleRequestService;
import com.common.utils.DateUtil;
import com.trade.model.MiddleOrderDis;
import com.trade.model.MiddlePurchaseOrder;
import com.trade.service.MiddleOrderDisService;
import com.trade.service.MiddlePurchaseOrderService;
import org.quartz.JobExecutionContext;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.*;

public class MiddleOrderDisJob implements BaseJob {
    private static final Logger log = LoggerFactory.getLogger(MiddleOrderDisJob.class);

    private MiddleOrderDisService orderDisService = QuartzConfig.getBean(MiddleOrderDisService.class);
    private MiddlePurchaseOrderService middlePurchaseOrderService=QuartzConfig.getBean(MiddlePurchaseOrderService.class);
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
            map.put("expyEndtime",DateUtil.dateFormat(orderDis.getExpyEndtime()));
            map.put("shpMemo",orderDis.getShpMemo());
            dataList.add(map);
            data.put("dataList",dataList);
            String requestBody = requestService.getRequestBody(SystemConfig.SEND_ORDER_DIS, data);

            try {
                //1.解析结果
                IntfResponseBody body = requestService.getDataByUrl(SystemConfig.COMMON_INTERFACES_URL,requestBody);
                if(body.getInfcode()==0){
                    JSONObject outputData = body.getOutput().getJSONObject("data");
                    if("1".equals(outputData.getString("returnCode"))){
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

                //根据采购单号更新采购信息
                MiddlePurchaseOrder middlePurchaseOrder = middlePurchaseOrderService.getById(orderDis.getShpId());
                Calendar cal = Calendar.getInstance();
                cal.setTime(middlePurchaseOrder.getSendTime());
                cal.add(Calendar.DATE, 1);
                JSONObject object = updateMiddlePurchaseOrderListByCode(middlePurchaseOrder.getOrdCode(),DateUtil.dateFormat(middlePurchaseOrder.getSendTime()),DateUtil.dateFormat(cal.getTime()),1);
                if(object.getString("resultCode").equals("0")){
                    orderDis.setResponseInfo(orderDis.getResponseInfo()+"  "+object.getString("resultMsg"));
                }
                orderDisService.updateById(orderDis);

            } catch (Exception e) {
                e.printStackTrace();
                log.info("上传发货信息失败，shpId："+orderDis.getShpId()+","+e.getMessage());
            }
        });

    }


    public JSONObject updateMiddlePurchaseOrderListByCode(String ordCode, String startTime, String endTime,int page){
        JSONObject data = new JSONObject();
        data.put("currentPageNumber",page);
        data.put("ordCode",ordCode);
        data.put("startTime",startTime);
        data.put("endTime",endTime);
        String requestBody = requestService.getRequestBody(SystemConfig.GET_ORDER, data);
        try {
            IntfResponseBody body = requestService.getDataByUrl(SystemConfig.COMMON_INTERFACES_URL, requestBody);
            //1.解析结果
            if(body.getInfcode()==0){
                JSONObject outputData = body.getOutput().getJSONObject("data");
                if("1".equals(outputData.getString("returnCode"))){
                    List<MiddlePurchaseOrder> middlePurchaseOrderList = JSONArray.parseArray(outputData.getString("dataList"), MiddlePurchaseOrder.class);
                    if (middlePurchaseOrderList.size() > 0){
                        if (page == 1){
                            middlePurchaseOrderService.removeByMap(new HashMap<String, Object>(){{
                                put("ord_code",ordCode);
                            }});
                        }
                        middlePurchaseOrderService.saveOrUpdateBatch(middlePurchaseOrderList);
                        if(page < outputData.getInteger("totalPageCount")){
                            updateMiddlePurchaseOrderListByCode(ordCode,startTime,endTime,++page);
                        }
                    }
                }else{
                    log.info("根据采购单号更新采购信息失败======"+body.getErr_msg());
                }
                JSONObject returnJsonObj = new JSONObject();
                returnJsonObj.put("resultCode", "1");
                returnJsonObj.put("resultMsg", "共获取"+outputData.getInteger("totalRecordCount"));
                return returnJsonObj;
            }else {
                log.info("根据采购单号更新采购信息失败======"+body.getErr_msg());
                JSONObject returnJsonObj = new JSONObject();
                returnJsonObj.put("resultCode", "0");
                returnJsonObj.put("resultMsg", body.getErr_msg());
                return returnJsonObj;
            }
        } catch (Exception e) {
            e.printStackTrace();
            log.info("根据采购单号更新采购信息失败");
        }
        JSONObject returnJsonObj = new JSONObject();
        returnJsonObj.put("resultCode", "0");
        returnJsonObj.put("resultMsg", "根据采购单号更新采购信息成功");
        return returnJsonObj;
    }
}
