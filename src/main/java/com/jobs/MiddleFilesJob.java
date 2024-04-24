package com.jobs;

import com.alibaba.fastjson.JSONObject;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.common.InterfaceConfigHelper;
import com.common.config.SystemConfig;
import com.common.entity.IntfResponseBody;
import com.common.service.MiddleRequestService;
import com.common.utils.DateUtil;
import com.enums.HttpStatusEnum;
import com.trade.model.MiddleFiles;
import com.trade.service.MiddleFilesService;
import okhttp3.*;
import org.apache.commons.lang.StringUtils;
import org.quartz.JobExecutionContext;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.*;
import java.util.*;

public class MiddleFilesJob implements BaseJob {
    private static final Logger log = LoggerFactory.getLogger(MiddleFilesJob.class);

    private MiddleFilesService filesService = QuartzConfig.getBean(MiddleFilesService.class);
    private MiddleRequestService requestService=QuartzConfig.getBean(MiddleRequestService.class);

    @Override
    public void execute(JobExecutionContext context) {
        try {
            syncDatas();
        } catch (Exception e) {
            e.printStackTrace();
        }
        log.info("发票附件上传任务执行的时间：" + DateUtil.dateTimeFormat(new Date()));
    }

    public void  syncDatas(){
        log.info("发票附件上传接口查询");
        //查询未交互的发票图片
        LambdaQueryWrapper<MiddleFiles> queryWrapper=new LambdaQueryWrapper<MiddleFiles>().
                eq(MiddleFiles::getResponseState,"0");
        List<MiddleFiles> files = filesService.list(queryWrapper);
        files.forEach(invoiceFile->{
            JSONObject object = getFileBase64Str(invoiceFile);
            if(object.getString("code").equals("0")){
                invoiceFile.setResponseState("3");
                invoiceFile.setResponseInfo(object.getString("msg"));
                invoiceFile.setResponseTime(new Date());
                filesService.updateById(invoiceFile);
                return;
            }
            JSONObject data=new JSONObject();
            String url = invoiceFile.getFileUrl();
            data.put("invoId",invoiceFile.getInvoId());
            data.put("fileBase64Str",object.getString("fileBase64Str"));
            data.put("fileName",url.substring(url.lastIndexOf("/")+1));
            String requestParam = requestService.getRequestBody(SystemConfig.SEND_INVOICE_IMG, data);
            try {
                //1.解析结果
                IntfResponseBody body =requestService.getDataByUrl(SystemConfig.COMMON_INTERFACES_URL,requestParam);
                JSONObject outputData = body.getOutput().getJSONObject("data");
                if(body.getInfcode()==0&&outputData.getInteger("returnCode")==1){
                    invoiceFile.setFileId(outputData.getString("fileId"));
                    invoiceFile.setResponseState("2");
                    invoiceFile.setResponseInfo(outputData.getString("returnMsg"));
                }else {
                    log.info("发票附件上传接口失败======"+body.getErr_msg());
                    invoiceFile.setResponseState("3");
                    invoiceFile.setResponseInfo(body.getErr_msg());
                }
                invoiceFile.setResponseTime(new Date());
                filesService.updateById(invoiceFile);
            } catch (Exception e) {
                e.printStackTrace();
                log.info("发票附件上传接口异常");
            }
        });
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

}
