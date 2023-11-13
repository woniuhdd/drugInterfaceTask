package com.trade.controller;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.common.config.SystemConfig;
import com.common.entity.IntfResponseBody;
import com.common.service.MiddleRequestService;
import com.common.utils.DateUtil;
import com.trade.model.*;
import com.trade.service.*;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.Base64;
import java.util.Date;
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
    @Autowired
    private MiddleInvoiceInfoService invoiceInfoService;
    @Autowired
    private MiddleInvoiceImgService invoiceImgService;

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

    @RequestMapping(value = "/sendInvoiceInfo", method = {RequestMethod.POST})
    @ResponseBody
    public JSONObject sendInvoiceInfo() {
        JSONObject returnJsonObj = new JSONObject();

        LambdaQueryWrapper<MiddleInvoiceInfo> queryWrapper=new LambdaQueryWrapper<MiddleInvoiceInfo>().
                eq(MiddleInvoiceInfo::getResponseState,"0");
        List<MiddleInvoiceInfo> invoiceInfos = invoiceInfoService.list(queryWrapper);
        invoiceInfos.forEach(invoiceInfo->{
            JSONObject data=new JSONObject();
            data.put("invoId",invoiceInfo.getInvoId());
            data.put("invoType",invoiceInfo.getInvoType());
            data.put("invoCode",invoiceInfo.getInvoCode());
            data.put("invono",invoiceInfo.getInvono());
            data.put("billAmt",invoiceInfo.getBillAmt());
            data.put("billTime", DateUtil.dateFormat(invoiceInfo.getBillTime()));
            data.put("invottl",invoiceInfo.getInvottl());
            data.put("invoMemo",invoiceInfo.getInvoMemo());
            String requestBody = requestService.getRequestBody(SystemConfig.SEND_INVOICE_INFO, data);

            try {
                //1.解析结果
                IntfResponseBody body = requestService.getDataByUrl(SystemConfig.COMMON_INTERFACES_URL,requestBody);
                JSONObject outputData = body.getOutput().getJSONObject("data");
                if(body.getInfcode()==0&&outputData.getInteger("returnCode")==1){
                    invoiceInfo.setInvoId(outputData.getString("invoId"));
                    invoiceInfo.setResponseState("2");
                    invoiceInfo.setResponseInfo(outputData.getString("returnMsg"));
                    returnJsonObj.put("resultCode", "1");
                    returnJsonObj.put("resultMsg", "上传发票成功，invoId："+outputData.getString("invoId"));

                }else {
                    log.info("维护发票信息接口失败======"+body.getErr_msg());
                    invoiceInfo.setResponseState("3");
                    invoiceInfo.setResponseInfo(body.getErr_msg());
                    returnJsonObj.put("resultCode", "0");
                    returnJsonObj.put("resultMsg", body.getErr_msg());
                }
                invoiceInfo.setResponseTime(new Date());
                invoiceInfoService.updateById(invoiceInfo);
            } catch (Exception e) {
                e.printStackTrace();
                log.info("维护发票信息接口异常");
                returnJsonObj.put("resultCode", "0");
                returnJsonObj.put("resultMsg", "维护发票信息接口异常");
            }
        });

        return returnJsonObj;
    }

    @RequestMapping(value = "/uploadInvoiceImg", method = {RequestMethod.POST})
    @ResponseBody
    public JSONObject uploadInvoiceImg() {
        JSONObject returnJsonObj = new JSONObject();
        //查询未交互的发票图片
        LambdaQueryWrapper<MiddleInvoiceImg> queryWrapper=new LambdaQueryWrapper<MiddleInvoiceImg>().
                eq(MiddleInvoiceImg::getResponseState,"0");
        List<MiddleInvoiceImg> invoiceImgs = invoiceImgService.list(queryWrapper);
        invoiceImgs.forEach(invoiceImg->{
            JSONObject object = getFileBase64Str(invoiceImg);
            if(object.getString("code").equals("0")){
                invoiceImg.setResponseState("3");
                invoiceImg.setResponseInfo(object.getString("msg"));
                invoiceImg.setResponseTime(new Date());
                invoiceImgService.updateById(invoiceImg);
                return;
            }
            JSONObject data=new JSONObject();
            data.put("invoId",invoiceImg.getInvoId());
            data.put("fileBase64Str",object.getString("fileBase64Str"));
            data.put("fileName",invoiceImg.getFileName());
            String requestParam = requestService.getRequestBody(SystemConfig.SEND_INVOICE_IMG, data);
            try {
                //1.解析结果
                IntfResponseBody body =requestService.getDataByUrl(SystemConfig.COMMON_INTERFACES_URL,requestParam);
                JSONObject outputData = body.getOutput().getJSONObject("data");
                if(body.getInfcode()==0&&outputData.getInteger("returnCode")==1){
                    invoiceImg.setFileId(outputData.getString("fileId"));
                    invoiceImg.setResponseState("2");
                    invoiceImg.setResponseInfo(outputData.getString("returnMsg"));
                    returnJsonObj.put("resultCode", "1");
                    returnJsonObj.put("resultMsg", "上传发票附件成功，fileId："+outputData.getString("fileId"));
                }else {
                    log.info("发票附件上传接口失败======"+body.getErr_msg());
                    invoiceImg.setResponseState("3");
                    invoiceImg.setResponseInfo(body.getErr_msg());
                    returnJsonObj.put("resultCode", "0");
                    returnJsonObj.put("resultMsg", body.getErr_msg());
                }
                invoiceImg.setResponseTime(new Date());
                invoiceImgService.updateById(invoiceImg);
            } catch (Exception e) {
                e.printStackTrace();
                log.info("发票附件上传接口异常");
                returnJsonObj.put("resultCode", "0");
                returnJsonObj.put("resultMsg", "发票附件上传接口异常");
            }
        });
        return returnJsonObj;
    }

    public JSONObject getFileBase64Str(MiddleInvoiceImg invoiceImg){
        JSONObject jsonObject = new JSONObject();
        jsonObject.put("code", "1");
        jsonObject.put("msg", "成功");
        FileInputStream fileInputStream=null;
        try {
            if (StringUtils.isEmpty(invoiceImg.getImgUrl())) {
                jsonObject.put("code", "0");
                jsonObject.put("msg", String.format("准备上传阶段-->该图片地址为空（数据库主键编号：%s），请检查数据库该字段的【url】内容后再试。", invoiceImg.getUuid()));
                return jsonObject;
            }
            if (StringUtils.isEmpty(invoiceImg.getInvoId())) {
                jsonObject.put("code", "0");
                jsonObject.put("msg", String.format("准备上传阶段-->该图片发票Id为空（数据库主键编号：%s），请检查数据库该字段的【invoId】内容后再试。", invoiceImg.getUuid()));
                return jsonObject;
            }
            if (!(invoiceImg.getFileName().toLowerCase().endsWith(".jpg")||
                    invoiceImg.getFileName().toLowerCase().endsWith(".png"))) {
                jsonObject.put("code", "0");
                jsonObject.put("msg", String.format("准备上传阶段-->图片格式必须为jpg/png（数据库主键编号：%s）", invoiceImg.getUuid()));
                return jsonObject;
            }
            File file = new File(invoiceImg.getImgUrl());
            byte[] fileBase64Byte=new byte[(int)file.length()];
            fileInputStream=new FileInputStream(file);
            fileInputStream.read(fileBase64Byte);
            jsonObject.put("fileBase64Str", Base64.getEncoder().encodeToString(fileBase64Byte));
        } catch (Exception e) {
            e.printStackTrace();
        }finally {
            if(fileInputStream!=null) {
                try {
                    fileInputStream.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
        return jsonObject;
    }
}
