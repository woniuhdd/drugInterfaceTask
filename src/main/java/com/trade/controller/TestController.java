package com.trade.controller;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.common.config.SystemConfig;
import com.common.entity.IntfResponseBody;
import com.common.service.MiddleRequestService;
import com.trade.model.BaseCompanyInfo;
import com.trade.model.BaseDrugInfo;
import com.trade.model.BaseHospitalInfo;
import com.trade.model.MiddlePurchaseOrder;
import com.trade.service.BaseCompanyInfoService;
import com.trade.service.BaseDrugInfoService;
import com.trade.service.BaseHospitalInfoService;
import com.trade.service.MiddlePurchaseOrderService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@Slf4j
public class TestController {

    @Autowired
    private MiddleRequestService requestService;
    @Autowired
    private BaseCompanyInfoService baseCompanyInfoService;
    @Autowired
    private BaseDrugInfoService baseDrugInfoService;
    @Autowired
    private BaseHospitalInfoService baseHospitalInfoService;
    @Autowired
    private MiddlePurchaseOrderService middlePurchaseOrderService;

    @RequestMapping(value = "/getBaseCompanyInfoList", method = {RequestMethod.POST})
    @ResponseBody
    public JSONObject getBaseCompanyInfoList(String dataList,String startTime, String endTime,int page) {

        JSONObject data = new JSONObject();
        data.put("currentPageNumber",page);
        data.put("dataList",dataList);
        data.put("startTime",startTime);
        data.put("endTime",endTime);
        String requestBody = requestService.getRequestBody(SystemConfig.GET_COMPANY, data);
        try {
            IntfResponseBody body = requestService.getDataByUrl(SystemConfig.COMMON_INTERFACES_URL, requestBody);
            //1.解析结果
            if(body.getInfcode()==0){
                JSONObject outputData = body.getOutput().getJSONObject("data");
                if("1".equals(outputData.getString("returnCode"))){
                    List<BaseCompanyInfo> baseCompanyInfoList = JSONArray.parseArray(outputData.getString("dataList"), BaseCompanyInfo.class);
                    if (baseCompanyInfoList.size() > 0){
                        baseCompanyInfoService.saveOrUpdateBatch(baseCompanyInfoList);
                        if(page < outputData.getInteger("totalPageCount")){
                            getBaseCompanyInfoList(dataList,startTime,endTime,++page);
                        }
                    }
                }else{
                    log.info("调用企业接口失败======"+body.getErr_msg());
                }
                JSONObject returnJsonObj = new JSONObject();
                returnJsonObj.put("resultCode", "1");
                returnJsonObj.put("resultMsg", "共获取"+outputData.getInteger("totalRecordCount"));
                return returnJsonObj;
            }else {
                log.info("调用企业接口失败======"+body.getErr_msg());
                JSONObject returnJsonObj = new JSONObject();
                returnJsonObj.put("resultCode", "0");
                returnJsonObj.put("resultMsg", body.getErr_msg());
                return returnJsonObj;
            }
        } catch (Exception e) {
            e.printStackTrace();
            log.info("调用企业接口失败");
        }
        JSONObject returnJsonObj = new JSONObject();
        returnJsonObj.put("resultCode", "0");
        returnJsonObj.put("resultMsg", "调用企业接口失败");
        return returnJsonObj;
    }

    /**
     * 初次全量获取
     */
    @RequestMapping(value = "/getBaseDrugInfoList", method = {RequestMethod.POST})
    @ResponseBody
    public JSONObject getBaseDrugInfoList(String dataList,String startTime, String endTime,int page) {

        JSONObject data = new JSONObject();
        data.put("currentPageNumber",page);
        data.put("dataList",dataList);
        data.put("startTime",startTime);
        data.put("endTime",endTime);
        String requestBody = requestService.getRequestBody(SystemConfig.GET_DRUG, data);
        try {
            IntfResponseBody body = requestService.getDataByUrl(SystemConfig.COMMON_INTERFACES_URL, requestBody);
            //1.解析结果
            if(body.getInfcode()==0){
                JSONObject outputData = body.getOutput().getJSONObject("data");
                if("1".equals(outputData.getString("returnCode"))){
                    List<BaseDrugInfo> druginfos = JSONArray.parseArray(outputData.getString("dataList"), BaseDrugInfo.class);
                    if (druginfos.size() > 0){
                        baseDrugInfoService.saveOrUpdateBatch(druginfos);
                        if(page < outputData.getInteger("totalPageCount")){
                            getBaseDrugInfoList(dataList,startTime,endTime,++page);
                        }
                    }
                }else{
                    log.info("获取药品信息失败======"+body.getErr_msg());
                }
                JSONObject returnJsonObj = new JSONObject();
                returnJsonObj.put("resultCode", "1");
                returnJsonObj.put("resultMsg", "共获取"+outputData.getInteger("totalRecordCount"));
                return returnJsonObj;
            }else {
                log.info("获取药品信息失败======"+body.getErr_msg());
                JSONObject returnJsonObj = new JSONObject();
                returnJsonObj.put("resultCode", "0");
                returnJsonObj.put("resultMsg", body.getErr_msg());
                return returnJsonObj;
            }
        } catch (Exception e) {
            e.printStackTrace();
            log.info("获取药品信息失败");
        }
        JSONObject returnJsonObj = new JSONObject();
        returnJsonObj.put("resultCode", "0");
        returnJsonObj.put("resultMsg", "获取药品信息失败");
        return returnJsonObj;
    }

