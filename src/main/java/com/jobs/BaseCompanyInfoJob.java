package com.jobs;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.common.config.SystemConfig;
import com.common.entity.IntfResponseBody;
import com.common.service.MiddleRequestService;
import com.trade.model.BaseCompanyInfo;
import com.trade.service.BaseCompanyInfoManager;
import org.quartz.JobExecutionContext;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

public class BaseCompanyInfoJob implements BaseJob {
    private static final Logger log = LoggerFactory.getLogger(BaseCompanyInfoJob.class);
    DateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");

    private BaseCompanyInfoManager baseCompanyInfoManager = QuartzConfig.getBean(BaseCompanyInfoManager.class);
    private MiddleRequestService requestService=QuartzConfig.getBean(MiddleRequestService.class);

    @Override
    public void execute(JobExecutionContext context) {
        try {
            syncDatas(1);
        } catch (Exception e) {
            e.printStackTrace();
        }
        log.info("获取企业信息任务执行的时间：" + dateFormat.format(new Date()));
    }

    public void  syncDatas( int page){
        log.info("生产企业接口查询");
        JSONObject data=new JSONObject();
        data.put("current",page);
        data.put("size",100);
        String requestBody = requestService.getRequestBody(SystemConfig.GET_COMPANY, data);
        try {
            //1.解析结果
            IntfResponseBody body =requestService.getDataByUrl(SystemConfig.COMMON_INTERFACES_URL,requestBody);
            if(body.getInfcode()==0){
                if(page==1){
                    baseCompanyInfoManager.deleteAllDatas();
                }
                JSONObject outputData = body.getOutput().getJSONObject("data");
                List<BaseCompanyInfo> companys = JSONArray.parseArray(outputData.getString("dataList"), BaseCompanyInfo.class);
                baseCompanyInfoManager.saveBatch(companys);
                if(page<outputData.getInteger("totalPageCount")){
                    syncDatas( ++page);
                }
            }else {
                log.info("调用生产企业接口失败======"+body.getErr_msg());
            }
        } catch (Exception e) {
            e.printStackTrace();
            log.info("调用生产企业接口异常");
        }
    }

}
