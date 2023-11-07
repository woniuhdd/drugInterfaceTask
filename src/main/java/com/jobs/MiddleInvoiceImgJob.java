package com.jobs;

import com.alibaba.fastjson.JSONObject;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.common.config.SystemConfig;
import com.common.config.TokenRestTemplate;
import com.common.entity.IntfRequestBody;
import com.common.entity.IntfResponseBody;
import com.common.utils.DateUtil;
import com.trade.model.MiddleInvoiceImg;
import com.trade.service.MiddleInvoiceImgService;
import org.apache.commons.lang.StringUtils;
import org.quartz.JobExecutionContext;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.*;

public class MiddleInvoiceImgJob implements BaseJob {
    private static final Logger log = LoggerFactory.getLogger(MiddleInvoiceImgJob.class);

    private MiddleInvoiceImgService invoiceImgService = QuartzConfig.getBean(MiddleInvoiceImgService.class);
    private TokenRestTemplate tokenRestTemplate=QuartzConfig.getBean(TokenRestTemplate.class);
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
        IntfRequestBody requestBody=new IntfRequestBody();
        IntfRequestBody.RequestInfo info = new IntfRequestBody.RequestInfo();
        Map<String,JSONObject> input=new HashMap<>();
        requestBody.setInfo(info);
        info.setInfno(SystemConfig.SEND_INVOICE_IMG);
        //查询未交互的发票图片
        LambdaQueryWrapper<MiddleInvoiceImg> queryWrapper=new LambdaQueryWrapper<MiddleInvoiceImg>().
                eq(MiddleInvoiceImg::getResponseState,"0");
        List<MiddleInvoiceImg> invoiceImgs = invoiceImgService.list(queryWrapper);
        invoiceImgs.forEach(invoiceImg->{
            JSONObject object = getFileBase64Str(invoiceImg);
            if(object.getString("code").equals("0")){
                invoiceImg.setResponseState("3");
                invoiceImg.setResponseInfo(object.getString("msg"));
                invoiceImg.setResponseTime(new Date());
                invoiceImgService.updateById(invoiceImg);
                return;
            }
            JSONObject data=new JSONObject();
            data.put("invoId",invoiceImg.getInvoId());
            data.put("fileBase64Str",object.getString("fileBase64Str"));
            data.put("fileName",invoiceImg.getFileName());
            input.put("data",data);
            info.setInput(input);

            HttpHeaders headers=new HttpHeaders();
            headers.add("content-type","application/json;charset=utf-8");
            HttpEntity<String> reqBody=new HttpEntity<>(JSONObject.toJSONString(requestBody),headers);
            try {
                ResponseEntity<IntfResponseBody> responseEntity = tokenRestTemplate.exchange(SystemConfig.url + SystemConfig.COMMON_INTERFACES_URL,
                        HttpMethod.POST, reqBody, IntfResponseBody.class);
                //1.解析结果
                IntfResponseBody body =responseEntity.getBody();
                if(body.getInfcode()==0){
                    JSONObject outputData = body.getOutput().getJSONObject("data");
                    invoiceImg.setFileId(outputData.getString("fileId"));
                    if("0".equals(outputData.getString("returnCode"))){
                        invoiceImg.setResponseState("2");
                    }else{
                        invoiceImg.setResponseState("3");
                    }
                    invoiceImg.setResponseInfo(outputData.getString("returnMsg"));
                }else {
                    log.info("发票附件上传接口失败======"+body.getErr_msg());
                    invoiceImg.setResponseState("3");
                    invoiceImg.setResponseInfo(body.getErr_msg());
                }
                invoiceImg.setResponseTime(new Date());
                invoiceImgService.updateById(invoiceImg);
            } catch (Exception e) {
                e.printStackTrace();
                log.info("发票附件上传接口异常");
            }
        });
    }

    public JSONObject getFileBase64Str(MiddleInvoiceImg invoiceImg){
        JSONObject jsonObject = new JSONObject();
        jsonObject.put("code", "1");
        jsonObject.put("msg", "成功");
        FileInputStream fileInputStream=null;
        try {
            if (StringUtils.isEmpty(invoiceImg.getImgUrl())) {
                jsonObject.put("code", "0");
                jsonObject.put("msg", String.format("准备上传阶段-->该图片地址为空（数据库主键编号：%s），请检查数据库该字段的【url】内容后再试。", invoiceImg.getUuid()));
                return jsonObject;
            }
            if (StringUtils.isEmpty(invoiceImg.getInvoId())) {
                jsonObject.put("code", "0");
                jsonObject.put("msg", String.format("准备上传阶段-->该图片发票Id为空（数据库主键编号：%s），请检查数据库该字段的【invoId】内容后再试。", invoiceImg.getUuid()));
                return jsonObject;
            }
            if (!(invoiceImg.getFileName().toLowerCase().endsWith(".jpg")||
                    invoiceImg.getFileName().toLowerCase().endsWith(".png"))) {
                jsonObject.put("code", "0");
                jsonObject.put("msg", String.format("准备上传阶段-->图片格式必须为jpg/png（数据库主键编号：%s）", invoiceImg.getUuid()));
                return jsonObject;
            }
            File file = new File(invoiceImg.getImgUrl());
            byte[] fileBase64Byte=new byte[(int)file.length()];
            fileInputStream=new FileInputStream(file);
            fileInputStream.read(fileBase64Byte);
            jsonObject.put("fileBase64Str",Base64.getEncoder().encodeToString(fileBase64Byte));
        } catch (Exception e) {
            e.printStackTrace();
        }finally {
            if(fileInputStream!=null) {
                try {
                    fileInputStream.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }

        return jsonObject;
    }
}
