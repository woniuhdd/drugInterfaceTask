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
            syncDatas(1);
        } catch (Exception e) {
            e.printStackTrace();
        }
        log.info("获取商品信息任务执行的时间：" + dateFormat.format(new Date()));
    }

    public void  syncDatas(int page) throws Exception {

        JSONObject data = new JSONObject();
        data.put("current", page);
        data.put("size", 100);

        String requestBody = requestService.getRequestBody(SystemConfig.GET_DRUG, data);
        try {
            //1.解析结果
            IntfResponseBody body = requestService.getDataByUrl(SystemConfig.COMMON_INTERFACES_URL, requestBody);
            JSONObject resultData = body.getOutput().getJSONObject("data");
            if (resultData.getInteger("returnCode") == 0) {
                List<BaseDrugInfo> druginfos = JSONArray.parseArray(resultData.getString("dataList"), BaseDrugInfo.class);
                baseDrugInfoService.saveOrUpdateBatch(druginfos);
                if (page < resultData.getInteger("totalPageCount")) {
                    syncDatas(++page);
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

}
