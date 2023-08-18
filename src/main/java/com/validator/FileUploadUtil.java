package com.validator;

import org.apache.http.HttpEntity;
import org.apache.http.HttpStatus;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.config.RequestConfig;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.ByteArrayEntity;
import org.apache.http.entity.ContentType;
import org.apache.http.entity.mime.MultipartEntityBuilder;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.util.EntityUtils;

import java.io.*;
import java.nio.charset.StandardCharsets;
import java.util.Base64;

public class FileUploadUtil {


    private static final String url = "http://localhost:8097/fsi/api/rsfComIfsService/callService";
    /** 按照报文要求传入JSON格式字符串 */
    private static final String downInput = "{…}";

    /**
     * 调用普通交易及文件下载交易
     */
    public void test1() {
        CloseableHttpClient httpclient = HttpClients.createDefault();
        HttpPost httppost = new HttpPost(url);
        RequestConfig requestConfig = RequestConfig.custom().setConnectTimeout(10000).setSocketTimeout(10000).build();
        httppost.setConfig(requestConfig);
        ByteArrayEntity entity = new ByteArrayEntity(downInput.getBytes(StandardCharsets.UTF_8));
        entity.setContentType("text/plain");
        httppost.setEntity(entity);
        CloseableHttpResponse response = null;
        try {
            response = httpclient.execute(httppost);
            int statusCode = response.getStatusLine().getStatusCode();
            if (statusCode != HttpStatus.SC_OK) {
                httppost.abort();
                throw new RuntimeException("HttpClient,error status code :" + statusCode);
            }
            HttpEntity responseEntity = response.getEntity();
            String result;
            if (responseEntity != null) {
                if (responseEntity.getContentType().getValue().contains("application/octet-stream")) {
                    InputStream content = responseEntity.getContent();
                    //返回文件流
                    File file = new File("testDownload.txt");
                    FileOutputStream fileOutputStream = new FileOutputStream(file);
                    int temp;
                    while ((temp = content.read()) != -1) {
                        fileOutputStream.write(temp);
                    }
                    fileOutputStream.close();
                } else {
                    //返回字符串
                    result = EntityUtils.toString(responseEntity, "UTF-8");
                    System.out.println(result);
                }
            }
            EntityUtils.consume(entity);
        } catch (ClientProtocolException e) {
            throw new RuntimeException("提交给服务器的请求，不符合HTTP协议", e);
        } catch (IOException e) {
            throw new RuntimeException("向服务器承保接口发起http请求,执行post请求异常", e);
        } finally {
            if (response != null) {
                try {
                    response.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
            if (httpclient != null) {
                try {
                    httpclient.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
    }

    /**
     * 调用文件上传交易
     */
    public void test2(String fileName) {
        File file = new File(fileName);
        FileInputStream fileInputStream=null;
        byte[] fileBase64Byte=new byte[(int)file.length()];
        try {
            fileInputStream=new FileInputStream(file);
            fileInputStream.read(fileBase64Byte);
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
        String fileBase64Str = Base64.getEncoder().encodeToString(fileBase64Byte);
        CloseableHttpClient httpclient = HttpClients.createDefault();
        HttpPost httppost = new HttpPost(url);
        RequestConfig requestConfig = RequestConfig.custom().setConnectTimeout(10000).setSocketTimeout(10000).build();
        httppost.setConfig(requestConfig);
        MultipartEntityBuilder builder = MultipartEntityBuilder.create();
        builder.setCharset(StandardCharsets.UTF_8);
        builder.addTextBody("fileName", fileName);
        builder.addBinaryBody("fileBase64Str", file, ContentType.DEFAULT_BINARY, fileName);
        HttpEntity entity = builder.build();
        httppost.setEntity(entity);
        CloseableHttpResponse response = null;
        try {
            response = httpclient.execute(httppost);
            int statusCode = response.getStatusLine().getStatusCode();
            if (statusCode != HttpStatus.SC_OK) {
                httppost.abort();
                throw new RuntimeException("HttpClient,error status code :" + statusCode);
            }
            HttpEntity responseEntity = response.getEntity();
            String result;
            if (responseEntity != null) {
                //返回字符串
                result = EntityUtils.toString(responseEntity, "UTF-8");
                System.out.println(result);
            }
            EntityUtils.consume(entity);
        } catch (ClientProtocolException e) {
            throw new RuntimeException("提交给服务器的请求，不符合HTTP协议", e);
        } catch (IOException e) {
            throw new RuntimeException("向服务器承保接口发起http请求,执行post请求异常", e);
        } finally {
            if (response != null) {
                try {
                    response.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
            if (httpclient != null) {
                try {
                    httpclient.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
    }
}
