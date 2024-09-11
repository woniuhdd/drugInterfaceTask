package com.jobs;

import com.alibaba.fastjson.JSONObject;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.common.config.SystemConfig;
import com.common.entity.IntfResponseBody;
import com.common.service.MiddleRequestService;
import com.common.utils.DateUtil;
import com.trade.model.MiddleInvoiceResponse;
import com.trade.model.MiddleInvoiceShp;
import com.trade.service.MiddleInvoiceResponseService;
import com.trade.service.MiddleInvoiceShpService;
import org.quartz.JobExecutionContext;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Arrays;
import java.util.Date;
import java.util.List;

public class MiddleInvoiceShpJob implements BaseJob {
    private static final Logger log = LoggerFactory.getLogger(MiddleInvoiceShpJob.class);

    private MiddleInvoiceShpService invoiceShpService = QuartzConfig.getBean(MiddleInvoiceShpService.class);
    private MiddleRequestService requestService=QuartzConfig.getBean(MiddleRequestService.class);

    @Override
    public void execute(JobExecutionContext context) {
        try {
            syncDatas();
        } catch (Exception e) {
            e.printStackTrace();
        }
        log.info("药品设置发票任务执行的时间：" + DateUtil.dateTimeFormat(new Date()));
    }

    public void  syncDatas(){
        log.info("药品设置发票接口");
        //查询未交互的发票信息
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
                if(body.getInfcode()==0&&outputData.getInteger("returnCode")==200){
                    invoiceShp.setResponseState("2");
                    invoiceShp.setResponseInfo(outputData.getString("returnMsg"));
                }else {
                    log.info("药品设置发票接口失败======"+body.getErr_msg());
                    invoiceShp.setResponseState("3");
                    invoiceShp.setResponseInfo(body.getErr_msg());
                }
                invoiceShp.setResponseTime(new Date());
                invoiceShpService.updateById(invoiceShp);
            } catch (Exception e) {
                e.printStackTrace();
                log.info("药品设置发票接口异常");
            }
        });

    }
}
