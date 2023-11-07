package com.trade.controller;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.common.config.SystemConfig;
import com.common.config.TokenRestTemplate;
import com.common.entity.IntfRequestBody;
import com.common.entity.IntfResponseBody;
import com.common.service.MiddleRequestService;
import com.trade.model.BaseDrugInfo;
import com.trade.service.BaseDrugInfoService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@Slf4j
public class TestController {

    @Autowired
    private BaseDrugInfoService baseDrugInfoService;
    @Autowired
    private MiddleRequestService requestService;

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
                    baseDrugInfoService.saveOrUpdateBatch(druginfos);
                    if(page < outputData.getInteger("totalPageCount")){
                        getBaseDrugInfoList(dataList,startTime,endTime,++page);
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
}
