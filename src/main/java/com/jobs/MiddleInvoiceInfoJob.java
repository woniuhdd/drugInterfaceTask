package com.jobs;

import com.alibaba.fastjson.JSONObject;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.common.config.SystemConfig;
import com.common.config.TokenRestTemplate;
import com.common.entity.IntfRequestBody;
import com.common.entity.IntfResponseBody;
import com.common.utils.DateUtil;
import com.trade.model.MiddleInvoiceInfo;
import com.trade.service.MiddleInvoiceInfoService;
import org.quartz.JobExecutionContext;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;

import java.util.Date;
import java.util.List;

public class MiddleInvoiceInfoJob implements BaseJob {
    private static final Logger log = LoggerFactory.getLogger(MiddleInvoiceInfoJob.class);

    private MiddleInvoiceInfoService invoiceInfoService = QuartzConfig.getBean(MiddleInvoiceInfoService.class);
    private TokenRestTemplate tokenRestTemplate=QuartzConfig.getBean(TokenRestTemplate.class);
    @Override
    public void execute(JobExecutionContext context) {
        try {
            syncDatas();
        } catch (Exception e) {
            e.printStackTrace();
        }
        log.info("维护发票信息任务执行的时间：" + DateUtil.dateTimeFormat(new Date()));
    }

    public void  syncDatas(){
        log.info("维护发票信息接口查询");
        IntfRequestBody requestBody=new IntfRequestBody();
        requestBody.setInfno(SystemConfig.SEND_INVOICE_INFO);
        //查询未交互的发票信息
        LambdaQueryWrapper<MiddleInvoiceInfo> queryWrapper=new LambdaQueryWrapper<MiddleInvoiceInfo>().
                eq(MiddleInvoiceInfo::getResponseState,"0");
        List<MiddleInvoiceInfo> invoiceInfos = invoiceInfoService.list(queryWrapper);
        invoiceInfos.forEach(invoiceInfo->{
            JSONObject input=new JSONObject();
            input.put("invoId",invoiceInfo.getInvoId());
            input.put("invoType",invoiceInfo.getInvoType());
            input.put("invoCode",invoiceInfo.getInvoCode());
            input.put("invono",invoiceInfo.getInvono());
            input.put("billAmt",invoiceInfo.getBillAmt());
            input.put("billTime",invoiceInfo.getBillTime());
            input.put("invottl",invoiceInfo.getInvottl());
            input.put("invoMemo",invoiceInfo.getInvoMemo());
            requestBody.setInput(input);

            HttpHeaders headers=new HttpHeaders();
            headers.add("content-type","application/json;charset=utf-8");
            HttpEntity<String> reqBody=new HttpEntity<>(JSONObject.toJSONString(requestBody),headers);
            try {
                ResponseEntity<IntfResponseBody> responseEntity = tokenRestTemplate.exchange(SystemConfig.url + SystemConfig.COMMON_INTERFACES_URL,
                        HttpMethod.POST, reqBody, IntfResponseBody.class);
                //1.解析结果
                IntfResponseBody body =responseEntity.getBody();
                if(body.getInfcode()==0){
                    JSONObject outputData = JSONObject.parseObject(body.getOutput()).getJSONObject("data");
                    invoiceInfo.setInvoId(outputData.getString("invoId"));
                    if("0".equals(outputData.getString("returnCode"))){
                        invoiceInfo.setResponseState("2");
                    }else{
                        invoiceInfo.setResponseState("3");
                    }
                    invoiceInfo.setResponseInfo(outputData.getString("returnMsg"));
                }else {
                    log.info("维护发票信息接口失败======"+body.getErr_msg());
                    invoiceInfo.setResponseState("3");
                    invoiceInfo.setResponseInfo(body.getErr_msg());
                }
                invoiceInfo.setResponseTime(new Date());
                invoiceInfoService.updateById(invoiceInfo);
            } catch (Exception e) {
                e.printStackTrace();
                log.info("维护发票信息接口异常");
            }
        });

    }
}
