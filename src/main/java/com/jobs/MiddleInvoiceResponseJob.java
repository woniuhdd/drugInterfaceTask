package com.jobs;

import com.alibaba.fastjson.JSONObject;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.common.config.SystemConfig;
import com.common.entity.IntfResponseBody;
import com.common.service.MiddleRequestService;
import com.common.utils.DateUtil;
import com.trade.model.MiddleInvoiceResponse;
import com.trade.service.MiddleInvoiceResponseService;
import org.quartz.JobExecutionContext;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Date;
import java.util.List;

public class MiddleInvoiceResponseJob implements BaseJob {
    private static final Logger log = LoggerFactory.getLogger(MiddleInvoiceResponseJob.class);

    private MiddleInvoiceResponseService invoiceResponseService = QuartzConfig.getBean(MiddleInvoiceResponseService.class);
    private MiddleRequestService requestService=QuartzConfig.getBean(MiddleRequestService.class);

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

        //查询未交互的发票信息
        LambdaQueryWrapper<MiddleInvoiceResponse> queryWrapper=new LambdaQueryWrapper<MiddleInvoiceResponse>().
                eq(MiddleInvoiceResponse::getResponseState,"0");
        List<MiddleInvoiceResponse> invoiceInfos = invoiceResponseService.list(queryWrapper);
        invoiceInfos.forEach(invoiceInfo->{
            JSONObject data=new JSONObject();
            data.put("invoId",invoiceInfo.getInvoId());
            data.put("invoType",invoiceInfo.getInvoType());
            data.put("invoCode",invoiceInfo.getInvoCode());
            data.put("invono",invoiceInfo.getInvono());
            data.put("billAmt",invoiceInfo.getBillAmt());
            data.put("billTime",DateUtil.dateFormat(invoiceInfo.getBillTime()));
            data.put("invottl",invoiceInfo.getInvottl());
            data.put("invoMemo",invoiceInfo.getInvoMemo());
            String requestBody = requestService.getRequestBody(SystemConfig.SEND_INVOICE_INFO, data);

            try {
                //1.解析结果
                IntfResponseBody body = requestService.getDataByUrl(SystemConfig.COMMON_INTERFACES_URL,requestBody);
                JSONObject outputData = body.getOutput().getJSONObject("data");
                if(body.getInfcode()==0&&outputData.getInteger("returnCode")==200){
                    invoiceInfo.setRetnInvoId(outputData.getString("invoId"));
                    invoiceInfo.setResponseState("2");
                    invoiceInfo.setResponseInfo(outputData.getString("returnMsg"));
                }else {
                    log.info("维护发票信息接口失败======"+body.getErr_msg());
                    invoiceInfo.setResponseState("3");
                    invoiceInfo.setResponseInfo(body.getErr_msg());
                }
                invoiceInfo.setResponseTime(new Date());
                invoiceResponseService.updateById(invoiceInfo);
            } catch (Exception e) {
                e.printStackTrace();
                log.info("维护发票信息接口异常");
            }
        });

    }
}