    @RequestMapping(value = "/getBaseHospitalInfoList", method = {RequestMethod.POST})
    @ResponseBody
    public JSONObject getBaseHospitalInfoList(String dataList,String startTime, String endTime,int page) {

        JSONObject data = new JSONObject();
        data.put("currentPageNumber",page);
        data.put("dataList",dataList);
        data.put("strUpTime",startTime);
        data.put("endUpTime",endTime);
        String requestBody = requestService.getRequestBody(SystemConfig.GET_HOSPITAL, data);
        try {
            IntfResponseBody body = requestService.getDataByUrl(SystemConfig.COMMON_INTERFACES_URL, requestBody);
            //1.解析结果
            if(body.getInfcode()==0){
                JSONObject outputData = body.getOutput().getJSONObject("data");
                if("1".equals(outputData.getString("returnCode"))){
                    List<BaseHospitalInfo> baseHospitalInfoList = JSONArray.parseArray(outputData.getString("dataList"), BaseHospitalInfo.class);
                    if (baseHospitalInfoList.size() > 0){
                        baseHospitalInfoService.saveOrUpdateBatch(baseHospitalInfoList);
                        if(page < outputData.getInteger("totalPageCount")){
                            getBaseHospitalInfoList(dataList,startTime,endTime,++page);
                        }
                    }
                }else{
                    log.info("获取医疗机构信息失败======"+body.getErr_msg());
                }
                JSONObject returnJsonObj = new JSONObject();
                returnJsonObj.put("resultCode", "1");
                returnJsonObj.put("resultMsg", "共获取"+outputData.getInteger("totalRecordCount"));
                return returnJsonObj;
            }else {
                log.info("获取医疗机构信息失败======"+body.getErr_msg());
                JSONObject returnJsonObj = new JSONObject();
                returnJsonObj.put("resultCode", "0");
                returnJsonObj.put("resultMsg", body.getErr_msg());
                return returnJsonObj;
            }
        } catch (Exception e) {
            e.printStackTrace();
            log.info("获取医疗机构信息失败");
        }
        JSONObject returnJsonObj = new JSONObject();
        returnJsonObj.put("resultCode", "0");
        returnJsonObj.put("resultMsg", "获取医疗机构信息失败");
        return returnJsonObj;
    }


    @RequestMapping(value = "/getMiddlePurchaseOrderList", method = {RequestMethod.POST})
    @ResponseBody
    public JSONObject getMiddlePurchaseOrderList(String startTime, String endTime,int page) {
        JSONObject data = new JSONObject();
        data.put("currentPageNumber",page);
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
                        middlePurchaseOrderService.saveOrUpdateBatch(middlePurchaseOrderList);
                        if(page < outputData.getInteger("totalPageCount")){
                            getMiddlePurchaseOrderList(startTime,endTime,++page);
                        }
                    }
                }else{
                    log.info("调用采购订单接口失败======"+body.getErr_msg());
                }
                JSONObject returnJsonObj = new JSONObject();
                returnJsonObj.put("resultCode", "1");
                returnJsonObj.put("resultMsg", "共获取"+outputData.getInteger("totalRecordCount"));
                return returnJsonObj;
            }else {
                log.info("调用采购订单接口失败======"+body.getErr_msg());
                JSONObject returnJsonObj = new JSONObject();
                returnJsonObj.put("resultCode", "0");
                returnJsonObj.put("resultMsg", body.getErr_msg());
                return returnJsonObj;
            }
        } catch (Exception e) {
            e.printStackTrace();
            log.info("调用采购订单接口失败");
        }
        JSONObject returnJsonObj = new JSONObject();
        returnJsonObj.put("resultCode", "0");
        returnJsonObj.put("resultMsg", "调用采购订单接口失败");
        return returnJsonObj;
    }
}
