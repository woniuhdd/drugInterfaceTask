package com.common.entity;

import com.alibaba.fastjson.JSONObject;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Map;


/**
 * 接口请求入参
 */
@NoArgsConstructor
@Data
public class IntfRequestBody {

  private RequestInfo info;

    @NoArgsConstructor
    @Data
    public static class RequestInfo{
        /**
         * 交易编号
         */
        private String infno;

        /**
         * 交易输入
         */
        private Map<String,JSONObject> input;
    }
}
