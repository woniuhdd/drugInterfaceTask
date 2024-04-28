package com.jobs;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.common.config.SystemConfig;
import com.common.entity.IntfResponseBody;
import com.common.service.MiddleRequestService;
import com.trade.model.BaseDrugInfo;
import com.trade.service.BaseDrugInfoService;
import lombok.extern.slf4j.Slf4j;
import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

@Slf4j
public class BaseDrugInfoJob implements BaseJob {

    DateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");

    private BaseDrugInfoService baseDrugInfoService = QuartzConfig.getBean(BaseDrugInfoService.class);

    private MiddleRequestService requestService=QuartzConfig.getBean(MiddleRequestService.class);

    @Override
    public void execute(JobExecutionContext context) throws JobExecutionException {
        try {
            log.info("获取商品信息任务开始的时间：" + dateFormat.format(new Date()));
            syncDatas("1");
        } catch (Exception e) {
            e.printStackTrace();
        }
        log.info("获取商品信息任务执行的时间：" + dateFormat.format(new Date()));
    }

    public void  syncDatas(String page){
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");

        JSONObject data = new JSONObject();
        data.put("currentPageNumber", page);
        Date date = new Date();
        Calendar cal = Calendar.getInstance();
        cal.setTime(date);
        cal.add(Calendar.DAY_OF_MONTH, -15);
        data.put("startTime",sdf.format(cal.getTime()));
        data.put("endTime",sdf.format(date));

        String requestBody = requestService.getRequestBody(SystemConfig.GET_DRUG, data);
        try {
            //1.解析结果
            IntfResponseBody body = requestService.getDataByUrl(SystemConfig.COMMON_INTERFACES_URL, requestBody);
            if(body.getInfcode()==0){
                JSONObject outputData = body.getOutput().getJSONObject("data");
                if ("1".equals(outputData.getString("returnCode"))) {
                    List<BaseDrugInfo> druginfos = JSONArray.parseArray(outputData.getString("dataList"), BaseDrugInfo.class);
                    if (druginfos.size() > 0){
                        baseDrugInfoService.saveOrUpdateBatch(druginfos);
                        int pageTemp = Integer.valueOf(page);
                        if (pageTemp < outputData.getInteger("totalPageCount")) {
                            syncDatas(String.valueOf(++pageTemp));
                        }
                    }
                }else {
                    log.info("获取商品信息失败======"+body.getErr_msg());
                }
            }else {
                log.info("获取商品信息失败======"+body.getErr_msg());
            }

        } catch (Exception e) {
            e.printStackTrace();
            log.info("获取商品信息异常");
        }
    }

}
