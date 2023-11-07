package com.common.controller;

import com.alibaba.fastjson.JSONObject;
import com.common.config.SystemConfig;
import com.common.entity.IntfResponseBody;
import com.common.service.MiddleRequestService;
import com.common.utils.DateUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;
import java.util.Date;


@RestController
@Slf4j
public class DemoController {

    @Value("${intf.url}")
    private String url;

    @Resource
    private MiddleRequestService requestService;

    @GetMapping ("/getOrder")
    public void getOrder(){
        JSONObject data=new JSONObject();
        Date now=new Date();
        data.put("currentPageNumber", 1);
        //订单发送日期
        data.put("startTime", DateUtil.dateFormat(now));
        data.put("endTime", DateUtil.dateFormat(now));
        String requestBody = requestService.getRequestBody(SystemConfig.GET_ORDER, data);
        //1.解析结果
        IntfResponseBody body = requestService.getDataByUrl(SystemConfig.COMMON_INTERFACES_URL,requestBody);
        log.info("请求结果==={}",body);
    }
}
