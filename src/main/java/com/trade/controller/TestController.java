package com.trade.controller;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.common.InterfaceConfigHelper;
import com.common.config.SystemConfig;
import com.common.entity.IntfResponseBody;
import com.common.service.MiddleRequestService;
import com.common.utils.DateUtil;
import com.enums.HttpStatusEnum;
import com.trade.model.*;
import com.trade.service.*;
import lombok.extern.slf4j.Slf4j;
import okhttp3.Call;
import okhttp3.Request;
import okhttp3.Response;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import java.io.*;
import java.util.*;

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
    private MiddleInvoiceResponseService invoiceInfoService;
    @Autowired
    private MiddleFilesService invoiceImgService;
    @Autowired
    private MiddleInvoiceService invoiceService;
    @Autowired
    private MiddleInvoiceShpService invoiceShpService;
    @Autowired
    private MiddleOrderRetnService middleOrderRetnService;
    @Autowired
    private MiddleOrderShpService middleOrderShpService;
    @Autowired
    private MiddleOrderDisService orderDisService;

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
        returnJsonObj.put("resultMsg", "调用采购订单接口成功");
        return returnJsonObj;
    }


    @RequestMapping(value = "/getMiddleInvoiceInfoList", method = {RequestMethod.POST})
    @ResponseBody
    public JSONObject getMiddleInvoiceInfoList(String startTime, String endTime,int page) {
        JSONObject data = new JSONObject();
        data.put("currentPageNumber",page);
        data.put("startTime",startTime);
        data.put("endTime",endTime);
        String requestBody = requestService.getRequestBody(SystemConfig.GET_INVOICE_INFO, data);
        try {
            IntfResponseBody body = requestService.getDataByUrl(SystemConfig.COMMON_INTERFACES_URL, requestBody);
            //1.解析结果
            if(body.getInfcode()==0){
                JSONObject outputData = body.getOutput().getJSONObject("data");
                if("1".equals(outputData.getString("returnCode"))){
                    List<MiddleInvoice> invoiceList = JSONArray.parseArray(outputData.getString("dataList"), MiddleInvoice.class);
                    if (invoiceList.size() > 0){
                        invoiceService.saveOrUpdateBatch(invoiceList);
                        if(page < outputData.getInteger("totalPageCount")){
                            getMiddleInvoiceInfoList(startTime,endTime,++page);
                        }
                    }
                }else{
                    log.info("调用获取发票信息接口失败======"+body.getErr_msg());
                }
                JSONObject returnJsonObj = new JSONObject();
                returnJsonObj.put("resultCode", "1");
                returnJsonObj.put("resultMsg", "共获取"+outputData.getInteger("totalRecordCount"));
                return returnJsonObj;
            }else {
                log.info("调用获取发票信息接口失败======"+body.getErr_msg());
                JSONObject returnJsonObj = new JSONObject();
                returnJsonObj.put("resultCode", "0");
                returnJsonObj.put("resultMsg", body.getErr_msg());
                return returnJsonObj;
            }
        } catch (Exception e) {
            e.printStackTrace();
            log.info("调用获取发票信息接口失败");
        }
        JSONObject returnJsonObj = new JSONObject();
        returnJsonObj.put("resultCode", "0");
        returnJsonObj.put("resultMsg", "调用获取发票信息接口失败");
        return returnJsonObj;
    }


    @RequestMapping(value = "/sendInvoiceInfo", method = {RequestMethod.POST})
    @ResponseBody
    public JSONObject sendInvoiceInfo() {
        JSONObject returnJsonObj = new JSONObject();

        LambdaQueryWrapper<MiddleInvoiceResponse> queryWrapper=new LambdaQueryWrapper<MiddleInvoiceResponse>().
                eq(MiddleInvoiceResponse::getResponseState,"0");
        List<MiddleInvoiceResponse> invoiceInfos = invoiceInfoService.list(queryWrapper);
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
                    invoiceInfo.setRetnInvoId(outputData.getString("invoId"));
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
        LambdaQueryWrapper<MiddleFiles> queryWrapper=new LambdaQueryWrapper<MiddleFiles>().
                eq(MiddleFiles::getResponseState,"0");
        List<MiddleFiles> invoiceImgs = invoiceImgService.list(queryWrapper);
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
            String url = invoiceImg.getFileUrl();
            data.put("invoId",invoiceImg.getInvoId());
            data.put("fileBase64Str",object.getString("fileBase64Str"));
            data.put("fileName",url.substring(url.lastIndexOf("/")+1));
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

    public JSONObject getFileBase64Str(MiddleFiles invoiceFile){
        JSONObject jsonObject = new JSONObject();
        jsonObject.put("code", "1");
        jsonObject.put("msg", "成功");
        ByteArrayOutputStream outStream = new ByteArrayOutputStream();
        InputStream inputStream = null;
        try {
            if (StringUtils.isEmpty(invoiceFile.getFileUrl())) {
                jsonObject.put("code", "0");
                jsonObject.put("msg", String.format("准备上传阶段-->该图片地址为空（数据库主键编号：%s），请检查数据库该字段的【url】内容后再试。", invoiceFile.getMiddleFileId()));
                return jsonObject;
            }
            if (StringUtils.isEmpty(invoiceFile.getInvoId())) {
                jsonObject.put("code", "0");
                jsonObject.put("msg", String.format("准备上传阶段-->该图片发票Id为空（数据库主键编号：%s），请检查数据库该字段的【invoId】内容后再试。", invoiceFile.getMiddleFileId()));
                return jsonObject;
            }
            if (!(invoiceFile.getFileName().equalsIgnoreCase("jpg")||
                    invoiceFile.getFileName().equalsIgnoreCase("png"))) {
                jsonObject.put("code", "0");
                jsonObject.put("msg", String.format("准备上传阶段-->图片格式必须为jpg/png（数据库主键编号：%s）", invoiceFile.getMiddleFileId()));
                return jsonObject;
            }
            Request request = (new okhttp3.Request.Builder()).get().url(invoiceFile.getFileUrl()).build();
            Call call = InterfaceConfigHelper.okHttpClient.newCall(request);
            Response response = call.execute();

            if (response.code() != HttpStatusEnum.OK.getKey()) {
                jsonObject.put("code", "0");
                jsonObject.put("msg", String.format("下载网络地址图片到本地阶段-->下载图片失败（数据库主键编号：%s），请求HTTP状态为：%s（%s）。", invoiceFile.getMiddleFileId(), HttpStatusEnum.getValueByKey(response.code()), response.code()));
                return jsonObject;
            }
            inputStream = response.body().byteStream();
            byte[] buffer = new byte[1024];
            int len;
            while((len = inputStream.read(buffer)) != -1) {
                outStream.write(buffer, 0, len);
            }
            byte[] fileBase64Byte = outStream.toByteArray();
            jsonObject.put("fileBase64Str",Base64.getEncoder().encodeToString(fileBase64Byte));
        } catch (Exception e) {
            e.printStackTrace();
            jsonObject.put("code", "0");
            jsonObject.put("msg", String.format("获取图片-->图片获取失败：", e.getMessage()));
            return jsonObject;
        }finally {
            if(inputStream!=null) {
                try {
                    inputStream.close();
                } catch (IOException e) {
                    e.printStackTrace();
                    jsonObject.put("code", "0");
                    jsonObject.put("msg", String.format("关闭输入流失败-->", e.getMessage()));
                    return jsonObject;
                }
            }
            if(outStream!=null) {
                try {
                    outStream.close();
                } catch (IOException e) {
                    e.printStackTrace();
                    jsonObject.put("code", "0");
                    jsonObject.put("msg", String.format("关闭输出流失败-->", e.getMessage()));
                    return jsonObject;
                }
            }
        }
        return jsonObject;
    }

    @RequestMapping(value = "/sendInvoiceShp", method = {RequestMethod.POST})
    @ResponseBody
    public JSONObject sendInvoiceShp() {
        JSONObject returnJsonObj = new JSONObject();

        LambdaQueryWrapper<MiddleInvoiceShp> queryWrapper=new LambdaQueryWrapper<MiddleInvoiceShp>().
                eq(MiddleInvoiceShp::getResponseState,"0");
        List<MiddleInvoiceShp> invoiceShps = invoiceShpService.list(queryWrapper);
        invoiceShps.forEach(invoiceShp->{
            JSONObject data=new JSONObject();
            data.put("invoIds", Arrays.asList(invoiceShp.getInvoId()));
            data.put("invoType",invoiceShp.getInvoType());
            data.put("shpId",invoiceShp.getShpId());
            String requestBody = requestService.getRequestBody(SystemConfig.SEND_INVOICE_SHP, data);
            try {
                //1.解析结果
                IntfResponseBody body = requestService.getDataByUrl(SystemConfig.COMMON_INTERFACES_URL,requestBody);
                JSONObject outputData = body.getOutput().getJSONObject("data");
                if(body.getInfcode()==0&&outputData.getInteger("returnCode")==1){
                    invoiceShp.setResponseState("2");
                    invoiceShp.setResponseInfo(outputData.getString("returnMsg"));
                    returnJsonObj.put("resultCode", "1");
                    returnJsonObj.put("resultMsg", "药品设置发票成功");

                }else {
                    log.info("药品设置发票接口失败======"+body.getErr_msg());
                    invoiceShp.setResponseState("3");
                    invoiceShp.setResponseInfo(body.getErr_msg());
                    returnJsonObj.put("resultCode", "0");
                    returnJsonObj.put("resultMsg", body.getErr_msg());
                }
                invoiceShp.setResponseTime(new Date());
                invoiceShpService.updateById(invoiceShp);
            } catch (Exception e) {
                e.printStackTrace();
                log.info("药品设置发票接口异常");
                returnJsonObj.put("resultCode", "0");
                returnJsonObj.put("resultMsg", "药品设置发票接口异常");
            }
        });

        return returnJsonObj;
    }

    @RequestMapping(value = "/getMiddleOrderShpList", method = {RequestMethod.POST})
    @ResponseBody
    public JSONObject getMiddleOrderShpList(String startTime, String endTime,int page) {
        JSONObject data=new JSONObject();
        data.put("currentPageNumber", String.valueOf(page));
        data.put("starTime",startTime);
        data.put("endTime",endTime);
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
                            getMiddleOrderShpList(startTime,endTime,++page);
                        }
                    }
                }else {
                    log.info("调用收货信息接口失败======"+body.getErr_msg());
                }
                JSONObject returnJsonObj = new JSONObject();
                returnJsonObj.put("resultCode", "1");
                returnJsonObj.put("resultMsg", "共获取"+outputData.getInteger("totalRecordCount"));
                return returnJsonObj;
            }else {
                log.info("调用收货信息接口失败======"+body.getErr_msg());
            }
        } catch (Exception e) {
            e.printStackTrace();
            log.info("调用收货信息接口异常");
        }
        JSONObject returnJsonObj = new JSONObject();
        returnJsonObj.put("resultCode", "0");
        returnJsonObj.put("resultMsg", "调用收货信息接口失败");
        return returnJsonObj;
    }


    @RequestMapping(value = "/getMiddleOrderRetnList", method = {RequestMethod.POST})
    @ResponseBody
    public JSONObject getMiddleOrderRetnList(String startTime, String endTime,int page) {
        JSONObject data = new JSONObject();
        data.put("currentPageNumber",page);
        data.put("strUpTime",startTime);
        data.put("endUpTime",endTime);
        String requestBody = requestService.getRequestBody(SystemConfig.GET_ORDER_RETN, data);
        try {
            IntfResponseBody body = requestService.getDataByUrl(SystemConfig.COMMON_INTERFACES_URL,requestBody);
            if(body.getInfcode()==0){
                JSONObject outputData = body.getOutput().getJSONObject("data");
                if("1".equals(outputData.getString("returnCode"))){
                    List<MiddleOrderRetn> middleOrderRetnList = JSONArray.parseArray(outputData.getString("dataList"), MiddleOrderRetn.class);
                    if (middleOrderRetnList.size() > 0){
                        middleOrderRetnService.saveOrUpdateBatch(middleOrderRetnList);
                        if(page<outputData.getInteger("totalPageCount")){
                            getMiddleOrderRetnList(startTime,endTime,++page);
                        }
                    }
                }else {
                    log.info("调用退货订单接口失败======"+body.getErr_msg());
                }
                JSONObject returnJsonObj = new JSONObject();
                returnJsonObj.put("resultCode", "1");
                returnJsonObj.put("resultMsg", "共获取"+outputData.getInteger("totalRecordCount"));
                return returnJsonObj;
            }else {
                log.info("调用退货订单接口失败======"+body.getErr_msg());
                JSONObject returnJsonObj = new JSONObject();
                returnJsonObj.put("resultCode", "0");
                returnJsonObj.put("resultMsg", body.getErr_msg());
                return returnJsonObj;
            }
        } catch (Exception e) {
            e.printStackTrace();
            log.info("调用退货订单接口失败");
        }
        JSONObject returnJsonObj = new JSONObject();
        returnJsonObj.put("resultCode", "0");
        returnJsonObj.put("resultMsg", "调用退货订单接口失败");
        return returnJsonObj;
    }


    @RequestMapping(value = "/sendOrderDis", method = {RequestMethod.POST})
    @ResponseBody
    public JSONObject sendOrderDis() {
        JSONObject returnJsonObj = new JSONObject();

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
                        returnJsonObj.put("resultCode", "1");
                        returnJsonObj.put("resultMsg", "上传发货信息成功，shpId："+orderDis.getShpId());
                    }else{
                        orderDis.setResponseState("3");
                        returnJsonObj.put("resultCode", "0");
                        returnJsonObj.put("resultMsg", "上传发货信息失败，shpId："+orderDis.getShpId()+","+body.getErr_msg());
                    }
                    orderDis.setResponseInfo(outputData.getString("returnMsg"));

                }else {
                    log.info("药品发货信息上传接口失败======"+body.getErr_msg());
                    orderDis.setResponseState("3");
                    orderDis.setResponseInfo(body.getErr_msg());
                    returnJsonObj.put("resultCode", "0");
                    returnJsonObj.put("resultMsg", "上传发货信息失败，shpId："+orderDis.getShpId()+","+body.getErr_msg());
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
                log.info("药品发货信息上传接口异常");
                returnJsonObj.put("resultCode", "0");
                returnJsonObj.put("resultMsg", "上传发货信息失败，shpId："+orderDis.getShpId()+","+e.getMessage());
            }
        });

        return returnJsonObj;
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
