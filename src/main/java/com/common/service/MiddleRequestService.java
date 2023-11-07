package com.common.service;

import com.alibaba.fastjson.JSONObject;
import com.common.entity.IntfResponseBody;

public interface MiddleRequestService {

    /**
     * 格式化请求参数
     * @param infoNo
     * @param data
     * @return
     */
    String getRequestBody(String infoNo, JSONObject data);

    /**
     * 调用请求
     * @param url
     * @param requestParam
     * @return
     */
    IntfResponseBody getDataByUrl(String url, String requestParam);
}
