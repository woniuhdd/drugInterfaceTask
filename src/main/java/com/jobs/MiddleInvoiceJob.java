package com.jobs;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.common.config.SystemConfig;
import com.common.entity.IntfResponseBody;
import com.common.service.MiddleRequestService;
import com.common.utils.DateUtil;
import com.trade.model.MiddleInvoice;
import com.trade.service.MiddleInvoiceService;
import org.quartz.JobExecutionContext;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Calendar;
import java.util.Date;
import java.util.List;

public class MiddleInvoiceJob implements BaseJob {
    private static final Logger log = LoggerFactory.getLogger(MiddleInvoiceJob.class);

    private MiddleInvoiceService invoiceService = QuartzConfig.getBean(MiddleInvoiceService.class);
    private MiddleRequestService requestService=QuartzConfig.getBean(MiddleRequestService.class);

    @Override
    public void execute(JobExecutionContext context) {
        try {
            syncDatas("1");
        } catch (Exception e) {
            e.printStackTrace();
        }
        log.info("获取发票信息任务执行的时间：" + DateUtil.dateTimeFormat(new Date()));
    }

    public void  syncDatas(String page){
        log.info("获取发票信息接口查询");
        JSONObject data=new JSONObject();
        Date now=new Date();
        data.put("currentPageNumber", String.valueOf(page));
        //发票上传时间
        Calendar cal = Calendar.getInstance();
        cal.setTime(now);
        cal.add(Calendar.DAY_OF_MONTH, -1);
        data.put("startTime", DateUtil.dateFormat(cal.getTime()) + " 00:00:00");
        data.put("endTime", DateUtil.dateFormat(now) + " 23:59:59");
        String requestBody = requestService.getRequestBody(SystemConfig.GET_INVOICE_INFO, data);
        try {
            //1.解析结果
            IntfResponseBody body = requestService.getDataByUrl(SystemConfig.COMMON_INTERFACES_URL,requestBody);
            if(body.getInfcode()==0){
                JSONObject outputData = body.getOutput().getJSONObject("data");
                if("200".equals(outputData.getString("returnCode"))){
                    List<MiddleInvoice> invoiceList = JSONArray.parseArray(outputData.getString("dataList"), MiddleInvoice.class);
                    if (invoiceList.size() > 0){
                        invoiceService.saveOrUpdateBatch(invoiceList);
                        int pageTemp = Integer.valueOf(page);
                        if(pageTemp<outputData.getInteger("totalPageCount")){
                            syncDatas(String.valueOf(++pageTemp));
                        }
                    }
                }else {
                    log.info("调用获取发票信息接口失败======"+body.getErr_msg());
                }
            }else {
                log.info("调用获取发票信息接口失败======"+body.getErr_msg());
            }
        } catch (Exception e) {
            e.printStackTrace();
            log.info("调用获取发票信息接口异常");
        }
    }
}
